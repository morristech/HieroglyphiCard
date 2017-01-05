package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by hazemsaleh on 1/5/17.
 */
public interface ShareManager {
    Intent getShareIntent(Activity activity, String type, String imagePath);
}
