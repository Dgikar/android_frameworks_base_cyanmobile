/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.android.systemui.carrierlabels;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.CmSystem;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.View;
import android.widget.TextView;

import com.android.internal.R;

/**
 * This widget display the current network status or registered PLMN, and/or
 * SPN if available.
 */
public class CenterCarrierLogo extends TextView {
    private boolean mAttached;

    private boolean mCarrierLogoCenter;

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
                    Settings.System.getUriFor(Settings.System.CARRIER_LOGO),
                    false, this);
            onChange(true);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateSettings();
        }
    }

    public CenterCarrierLogo(Context context) {
        this(context, null);
    }

    public CenterCarrierLogo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterCarrierLogo(Context context, AttributeSet attrs, int defStyle) {
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
            mSettingsObserver.observe();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            mAttached = false;
            mContext.getContentResolver().unregisterContentObserver(mSettingsObserver);
        }
    }

    private void updateSettings() {
        ContentResolver resolver = mContext.getContentResolver();

        mCarrierLogoCenter = (Settings.System.getInt(resolver,
                Settings.System.CARRIER_LOGO, 0) == 2);

        if(mCarrierLogoCenter)
            setVisibility(View.VISIBLE);
        else
            setVisibility(View.GONE);
    }

}