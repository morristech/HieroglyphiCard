package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by hazemsaleh on 1/5/17.
 */
public interface ProgressManager {
    ProgressDialog startProgressDialog(Context context);

    void endProgressDialog(ProgressDialog dialog);
}
