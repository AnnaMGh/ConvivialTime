package com.anaghizdavat.activity.activities;

import android.content.Intent;
import android.os.Bundle;

import com.anaghizdavat.activity.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private long lastBackPress;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize();
    }

    @Override
    public void onBackPressed() {
        long DELTA = 700;
        if (System.currentTimeMillis() - lastBackPress < DELTA) {
            try{
                timer.cancel();
                timer.purge();
            }catch (Exception e){e.printStackTrace();}
            this.finish();
        } else {
            lastBackPress = System.currentTimeMillis();
        }
    }

    private void initialize(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                timer.cancel();
                timer.purge();

                runOnUiThread(() -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });

            }
        },3000,3000);

    }
}
