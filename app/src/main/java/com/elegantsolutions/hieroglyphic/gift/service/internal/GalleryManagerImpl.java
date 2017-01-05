package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.os.Environment;

import com.elegantsolutions.hieroglyphic.gift.service.GalleryManager;

import java.io.File;

class GalleryManagerImpl implements GalleryManager {

    @Override
    public String getGalleryPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
    }

    @Override
    public String getAppGalleryPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/pharaohCard/";
    }

    @Override
    public void createAppGalleryDirectory() {
        String appDir = getAppGalleryPath();
        File newdir = new File(appDir);

        newdir.mkdirs();
    }
}
