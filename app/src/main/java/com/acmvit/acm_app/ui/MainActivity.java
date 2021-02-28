package com.acmvit.acm_app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.widget.PopupMenu;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.ActivityMainBinding;
import com.acmvit.acm_app.ui.auth.LoginActivity;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.acmvit.acm_app.ui.base.BaseViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        navHostFragment =
            (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(
            bottomNavigationView,
            navHostFragment.getNavController()
        );
    }

    @Override
    public void onLoginStateChanged(boolean isLoggedIn) {
        if (!isLoggedIn) {
            ((AcmApp) getApplicationContext()).getmGoogleSignInClient()
                .signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(
            binding.bottomNavigationView,
            msg,
            Snackbar.LENGTH_SHORT
        );
        snackbar.setAnchorView(binding.bottomNavigationView);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (activityViewModel.checkLocking()) {
            for (
                int i = 0;
                i < bottomNavigationView.getMenu().size() - 1;
                i++
            ) {
                bottomNavigationView.getMenu().getItem(i).setEnabled(false);
            }
        } else {
            for (
                int i = 0;
                i < bottomNavigationView.getMenu().size() - 1;
                i++
            ) {
                bottomNavigationView.getMenu().getItem(i).setEnabled(true);
            }
        }
    }
}
