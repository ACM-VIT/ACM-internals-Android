package com.acmvit.acm_app;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.service.NetworkChangeReceiver;
import com.acmvit.acm_app.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class AcmApp extends Application {
    private GoogleSignInClient mGoogleSignInClient;
    private static SessionManager sessionManager;
    private static final MutableLiveData<Boolean> isConnected = new MutableLiveData<>(false);

    @Override
    public void onCreate() {
        super.onCreate();

        //Setup Firebase Notification
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ProjectNotification.TOPIC);

        //Setup SessionManager
        sessionManager = new SessionManager(this);

        //Google SignIn Setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Init Connection status
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            setIsConnected(networkInfo.isConnected());
        }else{
            setIsConnected(false);
        }
        registerReceiver(new NetworkChangeReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }

    public static void setIsConnected(boolean isConnected) {
        AcmApp.isConnected.setValue(isConnected);
    }

    public static LiveData<Boolean> getIsConnected() {
        return isConnected;
    }

    public static boolean getIsConnectedOneTime(){
        return isConnected.getValue();
    }

}
