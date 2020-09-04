package com.anaghizdavat.activity;

import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

public class AnalyticsHandler {

    private static HiAnalyticsInstance analyticsInstance;

    public static void registerAnalytics(Context context) {
        HiAnalyticsTools.enableLog();
        analyticsInstance = HiAnalytics.getInstance(context);
        analyticsInstance.setAnalyticsEnabled(true);
        analyticsInstance.setAutoCollectionEnabled(true);
        analyticsInstance.regHmsSvcEvent();
    }

    public static void unregisterAnalytics() {
        analyticsInstance.unRegHmsSvcEvent();
    }

    public static void sendMessage(Context context, String title, Bundle bundle) {
        if (analyticsInstance == null) {
            registerAnalytics(context);
        }
        analyticsInstance.onEvent(title, bundle);
    }

}
