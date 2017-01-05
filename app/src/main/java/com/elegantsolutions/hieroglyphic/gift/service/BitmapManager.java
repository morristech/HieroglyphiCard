package com.elegantsolutions.hieroglyphic.gift.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ScrollView;

public interface BitmapManager {
    Bitmap loadBitmap(Activity activity, int ID);

    Bitmap augmentHorizontalBitmaps(Bitmap[] bitmaps);

    Bitmap augmentVerticalBitmaps(Bitmap[] bitmaps, int spacing);

    @TargetApi(16)
    @SuppressWarnings("deprecation")
    Bitmap getBitmapFromScrollableView(Activity activity, ScrollView view);
}
