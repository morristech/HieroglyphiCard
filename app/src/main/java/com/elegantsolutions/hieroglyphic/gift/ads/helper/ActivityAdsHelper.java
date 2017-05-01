package com.elegantsolutions.hieroglyphic.gift.ads.helper;

import android.app.Activity;
import android.widget.LinearLayout;

import com.elegantsolutions.hieroglyphic.gift.BuildConfig;
import com.elegantsolutions.hieroglyphic.gift.ads.interstitial.InterstitialAdsHandler;
import com.elegantsolutions.hieroglyphic.gift.ads.interstitial.InterstitialAdsListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ActivityAdsHelper {
    private final static String TAG = ActivityAdsHelper.class.getName();

    private Activity activity;
    private InterstitialAdsHandler interstitialAdsHandler;
    private AdView adView;

    public ActivityAdsHelper(Activity activity) {
        this.activity = activity;
    }

    public void setupBannerAds(int containerID) {
        // Normal Ads
        adView = new AdView(activity);

        // Ads configs
        adView.setAdUnitId(BuildConfig.ADS_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        // Attach Ad to ...
        LinearLayout layout = (LinearLayout) activity.findViewById(containerID);

        // Add the adView to it.
        layout.addView(adView, 0);

        // Initiate a generic request.
        AdRequest adRequest;

        if (BuildConfig.ENABLE_EMULATOR_TEST_ADS) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }

        adView.loadAd(adRequest);
    }

    public void setupInterstitialAds() {
        interstitialAdsHandler = new InterstitialAdsHandler(activity, this.interstitialAdsListener);
    }

    public void displayInterstitialAd() {
        if (interstitialAdsHandler != null) {
            interstitialAdsHandler.displayInterstitial();
        }
    }

    private final InterstitialAdsListener interstitialAdsListener = new InterstitialAdsListener() {
        @Override
        public void onInterstitialAdsFinished() {
            //TODO add some impl if needed
        }
    };

    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
    }

    public void onResume() {
        if (adView != null) {
            adView.resume();
        }
    }

    public void onStart() {
        if (adView != null) {
            EasyTracker.getInstance(activity).activityStart(activity);  // Add this method.
        }
    }

    public void onStop() {
        if (adView != null) {
            EasyTracker.getInstance(activity).activityStop(activity);  // Add this method.
        }
    }

    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
    }
}
