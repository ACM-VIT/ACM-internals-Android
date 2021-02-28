package com.acmvit.acm_app.ui.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.acmvit.acm_app.ui.ActivityViewModel;
import com.acmvit.acm_app.util.GeneralUtils;

public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityViewModel activityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewModel =
            new ViewModelProvider(this).get(ActivityViewModel.class);
        activityViewModel
            .getLoginState()
            .observe(this, this::onLoginStateChanged);
        activityViewModel
            .getNetworkState()
            .observe(this, this::onNetworkStateChanged);
        activityViewModel
            .getIsLoading()
            .observe(this, this::onLoadingStateChanged);

        activityViewModel
            .getAction()
            .observe(
                this,
                action -> {
                    switch (action.getType()) {
                        case TOAST:
                            createToast(action.getMessage());
                            break;
                        case SNACKBAR:
                            showSnackBar(action.getMessage());
                            break;
                        case HIDE_KEYBOARD:
                            closeKeyboard();
                            break;
                        case CALLBACK:
                            action.getCallback().run(this);
                            break;
                    }
                }
            );
    }

    public abstract void onLoginStateChanged(boolean isLoggedIn);

    public void onNetworkStateChanged(boolean isConnected) {}

    public void onLoadingStateChanged(boolean isLoading) {}

    public void showSnackBar(String msg) {}

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        GeneralUtils.hideKeyboard(this, view);
    }

    public void createToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
