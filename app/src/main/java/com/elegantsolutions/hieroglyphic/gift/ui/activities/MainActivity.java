package com.elegantsolutions.hieroglyphic.gift.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elegantsolutions.hieroglyphic.gift.R;
import com.elegantsolutions.hieroglyphic.gift.di.HieroApplication;
import com.elegantsolutions.hieroglyphic.gift.service.HieroManager;
import com.elegantsolutions.hieroglyphic.gift.ui.helper.Actions;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.elegantsolutions.hieroglyphic.gift.R.id.userName;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String JPG_EXT = ".jpg";

    private String photoPath;

    @Inject
    HieroManager hieroManager;

    @BindView(R.id.createCard)
    Button createCard;

    @BindView(R.id.cardPhoto)
    ImageView cardPhoto;

    @BindView(userName)
    EditText userNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestFullScreenWindow();

        super.onCreate(savedInstanceState);

        //inject dependencies
        ((HieroApplication) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        galleryManager.createAppGalleryDirectory();

        setupAdvertisement();

        registerHideSoftKeyboardEvent(getWindow().getDecorView().getRootView(), this);
    }

    @OnClick(R.id.cardPhoto)
    public void onSelectCardPhoto() {
        MainActivity.this.showPhotoOptions(null);
    }

    @OnClick(R.id.createCard)
    public void onCreateCard() {
        String userName = userNameField.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(MainActivity.this, R.string.enter_name_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (! hieroManager.isValidEnglishName(userName)) {
            Toast.makeText(MainActivity.this, R.string.name_should_be_english_message, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, CardActivity.class);

        intent.putExtra(Actions.Param.USER_NAME, userName);

        if (photoPath != null) {
            intent.putExtra(Actions.Param.PHOTO_PATH, photoPath);
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    protected void pickPhotoFromGallery() {
        if(Build.VERSION.SDK_INT >= ANDROID_6) {
            requestPermissions(this, Actions.Permission.READ_WRITE_EXTERNAL_STORAGE, Actions.Request.EXTERNAL_STORAGE);
        } else {
            pickImage();
        }
    }

    @Override
    protected void pickPhotoFromCamera() {
        photoPath = galleryManager.getGalleryPath() + System.currentTimeMillis() + JPG_EXT;

        if(Build.VERSION.SDK_INT >= ANDROID_6) {
            requestPermissions(this, Actions.Permission.FULL_CAMERA_STORAGE, Actions.Request.FULL_CAMERA_STORAGE_ACCESS);
        } else {
            captureImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "In onRequestPermissionsResult() and request code = " + requestCode);

        switch (requestCode) {
            case Actions.Request.EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Storage Permission granted");
                    pickImage();
                } else {
                    Log.d(TAG, "Storage Permission denied");
                }
                return;
            }
            case Actions.Request.CAMERA_ACCESS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Camera Permission is granted");
                    captureImage();
                } else {
                    Log.d(TAG, "Camera Permission denied");
                }
                return;
            }
            case Actions.Request.FULL_CAMERA_STORAGE_ACCESS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Camera + Disk Permission is granted");
                    captureImage();
                } else {
                    Log.d(TAG, "Camera + Disk Permission denied");
                }
                return;
            }
        }
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }

        if (requestCode == Actions.Application.LOAD_IMAGE_FROM_GALLERY &&  data != null) {
            photoPath = loadImage(data);
        } else if (requestCode == Actions.Application.CAPTURE_IMAGE_FROM_CAMERA) {
            Log.d(MainActivity.class.toString(), "Photo is saved");
        }

        if (photoPath != null) {
            showImage(R.id.cardPhoto, photoPath);
        }
    }

    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		
		photoPath = savedInstanceState.getString(Actions.Param.PHOTO_PATH);
		
		if (photoPath != null) {
            showImage(R.id.cardPhoto, photoPath);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (photoPath != null) {
			outState.putString(Actions.Param.PHOTO_PATH, photoPath);
		}
	}

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, Actions.Application.LOAD_IMAGE_FROM_GALLERY);
    }

    private void captureImage() {
        File photoFile = new File(photoPath);

        try {
            photoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.toString(), e.getMessage());

            Toast.makeText(MainActivity.this, "An error happens while creating Photo file", Toast.LENGTH_SHORT).show();

            photoPath = null;

            return;
        }

        Uri outputFileUri = Uri.fromFile(photoFile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, Actions.Application.CAPTURE_IMAGE_FROM_CAMERA);
    }
}
