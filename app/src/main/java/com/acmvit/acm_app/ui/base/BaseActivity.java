package com.acmvit.acm_app.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.acmvit.acm_app.util.Action;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseActivity extends AppCompatActivity {
    protected ActivityViewModel activityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        activityViewModel.getLoginState().observe(this, this::onLoginStateChanged);
        activityViewModel.getNetworkState().observe(this, this::onNetworkStateChanged);
        activityViewModel.getIsLoading().observe(this, this::onLoadingStateChanged);

        activityViewModel.getAction().observe(this, action -> {
            switch (action.getType()){
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
        });

    }

    public abstract void onLoginStateChanged(boolean isLoggedIn);
    public void onNetworkStateChanged(boolean isConnected) {}
    public void onLoadingStateChanged(boolean isLoading) {}
    public void showSnackBar(String msg){}

    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void createToast(String msg){
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
