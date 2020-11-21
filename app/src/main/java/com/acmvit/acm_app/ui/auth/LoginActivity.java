package com.acmvit.acm_app.ui.auth;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.BaseViewModelFactory;
import com.acmvit.acm_app.MainActivity;
import com.acmvit.acm_app.databinding.ActivityLoginBinding;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private static final int RC_GOOGLE = 100;
    private AcmApp acmApp;
    private ActivityLoginBinding binding;
    private GoogleSignInClient googleSignInClient;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        acmApp = ((AcmApp) getApplicationContext());
        googleSignInClient = acmApp.getmGoogleSignInClient();
        loginViewModel = new ViewModelProvider(this, new BaseViewModelFactory(this))
            .get(LoginViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewmodel(loginViewModel);
        initObservers();
    }

    @Override
    public void onLoginStateChanged(boolean isLoggedIn) {
        if(isLoggedIn){
            //Navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initObservers() {
        loginViewModel.getState().observe(this, state -> {
            switch (state){
                case LOG_IN:
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_GOOGLE);
                    break;

                case ERROR:
                    googleSignInClient.signOut();
                    break;
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account == null){
                loginViewModel.setError();
                return;
            }
            loginViewModel.getAccessCode(account.getIdToken());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            loginViewModel.setError();
            Log.e(TAG, "handleSignInResult: " + e.getStatusCode(), e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

}