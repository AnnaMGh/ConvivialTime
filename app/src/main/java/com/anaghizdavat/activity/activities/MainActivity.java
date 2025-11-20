package com.anaghizdavat.activity.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.anaghizdavat.activity.AnalyticsHandler;
import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.fragments.GameFragment;
import com.anaghizdavat.activity.fragments.GameSetupFragment;
import com.anaghizdavat.activity.model.Team;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.anaghizdavat.activity.others.KeyboardUtils;
import com.anaghizdavat.activity.others.StatusBarUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MAIN_A";

    RelativeLayout rlRootMain;
    LinearLayout llLanguage;

    TextView txtEn;

    TextView txtRo;

    LinearLayout llParentAd;

    //set from other class
    public boolean showAlert = true;
    public int previousTime = -1;
    public int previousMode = -1;
    public ArrayList<Team> previousTeams = null;

    private long lastBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make status bar icons visible
        Window window = getWindow();
        StatusBarUtil.makeStatusBarOpaque(window);
        StatusBarUtil.changeStatusBarColor(this, window);

        androidx.core.view.WindowInsetsControllerCompat controller =
                androidx.core.view.WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(false); // set true if your bar is light

        rlRootMain = findViewById(R.id.rl_root_main);
        txtEn = findViewById(R.id.txt_en);
        txtRo = findViewById(R.id.txt_ro);
        llParentAd = findViewById(R.id.ll_parent_ad);
        llLanguage = findViewById(R.id.ll_language);

        AnalyticsHandler.enableCrashlytics(MainActivity.this);
        AnalyticsHandler.registerAnalytics(MainActivity.this);
        GlobalSingleton.init(MainActivity.this);

        initialize();
        setListeners();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnalyticsHandler.unregisterAnalytics();
    }

    public void showLanguageButtons(boolean isShow) {
        llLanguage.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void handleBackPressed() {
        Log.d(TAG, "handleBackPressed");

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.container);
            if (fragment instanceof GameFragment) {
                if (!showAlert) {
                    showAlert = true;
                    addFragmentAsNew(R.id.container, "GameFragment", new GameSetupFragment(), "GameSetupFragment");
                    return;
                }
                showAlert(getString(R.string.do_you_want_to_cancel_the_game),
                        obj -> {
                            addFragmentAsNew(R.id.container, "GameFragment", new GameSetupFragment(), "GameSetupFragment");
                        },
                        obj -> {
                        }
                );
            } else {
                Log.d(TAG, "handleBackPressed: Finish");
                finish();
            }
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            //below code was needed when i was adding, now i am replacing
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.container);
            if (fragment instanceof GameFragment) {
                Log.e(TAG, "handleBackPressed: GameFragment>1");
                if (!showAlert) {
                    showAlert = true;
                    finish();
                    return;
                }
                showAlert(getString(R.string.do_you_want_to_cancel_the_game),
                        obj -> super.onBackPressed(),
                        obj -> {
                        }
                );
            } else {
                Log.d(TAG, "handleBackPressed: Just finish.");
                finish();
            }
        } else {
            Log.d(TAG, "handleBackPressed: Finish from start.");
            finish();
        }
    }

    private void initialize() {
        //ads on gms
//        if (BuildConfig.FLAVOR.equals("gms")) {
//            AdHandler.initialize(MainActivity.this, adBanner, llParentAd);
//            AdHandler.initialize(MainActivity.this, null, null);
//        }

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, MainActivity.this));
        AnalyticsHandler.sendMessage(MainActivity.this, Constants.EVENT_LAUNCH, bundle);

        changeLanguageUI(GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, this));
        addFragment(R.id.container, new GameSetupFragment(), "GameSetupFragment");
    }

    private void setListeners() {
        txtEn.setOnClickListener(view -> {
            if (GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, this).equals("en")) {
                return;
            }
            KeyboardUtils.hideKeyboard(this, this.getCurrentFocus());
            showAlert(getString(R.string.sure_change_language), obj -> changeDeviceLanguage("en"), null);
        });
        txtRo.setOnClickListener(view -> {
            if (GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, this).equals("ro")) {
                return;
            }
            KeyboardUtils.hideKeyboard(this, this.getCurrentFocus());
            showAlert(getString(R.string.sure_change_language), obj -> changeDeviceLanguage("ro"), null);
        });
    }

    private void changeLanguageUI(String lang) {
        if (lang.equals("ro")) {
            txtEn.setTextColor(getResources().getColor(R.color.colorGray));
            txtRo.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            txtEn.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtRo.setTextColor(getResources().getColor(R.color.colorGray));
        }
    }

    public RelativeLayout getRootMainLayout() {
        return rlRootMain;
    }
}
