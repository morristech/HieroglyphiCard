package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.app.ProgressDialog;
import android.content.Context;

import com.elegantsolutions.hieroglyphic.gift.R;
import com.elegantsolutions.hieroglyphic.gift.service.ProgressManager;

class ProgressManagerImpl implements ProgressManager {
    @Override
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

    @Override
    public void endProgressDialog(ProgressDialog dialog) {
        dialog.dismiss();
    }
}
