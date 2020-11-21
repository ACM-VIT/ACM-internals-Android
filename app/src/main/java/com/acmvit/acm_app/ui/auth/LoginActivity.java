package com.acmvit.acm_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private AcmApp acmApp;
    private ActivityLoginBinding binding;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        acmApp = ((AcmApp) getApplicationContext());
        googleSignInClient = acmApp.getmGoogleSignInClient();

    }



}