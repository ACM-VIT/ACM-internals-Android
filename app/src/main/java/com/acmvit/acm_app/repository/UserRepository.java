package com.acmvit.acm_app.repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.service.AuthService;
import com.acmvit.acm_app.util.Constants;
import com.acmvit.acm_app.util.Resource;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private static BackendService tokenizedService;
    private static ServiceGenerator serviceGenerator;
    private static AuthService authService;

    public static UserRepository getInstance() {
        if(instance == null){
            instance = new UserRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            tokenizedService = serviceGenerator.createService(BackendService.class);
            authService = AuthService.getInstance();
        }
        return instance;
    }

    public LiveData<Resource<UserData>> addDiscord(String discordToken){
        String bearerDiscordToken = "Bearer " + discordToken;
        MutableLiveData<Resource<UserData>> resource = new MutableLiveData<>();
        tokenizedService.addDiscord(bearerDiscordToken)
                .enqueue(new BackendNetworkCall<>(resource));
        return resource;
    }

    public LiveData<Resource<UserData>> addDiscordUsingIntent(Context context, Intent data){
        MutableLiveData<Resource<UserData>> authData =
                new MutableLiveData<>();
        authService.getDiscordAccessTokenFromIntent(context, data, token -> {
            if(token == null){
                authData.setValue(Resource.error("Bad Access Token", null));
                return;
            }
            String bearerDiscordToken = "Bearer " + token;
            tokenizedService.addDiscord(bearerDiscordToken)
                    .enqueue(new BackendNetworkCall<>(authData));
        });

        return authData;
    }

    //Synchronous update of FCM token
    @WorkerThread
    public boolean sendFCMToken(String token) throws IOException {
        MutableLiveData<Resource<UserData>> resource = new MutableLiveData<>();
        HashMap<String, String> updateBody = new HashMap<>();
        updateBody.put("fcm_token", token);
        Response<BackendResponse<UserData>> response =
        tokenizedService.updateUser(updateBody).execute();
        if(response.isSuccessful() && response.body() != null){
            return response.body().getStatusCode().equals(Constants.Backend.SUCCESS_STATUS);
        }
        return false;
    }

}
