package com.acmvit.acm_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Connection;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.acmvit.acm_app.databinding.ActivityMainBinding;
import com.acmvit.acm_app.repository.AuthRepository;
import com.acmvit.acm_app.ui.MainViewModel;
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
    private MainViewModel viewModel;

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

        viewModel =
            new ViewModelProvider(this, new BaseViewModelFactory(this))
            .get(MainViewModel.class);
        setupOverflowMenu();
    }

    public void setupOverflowMenu() {
        Context wrapper = new ContextThemeWrapper(
            this,
            R.style.ThemeOverlay_popupTheme
        );
        PopupMenu popup = new PopupMenu(wrapper, binding.overflowMenu);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(
            menuItem -> {
                if (menuItem.getItemId() == R.id.menu_signout) {
                    viewModel.logout();
                    return true;
                }
                return false;
            }
        );

        binding.overflowMenu.setOnClickListener(
            view -> {
                popup.show();
            }
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
}
