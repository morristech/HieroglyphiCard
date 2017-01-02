package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class MeasurementManager {
    private static final int MAX_HEIGHT = 200;
    private static final MeasurementManager measurementManager = new MeasurementManager();

    private MeasurementManager() {
    }

    public static MeasurementManager getInstance() {
        return measurementManager;
    }

    public boolean isTablet(Activity activity) {
        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
            return true;
        } else {
            return false;
        }
    }

    public int getProperMaximumHeight(Activity activity) {
        if (isTablet(activity)) {
            return MAX_HEIGHT + 100;
        } else {
            return MAX_HEIGHT;
        }
    }
}
