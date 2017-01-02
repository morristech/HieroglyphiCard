package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.elegantsolutions.hieroglyphic.gift.R;

import java.io.File;
import java.util.List;

public class ShareManager {
    private static final ShareManager shareManager = new ShareManager();

    private ShareManager() {
    }

    public static ShareManager getInstance() {
        return shareManager;
    }

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

                    share.putExtra(Intent.EXTRA_SUBJECT,  activity.getString(R.string.hieroglyphic_name_share_message));
                    share.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.checkout_name_message));

                    File file = new File(imagePath);

                    System.out.println("getShareIntent() imagePath = " + imagePath);

                    // Retry to get the file for 4 times
                    /*
                    int retries = 0;

                    while (!file.exists()) {
                        try {
                            System.out.println("getShareIntent() imagePath retry = " + retries);
                            Thread.sleep(1000);
                            ++retries;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (retries > 30) {
                            return null;
                        }
                    }
                    */

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
