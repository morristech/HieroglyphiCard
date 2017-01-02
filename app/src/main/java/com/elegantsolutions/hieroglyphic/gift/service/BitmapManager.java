package com.elegantsolutions.hieroglyphic.gift.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ScrollView;

import java.io.IOException;
import java.io.InputStream;

public class BitmapManager {
    private static final BitmapManager bitmapManager = new BitmapManager();

    private BitmapManager() {
    }

    public static BitmapManager getInstance() {
        return bitmapManager;
    }

    public Bitmap loadBitmap(Activity activity, int ID) {
        Bitmap b = null;

        try {
            InputStream inputStream = activity.getResources().openRawResource(ID);
            b = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }

    public Bitmap augmentHorizontalBitmaps(Bitmap[] bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }

        int totalWidth = 0;
        int totalHeight = bitmaps[0].getHeight();

        for (Bitmap bitmap:bitmaps) {
            totalWidth += bitmap.getWidth();
        }

        Bitmap bmOverlay = Bitmap.createBitmap(totalWidth,
                totalHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmOverlay);

        int currentX = 0;

        for (Bitmap bitmap:bitmaps) {

            canvas.drawBitmap(bitmap, currentX, 0, null);

            currentX += bitmap.getWidth();
        }

        return bmOverlay;
    }

    public Bitmap augmentVerticalBitmaps(Bitmap[] bitmaps, int spacing) {
        if (bitmaps.length <= 0) {
            return null;
        }

        int totalWidth = bitmaps[0].getWidth();
        int totalHeight = 0;

        for (Bitmap bitmap:bitmaps) {
            totalHeight += bitmap.getHeight();

            if (totalWidth < bitmap.getWidth()) {
                totalWidth = bitmap.getWidth();
            }
        }

        Bitmap bmOverlay = Bitmap.createBitmap(totalWidth,
                totalHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmOverlay);

        int currentY = 0;

        for (Bitmap bitmap:bitmaps) {

            canvas.drawBitmap(bitmap, 0, currentY + spacing, null);

            currentY += bitmap.getHeight();
        }

        return bmOverlay;
    }

    @TargetApi(16)
    @SuppressWarnings("deprecation")
    public Bitmap getBitmapFromScrollableView(Activity activity, final ScrollView view) {
        final int totalHeight = view.getChildAt(0).getHeight();
        final int totalWidth = view.getChildAt(0).getWidth();

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight , Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(returnedBitmap);

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Drawable bgDrawable = ((View) view.getParent()).getBackground();

                Drawable clone = bgDrawable.getConstantState().newDrawable();

                bgDrawable.setBounds(0,0, totalWidth, totalHeight);

                if (bgDrawable != null) {
                    bgDrawable.draw(canvas);
                }

                view.draw(canvas);

                // Restore old background ...
                if (Build.VERSION.SDK_INT >= 16) {
                    ((View) view.getParent()).setBackground(clone);
                } else {
                    ((View) view.getParent()).setBackgroundDrawable(clone);
                }
            }
        });

        // Wait until drawing the view ...
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnedBitmap;
    }
}
