package com.elegantsolutions.hieroglyphic.gift.ads.interstitial;

import android.app.Activity;
import android.util.Log;

import com.elegantsolutions.hieroglyphic.gift.BuildConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class InterstitialAdsHandler {
    private final static String TAG = InterstitialAdsHandler.class.getName();

    private InterstitialAd interstitialAdView;  // The ad
    private Runnable adDisplayer;               // Code to execute to perform this operation
    private Activity activity;
    private InterstitialAdsListener interstitialAdsListener;

    public InterstitialAdsHandler(Activity activity, InterstitialAdsListener interstitialAdsListener) {
        this.activity = activity;
        this.interstitialAdsListener = interstitialAdsListener;

        setupFullScreenAds(activity);
    }

    public void displayInterstitial() {
        if (adDisplayer != null) {
            adDisplayer.run();
        }
    }

    private void loadAd() {
        AdRequest adRequest;

        if (BuildConfig.ENABLE_EMULATOR_TEST_ADS) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }

        // Load the interstitialAdView object with the request
        interstitialAdView.loadAd(adRequest);
    }

    private AdListener adListener = new AdListener() {
        int numberOfTrials = 0;

        @Override
        public void onAdClosed() {
            interstitialAdsListener.onInterstitialAdsFinished();
            Log.i(TAG, "Trying to load Ad ...");
            loadAd(); // Need to reload the Ad when it is closed.
        }

        @Override
        public void onAdLoaded() {
            numberOfTrials = 0;
            Log.i(TAG, "Ad is officially loaded!");
        }

        @Override
        public void onAdFailedToLoad(int arg) {
            ++numberOfTrials;

            if (numberOfTrials > 5) {
                Log.i(TAG, "Cannot load ad because it could load after 5 trials!!!");
                return; // Do not try to reload ads ...
            }

            Log.i(TAG, "Ad could not be loaded. No retrying loading from another Vendor!");
            loadAd();
        }
    };

    private void setupFullScreenAds(final Activity activity) {
        interstitialAdView = new InterstitialAd(this.activity);
        interstitialAdView.setAdUnitId(BuildConfig.FS_ADS_UNIT_ID);
        interstitialAdView.setAdListener(adListener);
        adDisplayer = new Runnable() {
            public void run() {
                activity.runOnUiThread(adDisplayerRunnable);
            }
        };

        loadAd();
    }

    private Runnable adDisplayerRunnable = new Runnable() {
        public void run() {
            Log.i(TAG, "Before Ad is loaded and shown");
            if (interstitialAdView.isLoaded()) {
                Log.i(TAG, "Ad is loaded and shown!");
                interstitialAdView.show();
            } else {
                Log.i(TAG, "[Severe] Ad is NOT loaded. Forcing reloading Ad!!!");
                loadAd();
            }
        }
    };
}
