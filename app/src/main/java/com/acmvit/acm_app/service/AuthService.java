package com.acmvit.acm_app.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.acmvit.acm_app.R;
import com.acmvit.acm_app.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

public class AuthService {
    private AuthorizationService authService;
    private AuthorizationRequest.Builder builder;
    private static AuthService instance;

    public static AuthService getInstance() {
        if(instance == null){
            instance = new AuthService();
        }
        return instance;
    }

    private AuthService() {
    }

    public void configDiscord(Context context){
        AuthorizationServiceConfiguration authServiceConfig = new AuthorizationServiceConfiguration(
                Uri.parse(Constants.Discord.AUTH_URL),
                Uri.parse(Constants.Discord.TOKEN_URL));

        authService = new AuthorizationService(context);

        builder = new AuthorizationRequest.Builder(
                authServiceConfig,
                context.getString(R.string.discord_client_id),
                ResponseTypeValues.CODE,
                Uri.parse(Constants.Discord.REDIRECT_URL))
                .setPrompt(Constants.Discord.PROMPT)
                .setScopes(Constants.Discord.SCOPES);
    }

    public Intent getDiscordFlowIntent(Context context){
        if(authService == null){
            configDiscord(context);
        }
        return authService.getAuthorizationRequestIntent(builder.build());
    }

    public void getDiscordAccessTokenFromIntent(Context context,
                                                Intent data, TokenCallback tokenCallback){
        AuthorizationResponse authResponse = AuthorizationResponse.fromIntent(data);
        AuthorizationException authException = AuthorizationException.fromIntent(data);

        if(authException == null && authResponse !=null){
            String authCode = authResponse.authorizationCode;
            authService.performTokenRequest(
                    authResponse.createTokenExchangeRequest(), (resp, ex) -> {
                        tokenCallback.call(resp == null ? null : resp.accessToken);
                    });
        }
        tokenCallback.call(null);
    }


    public interface TokenCallback{
        void call(@Nullable String token);
    }

}
