package com.elegantsolutions.hieroglyphic.gift.ui.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;

public interface Actions {

    interface Application {
        int CAPTURE_IMAGE_FROM_CAMERA = 0;
        int LOAD_IMAGE_FROM_GALLERY = 1;
        int SHARE_IMAGE_FB = 2;
        int SHARE_IMAGE_TWITTER = 3;
        int SHARE_IMAGE_GPLUS = 4;
        int SHARE_IMAGE_EMAIL = 5;
        int SAVE_IMAGE_IN_GALLERY = 6;
    }

    interface Request {
        int EXTERNAL_STORAGE = 1;
        int CAMERA_ACCESS = 2;
    }

    interface Param {
        String PHOTO_PATH = "photoPath";
        String USER_NAME = "userName";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    interface Permission {
        String[] READ_WRITE_EXTERNAL_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        String[] CAMERA = {
                Manifest.permission.CAMERA
        };
    }
}
