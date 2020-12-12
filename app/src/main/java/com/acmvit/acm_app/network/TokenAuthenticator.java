package com.acmvit.acm_app.network;

import android.util.Log;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.model.AuthToken;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.repository.AuthRepository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private static final String TAG = "TokenAuthenticator";
    private final SessionManager sessionManager;
    private final AuthRepository authRepository;

    public TokenAuthenticator() {
        sessionManager = AcmApp.getSessionManager();
        authRepository = AuthRepository.getInstance();
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) {
        AuthToken authToken = sessionManager.getToken();
        synchronized (this) {
            if (responseCount(response) >= 2) {
                // If both the original call and the call with refreshed token failed,
                // it will probably keep failing, so don't try again and logout
                sessionManager.truncateSession();
                Log.d(TAG, "authenticate: ");
                return null;
            }
            AuthToken authToken1 = sessionManager.getToken();
            if (!authToken.equals(authToken1)) {
                //Token Already refreshed
                return createNewRequestWithHeader(response, authToken1.getAccessToken());
            }

            // We need a new client, since we don't want to make another call using our client with access token
            try {
                retrofit2.Response<BackendResponse<UserData>> tokenResponse =
                        authRepository.refreshAccessToken(authToken.getAccessToken(), authToken.getRefreshToken());
                if (tokenResponse.code() == 200) {
                    AuthToken newToken = tokenResponse.body().getData().getToken();
                    sessionManager.addToken(newToken);

                    return createNewRequestWithHeader(response, newToken.getAccessToken());
                } else if(tokenResponse.code() == 401){
                    sessionManager.truncateSession();
                    return null;
                }
                else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }
    }

    private static Request createNewRequestWithHeader(Response response, String accessToken){
        return response.request().newBuilder()
                .header("Authorization", AuthToken.TOKEN_TYPE + " " + accessToken)
                .build();
    }
    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

}

