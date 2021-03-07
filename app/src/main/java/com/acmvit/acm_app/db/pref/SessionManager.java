package com.acmvit.acm_app.db.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.acmvit.acm_app.model.AuthToken;
import com.acmvit.acm_app.model.User;
import com.google.gson.Gson;

public class SessionManager {

    private static final String TAG = "SessionManager";

    private final Gson gson = new Gson();
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserSession";
    private static final String AUTH_TOKEN = "AuthToken";
    private static final String USER = "User";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final MutableLiveData<Boolean> authStateNotifier = new MutableLiveData<>();

    @SuppressLint("CommitPrefEdits")
    @MainThread
    public SessionManager(Context context) {
        Log.d(TAG, "SessionManager:");
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        authStateNotifier.setValue(getAuthState());
        editor = pref.edit();
    }

    public LiveData<Boolean> getAuthStateNotifier() {
        return authStateNotifier;
    }

    public void addUserDetails(User user) {
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.commit();
        if (user == null) {
            addToken(null);
        }
    }

    public void addToken(AuthToken token) {
        String json = gson.toJson(token);
        editor.putString(AUTH_TOKEN, json);
        editor.commit();
        authStateNotifier.postValue(token != null && !token.isNull());
    }

    public User getUserDetails() {
        String json = pref.getString(USER, "");
        User user = gson.fromJson(json, User.class);
        if (user == null) {
            addToken(null);
        }
        return user;
    }

    public AuthToken getToken() {
        AuthToken token = getParsedAuthToken();
        if (token == null) {
            authStateNotifier.postValue(false);
        }
        return token;
    }

    private AuthToken getParsedAuthToken() {
        String json = pref.getString(AUTH_TOKEN, "");
        return gson.fromJson(json, AuthToken.class);
    }

    public boolean getAuthState() {
        AuthToken authToken = getParsedAuthToken();
        return authToken != null && !authToken.isNull();
    }

    public void truncateSession() {
        editor.clear();
        editor.commit();
        authStateNotifier.postValue(false);
    }
}
