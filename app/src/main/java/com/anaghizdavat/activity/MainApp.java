package com.anaghizdavat.activity;

import android.content.Context;
import android.content.res.Configuration;

import androidx.multidex.MultiDexApplication;

import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.anaghizdavat.activity.others.LocaleHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, base)));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(this, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, this));
    }

}
