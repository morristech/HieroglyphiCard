package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Display;

import com.elegantsolutions.hieroglyphic.gift.service.PhotoMeasurementManager;

class MeasurementManagerImpl implements PhotoMeasurementManager {
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
            return getMaxPhotoHeight(activity) + 100;
        } else {
            return getMaxPhotoHeight(activity);
        }
    }

    private int getMaxPhotoHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();

        return display.getHeight() / 3;
    }
}
