package com.elegantsolutions.hieroglyphic.gift.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elegantsolutions.hieroglyphic.gift.R;
import com.elegantsolutions.hieroglyphic.gift.ads.helper.ActivityAdsHelper;
import com.elegantsolutions.hieroglyphic.gift.di.HieroApplication;
import com.elegantsolutions.hieroglyphic.gift.service.BitmapManager;
import com.elegantsolutions.hieroglyphic.gift.service.GalleryManager;
import com.elegantsolutions.hieroglyphic.gift.service.HieroManager;
import com.elegantsolutions.hieroglyphic.gift.service.ImageManager;
import com.elegantsolutions.hieroglyphic.gift.service.ProgressManager;
import com.elegantsolutions.hieroglyphic.gift.ui.helper.Actions;
import com.elegantsolutions.hieroglyphic.gift.ui.helper.ShareOptions;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;

public class BaseActivity extends ActionBarActivity {
    public static final int ANDROID_6 = 23;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String FONTS_FFF_TUSJ_TTF = "fonts/FFF_Tusj.ttf";

    private ActivityAdsHelper activityAdsHelper;
    private ProgressDialog dialog;

    @Inject
    ImageManager imageManager;

    @Inject
    BitmapManager bitmapManager;

    @Inject
    GalleryManager galleryManager;

    @Inject
    ProgressManager progressManager;

    @Inject
    HieroManager hieroManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inject dependencies
        ((HieroApplication) getApplication()).getAppComponent().inject(this);

        // Initialize Ads component
        activityAdsHelper = new ActivityAdsHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (activityAdsHelper != null) {
            activityAdsHelper.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (activityAdsHelper != null) {
            activityAdsHelper.onStop();
        }
    }

    @Override
    public void onPause() {
        if (activityAdsHelper != null) {
            activityAdsHelper.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activityAdsHelper != null) {
            activityAdsHelper.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (activityAdsHelper != null) {
            activityAdsHelper.onDestroy();
        }

        activityAdsHelper = null;

        super.onDestroy();
    }

    protected void setupAdsBanner(int containerID) {
        activityAdsHelper.setupBannerAds(containerID);
    }

    protected void setupInterstitialAds() {
        activityAdsHelper.setupInterstitialAds();
    }

    protected void displayInterstitialAd() {
        activityAdsHelper.displayInterstitialAd();
    }

    protected void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        Log.d(TAG, "In requestPermissions()");

        ActivityCompat.requestPermissions(activity, permissions, requestCode);

        Log.d(TAG, "requestPermissions() completes");
    }

    protected void showPhotoOptions(View view) {
        final CharSequence[] items = {getResources().getString(R.string.from_camera),
                getResources().getString(R.string.from_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.pick_photo));

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == Actions.Application.LOAD_IMAGE_FROM_GALLERY) {
                    pickPhotoFromGallery();
                } else if (item == Actions.Application.CAPTURE_IMAGE_FROM_CAMERA) {
                    pickPhotoFromCamera();
                }
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    protected void pickPhotoFromGallery() {
        Log.d(TAG, "pickPhotoFromGallery() default implementation");
    }

    protected void pickPhotoFromCamera() {
        Log.d(TAG, "pickPhotoFromCamera() default implementation");
    }

    protected String loadImage(Intent data) {
        String photoPath;
        Uri selectedImage = data.getData();

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        photoPath = cursor.getString(columnIndex);
        cursor.close();

        Log.i(TAG, "photoPath = " + photoPath);

        if (photoPath.startsWith("http") || photoPath.startsWith("https")) {
            Toast.makeText(this.getApplicationContext(),
                    R.string.remote_files_not_supported_message,
                    Toast.LENGTH_LONG).show();

            return null;
        }

        return photoPath;
    }

    protected void showImage(int ID, String photoPath) {
        imageManager.showImage(this, ID, photoPath);
    }

    protected void showImage(int ID, Bitmap bitmap) {
        imageManager.showImage(this, ID, bitmap);
    }

    protected Bitmap convertNameToHieroBitmap(String userName) {
        Bitmap completeHieroName;
        Bitmap[] wordBitmaps;
        List<int[]> userNameHiero = hieroManager.convertEnglishNameToHiero(userName);

        wordBitmaps = new Bitmap[userNameHiero.size()];

        for (int j = 0; j < userNameHiero.size(); ++j) {
            int[] word = userNameHiero.get(j);

            Bitmap[] bitmaps = new Bitmap[word.length];

            for (int i = 0; i < bitmaps.length; ++i) {
                bitmaps[i] = bitmapManager.loadBitmap(this, word[i]);
            }

            Bitmap wordBitmap = bitmapManager.augmentHorizontalBitmaps(bitmaps);
            wordBitmaps[j] = wordBitmap;
        }

        completeHieroName = bitmapManager.augmentVerticalBitmaps(wordBitmaps, 5);

        return completeHieroName;
    }

    protected void displayUserName(TextView userNameView, String userName) {
        Typeface type = Typeface.createFromAsset(getAssets(), FONTS_FFF_TUSJ_TTF);

        userNameView.setTypeface(type);
        userNameView.setText(userName);
    }

    protected void requestFullScreenWindow() {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void goToAppRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));

        if (myStartActivity(intent) == false) {

            //Market (Google play) app seems not installed, let's try to open a web-browser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));

            if (myStartActivity(intent) == false) {

                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(this, this.getString(R.string.error_no_google_play), Toast.LENGTH_LONG).show();
            }
        }
    }

    protected String getUniqueImgName(String userName) {
        String photoName = userName.split("\\s+")[0];

        return photoName + "." + System.currentTimeMillis() + ".png";
    }

    protected void showBusyIndicator() {
        dialog = progressManager.startProgressDialog(this);
    }

    protected void hideBusyIndicator() {
        if (dialog != null) {
            progressManager.endProgressDialog(dialog);
        }
    }

    private boolean myStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    protected void showLongMessage(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    protected void showShortMessage(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    protected Observable<String> saveViewAsImage(final String currentImageName, final String userName,
                                                 final Activity sourceActivity, final int scrollableViewID) {

        Observable<String> observable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String newImageName;

                if (currentImageName == null) {
                    newImageName = getUniqueImgName(userName);

                    imageManager.saveViewAsPapyrusImage(sourceActivity,
                            scrollableViewID,
                            galleryManager.getAppGalleryPath(), newImageName);
                } else {
                    newImageName = currentImageName;
                }

                return newImageName;
            }
        });

        return observable;
    }

    protected boolean showSharingErrorMessage(String type) {
        switch (type) {
            case ShareOptions.FACEBOOK:
                showLongMessage(getString(R.string.cannot_find_facebook_app));
                return true;
            case ShareOptions.GMAIL:
                showLongMessage(getString(R.string.cannot_find_gmail_app));
                return true;
            case ShareOptions.GPLUS:
                showLongMessage(getString(R.string.cannot_find_gplus_app));
                return true;
            case ShareOptions.TWITTER:
                showLongMessage(getString(R.string.cannot_find_twitter_app));
                return true;
        }
        return false;
    }

    protected void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void registerHideSoftKeyboardEvent(View view, final Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                registerHideSoftKeyboardEvent(innerView, activity);
            }
        }
    }
}
