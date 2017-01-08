package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;
import android.graphics.Bitmap;

public interface ImageManager {
    void showImage(Activity activity, int ID, String photoPath);

    void showImage(Activity activity, int ID, Bitmap src);

    int getProperImageInSample(Activity activity, String photoPath);

    void saveViewAsPapyrusImage(Activity activity, int scrollableViewID,
                                String path, String outFileName) throws Exception;
}
