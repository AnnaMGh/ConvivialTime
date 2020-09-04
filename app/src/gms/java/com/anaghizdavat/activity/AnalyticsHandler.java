package com.anaghizdavat.activity;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsHandler {

    private static FirebaseAnalytics analyticsInstance;

    public static void registerAnalytics(Context context) {
        analyticsInstance = FirebaseAnalytics.getInstance(context);
    }

    public static void unregisterAnalytics() {
    }

    public static void sendMessage(Context context, String title, Bundle bundle) {
        if (analyticsInstance == null) {
            registerAnalytics(context);
        }
        analyticsInstance.logEvent(title, bundle);
    }
}
