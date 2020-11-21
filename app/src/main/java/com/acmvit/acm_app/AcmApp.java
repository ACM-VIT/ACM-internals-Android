package com.acmvit.acm_app;

import android.app.Application;

import com.acmvit.acm_app.pref.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AcmApp extends Application {
    private GoogleSignInClient mGoogleSignInClient;
    private static SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //Setup SessionManager
        sessionManager = new SessionManager(this);

        //Google SignIn Setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }
}
