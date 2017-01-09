package com.elegantsolutions.hieroglyphic.gift.service;

import android.app.Activity;
import android.content.Intent;

public interface ShareManager {
    Intent getShareIntent(Activity activity, String type, String imagePath);
}
