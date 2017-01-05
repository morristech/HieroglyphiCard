package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;

/**
 * Created by hazemsaleh on 1/5/17.
 */
public interface MeasurementManager {
    boolean isTablet(Activity activity);

    int getProperMaximumHeight(Activity activity);
}
