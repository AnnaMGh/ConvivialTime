package com.anaghizdavat.activity.others;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.anaghizdavat.activity.activities.BaseActivity;
import com.anaghizdavat.activity.activities.MainActivity;
import com.anaghizdavat.activity.model.ObjectListener;
import com.anaghizdavat.activity.model.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalSingleton {

    private static GlobalSingleton mInstance;
    private static Context mContext;
    private SharedPreferences prefs;
    private static boolean isDeviceTablet;
    private DisplayMetrics displayMetrics;
    private int[] tutorialsStatus;


    String key = "3BBD147B911E44B4C03AFD9AA6AC8D0826068DFBB450854CFD7A11201366E50A";
    String secret = "dd9726bfbc3661d528ba965db7ece4b7561a8cadc68f37013a9a9a362219d2a0";
    String token = "";
    String clientId = "413589201460659200";
    String appId = "102606985";


    public static GlobalSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalSingleton();
        }
        return mInstance;
    }


    public static void init(Context ctx) {
        mContext = ctx;
    }

    //Shared preferences
    private final static String SHARED_PREFERENCES_KEY = "QUARANTINE";


    private boolean checkPreferences(Context context) {
        if (prefs == null && context != null)
            prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);

        if (context == null)
            return false;
        else
            return true;
    }


    public void setString(String key, String val, Context context) {
        checkPreferences(context);
        if (checkPreferences(context)) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(key, val);
            edit.apply();
        }

    }

    public String getString(String key, Context context) {
        if (checkPreferences(context)) {
            return prefs.getString(key, "");
        }
        return "";
    }

    public void setInteger(String key, int val, Context context) {
        checkPreferences(context);
        if (checkPreferences(context)) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt(key, val);
            edit.apply();
        }

    }

    public int getInteger(String key, Context context) {
        if (checkPreferences(context)) {
            return prefs.getInt(key, 0);
        }
        return 0;
    }

    public void setBoolean(String key, boolean val, Context context) {
        checkPreferences(context);
        if (checkPreferences(context)) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(key, val);
            edit.apply();
        }

    }

    public boolean getBoolean(String key, Context context) {
        if (checkPreferences(context)) {
            return prefs.getBoolean(key, false);
        }
        return false;
    }

    public void clearSharedPreferences(Context context) {
        checkPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear().apply();
    }

    public DisplayMetrics getScreenMetrics(Activity activity) {
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        return displayMetrics;
    }

    public void getViewSize(View view, ObjectListener listener) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (view.getWidth() > 0) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        Point point = new Point(view.getWidth(), view.getHeight());
                        if (listener != null) {
                            listener.getObject(point);
                        }
                    }
                }
            });
        }
    }


    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public float getScaleDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.scaledDensity;
    }

    public float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public float getCurrentDp(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }

    public float calculateStringWidth(TextView txt) {
        Rect bounds = new Rect();
        Paint textPaint = txt.getPaint();
        textPaint.getTextBounds(txt.getText().toString(), 0, txt.length(), bounds);
        int height = bounds.height();
        int width = bounds.width();

        return convertPixelsToDp(width, mContext);
    }

    public ArrayList<Team> copyArray(ArrayList<Team> toCopy) {
        ArrayList<Team> copied = new ArrayList<>();
        for (Team team : toCopy) {
            copied.add(new Team(team));
        }
        return copied;
    }

    public boolean hasInternet(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = (connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isTablet(Context context) {
        if (!isDeviceTablet) {
            isDeviceTablet = ((BaseActivity) context).isTablet();
        }
        return isDeviceTablet;
    }

    public boolean isTutorialDone(int tutorialId) {
        if (tutorialsStatus == null) {
            getTutorials();
        }

        if (tutorialsStatus.length>0) {
            for (int status : tutorialsStatus) {
                if (status == tutorialId) {
                    return true;
                }
            }
        }


        return false;
    }

    public void addTutorial(int id) {
        if (tutorialsStatus == null) {
            getTutorials();
        }

        int[] tutorials = new int[tutorialsStatus.length + 1];
        System.arraycopy(tutorialsStatus, 0, tutorials, 0, tutorialsStatus.length);
        tutorials[tutorialsStatus.length] = id;
        tutorialsStatus = tutorials;

        StringBuilder tutorialString = new StringBuilder();
        for (int tutorial : tutorials) {
            tutorialString.append(",").append(tutorial);
        }
        if (tutorialString.length() > 0) {
            setString(Constants.KEY_TUTORIAL, tutorialString.substring(1), mContext);
        }
    }

    private void getTutorials() {
        String tutorials = getString(Constants.KEY_TUTORIAL, mContext);

        if (tutorials.length() > 0) {
            String[] tutorialArray = tutorials.split(",");
            tutorialsStatus = new int[tutorialArray.length];
            for (int i = 0; i < tutorialArray.length; i++) {
                tutorialsStatus[i] = Integer.parseInt(tutorialArray[i]);
            }
        }else
        {
            tutorialsStatus = new int[0];
        }

    }
}
