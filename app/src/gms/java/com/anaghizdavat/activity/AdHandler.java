package com.anaghizdavat.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.anaghizdavat.activity.model.ObjectListener;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdHandler {
    private static InterstitialAd mInterstitialAd;
    private static Context mContext;
    private static ObjectListener listener;

    public static void initialize(Context context) {
        mContext = context;
        Log.d("adHandler", "initialize");
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d("adHandler", "initialize - onInitializationComplete");
            }
        });
    }

    public static void addInterstitialAd(Context context, String unitId) {
        if (context != null) {
            mContext = context;
        }

        Log.d("adHandler", "addInterstitialAd");
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(unitId);

        //ad for children
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");

        AdRequest request = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .tagForChildDirectedTreatment(true)
                .build();
        mInterstitialAd.loadAd(request);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("adHandler", "addInterstitialAd - onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("adHandler", "addInterstitialAd - onAdFailedToLoad" + adError.toString());

                //send event
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, mContext));
                AnalyticsHandler.sendMessage(mContext, Constants.EVENT_AD_FAILED, bundle);

                //send data back
                if (listener != null) {
                    listener.getObject("Failed");
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d("adHandler", "addInterstitialAd - onAdOpened");

                //send event
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, mContext));
                AnalyticsHandler.sendMessage(mContext, Constants.EVENT_AD_OPENED, bundle);
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d("adHandler", "addInterstitialAd - onAdClicked");

                //send event
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, mContext));
                AnalyticsHandler.sendMessage(mContext, Constants.EVENT_AD_CLICKED, bundle);

                //send data back
                if (listener != null) {
                    listener.getObject("Clicked");
                }
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d("adHandler", "addInterstitialAd - onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                Log.d("adHandler", "addInterstitialAd - onAdClosed");

                //send event
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, mContext));
                AnalyticsHandler.sendMessage(mContext, Constants.EVENT_AD_CLOSED, bundle);

                if (listener != null) {
                    listener.getObject("Closed");
                }
            }
        });
    }

    public static void showInterstitialAd(Context context) {
        if (context != null) {
            mContext = context;
        }

        Log.d("adHandler", "showInterstitalAd");
        if (GlobalSingleton.getInstance().hasInternet(mContext) && mInterstitialAd.isLoaded()) {
            Log.d("adHandler", "ad loaded and showed");
            mInterstitialAd.show();
        }
    }

    public static void showInterstitialAd(Context context, ObjectListener objectListener) {
        listener = objectListener;

        if (context != null) {
            mContext = context;
        }

        Log.d("adHandler", "showInterstitalAd");
        if (GlobalSingleton.getInstance().hasInternet(mContext) && mInterstitialAd.isLoaded()) {
            Log.d("adHandler", "ad loaded and showed");
            mInterstitialAd.show();
        } else {
            if (listener != null) {
                listener.getObject("No internet");
            }
        }
    }
}
