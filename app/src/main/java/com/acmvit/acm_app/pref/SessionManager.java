package com.acmvit.acm_app.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akribase.oauthloginimplementation.model.AuthToken;
import com.akribase.oauthloginimplementation.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static final String TAG = "SessionManager";

    private final Gson gson = new Gson();
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserSession";
    private static final String AUTH_TOKEN = "AuthToken";
    private static final String USER = "User";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final MutableLiveData<Boolean> authStateNotifier = new MutableLiveData<>();

    @SuppressLint("CommitPrefEdits")
    @MainThread
    public SessionManager(Context context){
        Log.d(TAG, "SessionManager:");
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        authStateNotifier.setValue(getAuthState());
        editor = pref.edit();
    }

    public LiveData<Boolean> getAuthStateNotifier() {
        return authStateNotifier;
    }

    public void addUserDetails(User user){
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.commit();
    }

    public void addToken(@NonNull AuthToken token){
        String json = gson.toJson(token);
        editor.putString(AUTH_TOKEN, json);
        editor.commit();
        authStateNotifier.postValue(!token.isNull());
    }

    public User getUserDetails(){
        String json = pref.getString(USER, "");
        return gson.fromJson(json, User.class);
    }

    public AuthToken getToken(){
        AuthToken token = getParsedAuthToken();
        if(token == null){
            authStateNotifier.postValue(false);
        }
        return token;
    }

    private AuthToken getParsedAuthToken(){
        String json = pref.getString(AUTH_TOKEN, "");
        return gson.fromJson(json, AuthToken.class);
    }

    public boolean getAuthState(){
        AuthToken authToken = getParsedAuthToken();
        return authToken != null && !authToken.isNull();
    }

    public void truncateSession(){
        editor.clear();
        editor.commit();
        authStateNotifier.postValue(false);
    }
}
