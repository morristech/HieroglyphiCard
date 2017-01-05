package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.elegantsolutions.hieroglyphic.gift.service.MeasurementManager;

class MeasurementManagerImpl implements MeasurementManager {
    private static final int MAX_HEIGHT = 200;

    @Override
    public boolean isTablet(Activity activity) {
        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getProperMaximumHeight(Activity activity) {
        if (isTablet(activity)) {
            return MAX_HEIGHT + 100;
        } else {
            return MAX_HEIGHT;
        }
    }
}
