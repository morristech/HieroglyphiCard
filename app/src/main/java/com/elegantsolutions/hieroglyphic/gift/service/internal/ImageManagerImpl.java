package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.elegantsolutions.hieroglyphic.gift.service.BitmapManager;
import com.elegantsolutions.hieroglyphic.gift.service.ImageManager;
import com.elegantsolutions.hieroglyphic.gift.service.PhotoMeasurementManager;
import com.elegantsolutions.hieroglyphic.gift.ui.activities.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class ImageManagerImpl implements ImageManager {
    private static final String TAG = ImageManagerImpl.class.getSimpleName();

    private BitmapManager bitmapManager;
    private PhotoMeasurementManager measurementManager;

    public ImageManagerImpl(BitmapManager bitmapManager, PhotoMeasurementManager measurementManager) {
        this.bitmapManager = bitmapManager;
        this.measurementManager = measurementManager;
    }

    @Override
    public void showImage(Activity activity, int ID, String photoPath) {
        ImageView imageView = (ImageView) activity.findViewById(ID);

        //Get object original position
        ExifInterface exif;
        Matrix matrix = new Matrix();

        try {
            exif = new ExifInterface(photoPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);

            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), "[Error] Unable to rotate Image", Toast.LENGTH_SHORT).show();

            return;
        }

        // Reduce the image size by 1:8 to avoid out of memory issues
        int properInSampleSize = getProperImageInSample(activity, photoPath);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = properInSampleSize;

        Log.i(MainActivity.class.toString(), "Optimum inSample = " + properInSampleSize);

        Bitmap src = BitmapFactory.decodeFile(photoPath, opts);

        if (src == null) {
            return;
        }

        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

        int width = src.getWidth();
        int height = src.getHeight();
        int maximumHeight = measurementManager.getProperMaximumHeight(activity);

        if (height > maximumHeight) {
            double newWidth = ((double) src.getWidth() / src.getHeight()) * maximumHeight;

            width = (int) newWidth;
            height = maximumHeight;
        }

        Log.i(TAG, "Reduced Width = " + width);
        Log.i(TAG, "Reduced Height = " + height);

        imageView.setImageBitmap(Bitmap.createScaledBitmap(src, width, height, false));
    }

    @Override
    public void showImage(Activity activity, int ID, Bitmap src) {
        ImageView imageView = (ImageView) activity.findViewById(ID);

        imageView.setImageBitmap(Bitmap.createScaledBitmap(src, src.getWidth(), src.getHeight(), false));
    }

    @Override
    public int getProperImageInSample(Activity activity, String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(photoPath, options);

        int width = options.outWidth;
        int height = options.outHeight;
        int maximumHeight = measurementManager.getProperMaximumHeight(activity);

        Log.i(TAG, "Original Width = " + width);
        Log.i(TAG, "Original Height = " + height);

        if (height > maximumHeight) {
            double ratio = (double) height / maximumHeight;

            return (int) ratio;
        }

        return 1;
    }

    @Override
    public void saveViewAsPapyrusImage(final Activity activity, final int scrollableViewID,
                                       String path, String outFileName) throws Exception {

        File containerDir = new File(path);

        if (!containerDir.exists()) {
            containerDir.mkdirs();
        }

        File file = new File(path + outFileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        final FileOutputStream ostream = new FileOutputStream(file);

        Bitmap bitmap = bitmapManager.getBitmapFromScrollableView(activity,
                (ScrollView) activity.findViewById(scrollableViewID));

        bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);

        ostream.flush();
        ostream.close();
    }

    @SuppressWarnings("deprecation")
    public void setViewAsWallpaper(Activity activity, int scrollableViewID) throws Exception {
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(activity.getApplicationContext());

        // Get device screen height and width
        Display display = activity.getWindowManager().getDefaultDisplay();

        int fullWidth = display.getWidth();
        int fullHeight = display.getHeight();

        Log.i(TAG, "Width = " + fullWidth);
        Log.i(TAG, "Height = " + fullHeight);

        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmapManager.getBitmapFromScrollableView(activity,
                (ScrollView) activity.findViewById(scrollableViewID)),
                fullWidth, fullHeight, false);

        myWallpaperManager.suggestDesiredDimensions(bitmapResized.getWidth(), bitmapResized.getHeight());
        myWallpaperManager.setBitmap(bitmapResized);
    }
}
