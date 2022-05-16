package com.Scrip0.numble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartSplashScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start_splash);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
                public void run() {
                        startActivity(new Intent(StartSplashScreen.this, MainMenu.class));
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                        finish();
                }
        }, secondsDelayed * 1000);
    }
}