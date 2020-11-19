package com.acmvit.acm_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.acmvit.acm_app.MainActivity;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.pref.BasePreferenceManager;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_TIMEOUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        BasePreferenceManager basePreferenceManager = new BasePreferenceManager(this);
        boolean isFirstTime = basePreferenceManager.getIsFirstTime();
        final Class<?> targetActivityClass = isFirstTime ? WelcomeActivity.class : MainActivity.class;

        Runnable startActivityRunnable = () -> {
            Intent intent = new Intent(SplashActivity.this, targetActivityClass);
            startActivity(intent);
            finish();
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(startActivityRunnable, SPLASH_TIMEOUT);
    }
}