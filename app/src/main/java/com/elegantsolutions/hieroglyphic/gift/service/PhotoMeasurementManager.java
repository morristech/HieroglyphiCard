package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;

public interface PhotoMeasurementManager {
    boolean isTablet(Activity activity);

    int getProperMaximumHeight(Activity activity);
}
