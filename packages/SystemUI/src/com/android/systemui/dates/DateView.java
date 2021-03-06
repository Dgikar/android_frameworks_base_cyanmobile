/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.dates;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;

import android.util.AttributeSet;
import android.util.Slog;
import android.widget.TextView;
import android.view.View;
import android.util.TypedValue;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.Date;

public final class DateView extends TextView {
    private static final String TAG = "DateView";

    private boolean mUpdating = false;

    private int mCarrierSize;
    private boolean mAttached;

    private Handler mHandler;
    private Context mContext;
    private SettingsObserver mSettingsObserver;

    private class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(
                    Settings.System.getUriFor(Settings.System.STATUSBAR_ICON_FONT_SIZE), false, this);
        }

        @Override public void onChange(boolean selfChange) {
            updateSettings();
        }
    }


    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                updateSettings();
            }
            updateClock();
        }
    };

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mHandler = new Handler();
        mSettingsObserver = new SettingsObserver(mHandler);
        updateSettings();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
            mContext.registerReceiver(mIntentReceiver, filter, null, getHandler());
            mSettingsObserver.observe();
        }

        updateClock();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            mContext.unregisterReceiver(mIntentReceiver);
            mContext.getContentResolver().unregisterContentObserver(mSettingsObserver);
            mAttached = false;
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        // makes the large background bitmap not force us to full width
        return 0;
    }

    private final void updateClock() {
        Date now = new Date();
        Resources res = Resources.getSystem();
        setText(DateFormat.format("EEEE, MMMM d, yyyy",now));
        setTextSize(mCarrierSize);
    }

    private void updateSettings(){
        ContentResolver resolver = mContext.getContentResolver();
        int defValuesFontSize = mContext.getResources().getInteger(com.android.internal.R.integer.config_fontsize_default_cyanmobile);
        float mCarrierSizeval = (float) Settings.System.getInt(resolver,
                Settings.System.STATUSBAR_ICON_FONT_SIZE, defValuesFontSize);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int CarrierSizepx = (int) (metrics.density * mCarrierSizeval);
        mCarrierSize = CarrierSizepx;

           if (mAttached) {
                updateClock();
           }
    }
}

