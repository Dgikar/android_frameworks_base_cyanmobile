package com.android.systemui.statusbar.quicksettings.quicktile;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.android.systemui.R;
import com.android.systemui.statusbar.quicksettings.QuickSettingsController;
import com.android.systemui.statusbar.quicksettings.QuickSettingsContainerView;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NetworkController.NetworkSignalChangedCallback;

public class AirplaneModeTile extends QuickSettingsTile implements NetworkSignalChangedCallback{

    private boolean mEnabled = false;

    public AirplaneModeTile(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container, QuickSettingsController qsc) {
        super(context, inflater, container, qsc);

        mLabel = mContext.getString(R.string.quick_settings_airplane_mode_label);

        mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             // Change the system setting
                Settings.System.putInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
                                        !mEnabled ? 1 : 0);

                // Post the intent
                Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                intent.putExtra("state", !mEnabled);
                mContext.sendBroadcast(intent);
            }
        };
        mOnLongClick = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                startSettingsActivity(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                return true;
            }
        };
    }

    @Override
    void onPostCreate() {
        NetworkController controller = new NetworkController(mContext);
        controller.addNetworkSignalChangedCallback(this);
        super.onPostCreate();
    }

    @Override
    public void onWifiSignalChanged(boolean enabled, int wifiSignalIconId, String description) {
    }

    @Override
    public void onMobileDataSignalChanged(boolean enabled,
            int mobileSignalIconId, int dataTypeIconId, String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAirplaneModeChanged(boolean enabled) {
        mEnabled = enabled;
        if(enabled){
            mDrawable = R.drawable.stat_airplane_on;
        }else{
            mDrawable = R.drawable.stat_airplane_off;
        }
        updateQuickSettings();
    }

}