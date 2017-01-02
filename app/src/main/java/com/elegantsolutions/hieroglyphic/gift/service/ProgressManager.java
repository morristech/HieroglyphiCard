package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.ProgressDialog;
import android.content.Context;

import com.elegantsolutions.hieroglyphic.gift.R;

public class ProgressManager {
    private static ProgressManager progressManager;

    private ProgressManager() {
    }

    public static ProgressManager getInstance() {
        if (progressManager == null) {
            progressManager = new ProgressManager();
        }

        return progressManager;
    }

    public ProgressDialog startProgressDialog(Context context) {
        if (context == null) {
            throw new IllegalStateException("Context is null. Cannot create ProgressManager");
        }

        ProgressDialog dialog = ProgressDialog.show(context,
                context.getString(R.string.loading_message),
                context.getString(R.string.please_wait_message),
                true);

        return dialog;
    }

    public void endProgressDialog(ProgressDialog dialog) {
        dialog.dismiss();
    }
}
