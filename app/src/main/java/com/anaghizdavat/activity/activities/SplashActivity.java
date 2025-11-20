package com.anaghizdavat.activity.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.anaghizdavat.activity.AnalyticsHandler;
import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SPLASH_A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // From SDK 31 (12) it has its own Splash Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            goToMain();
            return;
        }

        setContentView(R.layout.activity_splash);
        AnalyticsHandler.enableCrashlytics(SplashActivity.this);
        AnalyticsHandler.registerAnalytics(SplashActivity.this);

        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnalyticsHandler.unregisterAnalytics();
    }

    private void initialize() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(this::goToMain, 1500);
        } catch (Exception e) {
            Log.e(TAG, "initialize: Exception " + e);
        }
    }

    private void goToMain() {
        // Send event
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, SplashActivity.this));
        AnalyticsHandler.sendMessage(SplashActivity.this, Constants.EVENT_LAUNCH, bundle);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }
}
