package com.elegantsolutions.hieroglyphic.gift.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.elegantsolutions.hieroglyphic.gift.R;
import com.elegantsolutions.hieroglyphic.gift.di.HieroApplication;
import com.elegantsolutions.hieroglyphic.gift.service.ShareManager;
import com.elegantsolutions.hieroglyphic.gift.ui.helper.Actions;
import com.elegantsolutions.hieroglyphic.gift.ui.helper.ShareOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardActivity extends BaseActivity {
    private static final String TAG = CardActivity.class.getSimpleName();

    private String userName;
    private String currentImageName;
    private int operationID;

    @Inject
    ShareManager shareManager;

    @BindView(R.id.userNameView)
    TextView userNameField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //inject dependencies
        ((HieroApplication) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        displayCardInformation();

        //TODO convert to snack
        showLongMessage(getString(R.string.procedure_message));
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.card, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.share_on_fb:
                if(Build.VERSION.SDK_INT >= ANDROID_6) {
                    requestPermissions(this, Actions.Permission.READ_WRITE_EXTERNAL_STORAGE, Actions.Request.EXTERNAL_STORAGE);
                    operationID = Actions.Application.SHARE_IMAGE_FB;
                } else {
                    shareImage(ShareOptions.FACEBOOK);
                }
	            return true;
	        case R.id.share_on_twitter:
                if(Build.VERSION.SDK_INT >= ANDROID_6) {
                    requestPermissions(this, Actions.Permission.READ_WRITE_EXTERNAL_STORAGE, Actions.Request.EXTERNAL_STORAGE);
                    operationID = Actions.Application.SHARE_IMAGE_TWITTER;
                } else {
                    shareImage(ShareOptions.TWITTER);
                }
	            return true;
	        case R.id.share_on_google_plus:
                if(Build.VERSION.SDK_INT >= ANDROID_6) {
                    requestPermissions(this, Actions.Permission.READ_WRITE_EXTERNAL_STORAGE, Actions.Request.EXTERNAL_STORAGE);
                    operationID = Actions.Application.SHARE_IMAGE_GPLUS;
                } else {
                    shareImage(ShareOptions.GPLUS);
                }
	            return true;
	        case R.id.send_to_friend:
                if(Build.VERSION.SDK_INT >= ANDROID_6) {
                    requestPermissions(this, Actions.Permission.READ_WRITE_EXTERNAL_STORAGE, Actions.Request.EXTERNAL_STORAGE);
                    operationID = Actions.Application.SHARE_IMAGE_EMAIL;
                } else {
                    shareImage(ShareOptions.GMAIL);
                }
	            return true;
	        //case R.id.set_as_wallpaper:
	        //	setViewAsWallpaper();
	        //	return true;
	        case R.id.save_in_gallery:
                if(Build.VERSION.SDK_INT >= ANDROID_6) {
                    requestPermissions(this, Actions.Permission.READ_WRITE_EXTERNAL_STORAGE, Actions.Request.EXTERNAL_STORAGE);
                    operationID = Actions.Application.SAVE_IMAGE_IN_GALLERY;
                } else {
                    saveInGallery();
                }
	    		return true;
	        case R.id.rate_App:
	        	goToAppRate();
	        	return true;
	    }
	    
	    return true;
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Actions.Request.EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Storage Permission granted");

                    switch (operationID) {
                        case Actions.Application.SHARE_IMAGE_FB:
                            shareImage(ShareOptions.FACEBOOK);
                            break;
                        case Actions.Application.SHARE_IMAGE_TWITTER:
                            shareImage(ShareOptions.TWITTER);
                            break;
                        case Actions.Application.SHARE_IMAGE_GPLUS:
                            shareImage(ShareOptions.GPLUS);
                            break;
                        case Actions.Application.SHARE_IMAGE_EMAIL:
                            shareImage(ShareOptions.GMAIL);
                            break;
                        case Actions.Application.SAVE_IMAGE_IN_GALLERY:
                            saveInGallery();
                            break;
                    }

                } else {
                    Log.d(TAG, "Storage Permission denied");
                }
                return;
            }
        }
    }
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		
		currentImageName = savedInstanceState.getString("currentImageName");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (currentImageName != null) {
			outState.putString("currentImageName", currentImageName);
		}
	}

    private void shareImage(final String type) {
        showBusyIndicator();

        Observable<String> observable = saveViewAsImage(currentImageName, userName, CardActivity.this, R.id.ScrlCardView);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideBusyIndicator();
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                        showLongMessage(getString(R.string.error_message));
                    }

                    @Override
                    public void onNext(String imageName) {
                        hideBusyIndicator();

                        Log.d(TAG, "Passed imageName = " + imageName);

                        // Store image name if updated ...
                        currentImageName = imageName;

                        // Lookup for apps to use for share
                        Intent intent = shareManager.getShareIntent(CardActivity.this,
                                type,
                                galleryManager.getAppGalleryPath() + currentImageName);

                        if (intent == null) {
                            showSharingErrorMessage(type);
                            return;
                        }

                        // Share image
                        startActivity(Intent.createChooser(intent, getString(R.string.share_image_message)));

                        // Show success message
                        showLongMessage(getString(R.string.done_successfully_message));
                    }
                });
	}

    private void saveInGallery() {
		if (currentImageName != null) {
			showShortMessage(getString(R.string.image_saved_app) +
                    galleryManager.getAppGalleryPath() + currentImageName);
			return;
		}

        showBusyIndicator();

        Observable<String> observable = saveViewAsImage(currentImageName, userName, CardActivity.this, R.id.ScrlCardView);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideBusyIndicator();
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                        showLongMessage(getString(R.string.cannot_save_card_error_message));
                    }

                    @Override
                    public void onNext(String imageName) {
                        hideBusyIndicator();

                        // Store image name
                        currentImageName = imageName;

                        showLongMessage(getString(R.string.image_saved_app) +
                                galleryManager.getAppGalleryPath() +
                                currentImageName);
                    }
                });
	}

    private void displayCardInformation() {
        Intent intent = getIntent();

        userName = intent.getStringExtra(Actions.Param.USER_NAME);
        String photoPath = intent.getStringExtra(Actions.Param.PHOTO_PATH);

        // Display user image
        if (photoPath != null) {
            imageManager.showImage(this, R.id.photoView, photoPath);
        }

        // Display Hero characters
        Bitmap completeHieroName = convertNameToHieroBitmap(userName);

        showImage(R.id.heroView, completeHieroName);

        // Display user name
        displayUserName(userNameField, userName);
    }
}