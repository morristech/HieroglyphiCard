package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.elegantsolutions.hieroglyphic.gift.R;
import com.elegantsolutions.hieroglyphic.gift.service.ShareManager;

import java.io.File;
import java.util.List;

class ShareManagerImpl implements ShareManager {
    private static final String TAG = ShareManagerImpl.class.getSimpleName();

    @Override
    public Intent getShareIntent(Activity activity, String type, String imagePath) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);

        share.setType("image/png");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(share, 0);

        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {

                    share.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.hieroglyphic_name_share_message));
                    share.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.checkout_name_message));

                    File file = new File(imagePath);

                    Log.d(TAG, "getShareIntent() imagePath = " + imagePath);

                    Uri imageUri = Uri.fromFile(file); //Uri.parse("file://" + imagePath);

                    //set photo
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);

                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return null;
            }

            return share;
        }
        return null;
    }
}
