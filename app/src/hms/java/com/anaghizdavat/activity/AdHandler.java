package com.anaghizdavat.activity;

import com.anaghizdavat.activity.model.ObjectListener;
import android.content.Context;
import android.util.Log;
import android.view.View;


public class AdHandler {
    private static Context mContext;
    private static ObjectListener listener;

    public static void initialize(Context context, View adBanner, View llParent) {
        mContext = context;
        Log.d("adHandler", "initialize");

    }

    public static void addInterstitialAd(Context context, String unitId) {
        Log.d("adHandler", "addInterstitialAd");

    }

    public static void showInterstitialAd(Context context) {
        Log.d("adHandler", "showInterstitalAd");
    }

    public static void showInterstitialAd(Context context, ObjectListener objectListener) {
        Log.d("adHandler", "showInterstitalAd listener");
        listener = objectListener;
        if (listener != null) {
            listener.getObject("Closed");
        }
    }
}
