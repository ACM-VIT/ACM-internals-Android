package com.acmvit.acm_app.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.acmvit.acm_app.MainActivity;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.pref.BasePreferenceManager;
import com.acmvit.acm_app.ui.base.BaseActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private SplashViewModel splashViewModel;
    private static final int SPLASH_TIMEOUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(SplashViewModel.class);

        //Fetch User details
        splashViewModel.fetchUserDetails();

        BasePreferenceManager basePreferenceManager = new BasePreferenceManager(this);
        boolean isFirstTime = basePreferenceManager.getIsFirstTime();
        final Class<?> targetActivityClass = isFirstTime ? MainActivity.class : MainActivity.class;

        Runnable startActivityRunnable = () -> {
            Intent intent = new Intent(SplashActivity.this, targetActivityClass);
            startActivity(intent);
            finish();
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(startActivityRunnable, SPLASH_TIMEOUT);
    }
}