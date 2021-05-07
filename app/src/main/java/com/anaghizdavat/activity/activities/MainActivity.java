package com.anaghizdavat.activity.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.anaghizdavat.activity.AdHandler;
import com.anaghizdavat.activity.AnalyticsHandler;
import com.anaghizdavat.activity.BuildConfig;
import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.fragments.GameFragment;
import com.anaghizdavat.activity.fragments.GameSetupFragment;
import com.anaghizdavat.activity.model.Team;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.anaghizdavat.activity.others.KeyboardUtils;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rl_root_main)
    RelativeLayout rlRootMain;
    @BindView(R.id.txt_en)
    TextView txtEn;
    @BindView(R.id.txt_ro)
    TextView txtRo;
    @BindView(R.id.ll_parent_ad)
    LinearLayout llParentAd;
//    @BindView(R.id.ad_banner)
//    AdView adBanner;

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

        ButterKnife.bind(MainActivity.this);
        AnalyticsHandler.enableCrashlytics(MainActivity.this);
        AnalyticsHandler.registerAnalytics(MainActivity.this);
        GlobalSingleton.init(MainActivity.this);

        initialize();
        setListeners();

        //check tutorial status
       // GlobalSingleton.getInstance().setString(Constants.KEY_TUTORIAL, "", this); //TODO remove this
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnalyticsHandler.unregisterAnalytics();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.container);
            if (fragment instanceof GameFragment) {
                if (!showAlert) {
                    showAlert = true;
//                    super.onBackPressed();
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
                long DELTA = 700;
                if (System.currentTimeMillis() - lastBackPress < DELTA) {
                    this.finish();
                } else {
                    showToast(getString(R.string.t_press_back));
                    lastBackPress = System.currentTimeMillis();
                }
            }
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            //below code was needed when i was adding, now i am replacing
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.container);
            if (fragment instanceof GameFragment) {
                Log.e("backpressed", "GameFragment>1");
                if (!showAlert) {
                    showAlert = true;
                    super.onBackPressed();
                    return;
                }
                showAlert(getString(R.string.do_you_want_to_cancel_the_game),
                        obj -> super.onBackPressed(),
                        obj -> {
                        }
                );
            } else {
                super.onBackPressed();
            }
        }
    }

    private void initialize() {
        //ads on gms
        if (BuildConfig.FLAVOR.equals("gms")) {
//            AdHandler.initialize(MainActivity.this, adBanner, llParentAd);
            AdHandler.initialize(MainActivity.this, null, null);
        }

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

    public RelativeLayout getRootMainLayout()
    {
        return  rlRootMain;
    }
}
