package com.acmvit.acm_app.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityViewModel activityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        activityViewModel.getLoginState().observe(this, this::onLoginStateChanged);
        activityViewModel.getNetworkState().observe(this, this::onNetworkStateChanged);
    }

    public abstract void onLoginStateChanged(boolean isLoggedIn);
    public abstract void onNetworkStateChanged(boolean isConnected);
}
