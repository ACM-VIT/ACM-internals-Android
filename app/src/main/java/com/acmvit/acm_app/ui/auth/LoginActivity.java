package com.acmvit.acm_app.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.ActivityLoginBinding;
import com.acmvit.acm_app.pref.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private AcmApp acmApp;
    private ActivityLoginBinding binding;
    private GoogleSignInClient googleSignInClient;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);

        acmApp = ((AcmApp) getApplicationContext());
        googleSignInClient = acmApp.getmGoogleSignInClient();
        sessionManager = AcmApp.getSessionManager();
    }



}