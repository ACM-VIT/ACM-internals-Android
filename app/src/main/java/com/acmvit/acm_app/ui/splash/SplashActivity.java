package com.acmvit.acm_app.ui.splash;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.pref.BasePreferenceManager;
import com.acmvit.acm_app.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashViewModel =
            new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
            )
                .get(SplashViewModel.class);

        BasePreferenceManager basePreferenceManager = new BasePreferenceManager(
            this
        );
        boolean isFirstTime = basePreferenceManager.getIsFirstTime();
        final Class<?> targetActivityClass = isFirstTime
            ? MainActivity.class
            : MainActivity.class;

        splashViewModel
            .getCanNavigate()
            .observe(
                this,
                canNav -> {
                    if (canNav) {
                        Intent intent = new Intent(
                            SplashActivity.this,
                            targetActivityClass
                        );
                        startActivity(intent);
                        finish();
                    }
                }
            );

        splashViewModel.fetchUserDetails();
        splashViewModel.startNavigationIntent();
    }
}
