package com.anaghizdavat.activity.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.model.ObjectListener;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.anaghizdavat.activity.others.LocaleHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends FragmentActivity {

    //variables
    View loadingView = null;
    View alertView = null;
    View rlBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        rlBase = findViewById(R.id.rl_base);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(LocaleHelper.onAttach(newBase, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, newBase))));
    }


    public void updateStatusBarColor(int color) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void addFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .commit();
    }

    public void addFragmentAsNew(int containerViewId, String lastFragmentTag, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .commit();
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String fragmentTag, String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private View getAlertView() {
        if (alertView == null)
            alertView = View.inflate(this, R.layout.alert, null);

        return alertView;
    }

    public void showAlert(String message, ObjectListener listenerYes, ObjectListener listenerNo) {
        ViewGroup v = findViewById(android.R.id.content);
        if (getAlertView().getParent() == null) {
            v.addView(getAlertView());
        }

        //set data
        getAlertView().setOnClickListener(view -> {
            //block listeners
        });

        getAlertView().findViewById(R.id.txt_yes).setOnClickListener(view -> {
            hideAlert();
            if (listenerYes != null) {
                listenerYes.getObject(null);
            }
        });

        getAlertView().findViewById(R.id.txt_no).setOnClickListener(view -> {
            hideAlert();
            if (listenerNo != null) {
                listenerNo.getObject(null);
            }
        });

        if (message != null) {
            ((TextView) getAlertView().findViewById(R.id.txt_message)).setText(message);
        }

        getAlertView().setVisibility(View.VISIBLE);
    }

    public void hideAlert() {
        getAlertView().setVisibility(View.GONE);
    }

    private View getLoadingView() {
        if (loadingView == null)
            loadingView = View.inflate(this, R.layout.loading, null);

        return loadingView;
    }

    public void showLoading() {
        ViewGroup v = findViewById(android.R.id.content);
        if (getLoadingView().getParent() == null)
            v.addView(getLoadingView());

        getLoadingView().setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        getLoadingView().setVisibility(View.GONE);
    }

    public void changeDeviceLanguage(String languageCode) {
        //show loading
        showLoading();

        //set new language
        LocaleHelper.setLocale(this, languageCode);

        try {
            //restart activity
            Intent i = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
            if (i != null) {
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isTablet(){
        return (rlBase.getTag()!=null && rlBase.getTag().equals(getString(R.string.tablet)));
    }

}
