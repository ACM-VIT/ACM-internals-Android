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
    private SessionManager sessionManager;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);

        acmApp = ((AcmApp) getApplicationContext());
        googleSignInClient = acmApp.getmGoogleSignInClient();
        sessionManager = AcmApp.getSessionManager();
        loginViewModel = new ViewModelProvider(this, new BaseViewModelFactory(this))
            .get(LoginViewModel.class);
        initObservers();
    }

    @Override
    public void onLoginStateChanged(boolean isLoggedIn) {
        if(isLoggedIn){
            //Navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void initObservers() {
        loginViewModel.getState().observe(this, state -> {
            if(state == LoginViewModel.State.LOG_IN){
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account == null){
                loginViewModel.setError();
                return;
            }
            loginViewModel.getAccessCode();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
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