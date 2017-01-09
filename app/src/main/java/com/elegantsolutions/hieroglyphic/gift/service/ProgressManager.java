package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.ProgressDialog;
import android.content.Context;

public interface ProgressManager {
    ProgressDialog startProgressDialog(Context context);

    void endProgressDialog(ProgressDialog dialog);
}
