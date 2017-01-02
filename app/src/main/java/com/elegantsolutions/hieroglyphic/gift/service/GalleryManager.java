package com.elegantsolutions.hieroglyphic.gift.service;

import android.os.Environment;

import java.io.File;

public class GalleryManager {
    private static final GalleryManager galleryManager = new GalleryManager();

    private GalleryManager() {
    }

    public static GalleryManager getInstance() {
        return galleryManager;
    }
    
    public String getGalleryPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
    }

    public String getAppGalleryPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/pharaohCard/";
    }

    public void createAppGalleryDirectory() {
        String appDir = getAppGalleryPath();
        File newdir = new File(appDir);

        newdir.mkdirs();
    }
}
