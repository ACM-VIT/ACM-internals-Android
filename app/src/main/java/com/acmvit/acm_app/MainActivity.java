package com.acmvit.acm_app;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.acmvit.acm_app.databinding.ActivityMainBinding;
import com.acmvit.acm_app.ui.auth.LoginActivity;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.acmvit.acm_app.util.SingleLiveEvent;
import com.acmvit.acm_app.util.SingleTimeObserver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setViewmodel(activityViewModel);
        binding.setLifecycleOwner(this);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

    }

    @Override
    public void onLoginStateChanged(boolean isLoggedIn) {
        if (!isLoggedIn) {
            ((AcmApp)getApplicationContext()).getmGoogleSignInClient().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.bottomNavigationView, msg,
                BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.setAnchorView(binding.bottomNavigationView);
        snackbar.show();
    }
}