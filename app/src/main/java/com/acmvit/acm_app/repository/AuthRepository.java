package com.acmvit.acm_app.repository;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.util.Resource;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;

public class AuthRepository {
    private static AuthRepository instance;
    private static BackendService baseService;
    private static ServiceGenerator serviceGenerator;

    public static AuthRepository getInstance() {
        if(instance == null){
            instance = new AuthRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            baseService = serviceGenerator.createService(BackendService.class);
        }
        return instance;
    }

    public LiveData<Resource<UserData>> loginByGoogle(String idToken){
        String bearerToken = "Bearer " + idToken;
        MutableLiveData<Resource<UserData>> resource = new MutableLiveData<>();
        baseService.getAccessToken(bearerToken).enqueue(new BackendNetworkCall<>(resource));
        return resource;
    }

    //Synchronous refresh access call
    @WorkerThread
    public Response<BackendResponse<UserData>> refreshAccessToken(String accessToken, String refreshToken) throws IOException {
        String bearerAccessToken = "Bearer " + accessToken;
        HashMap<String, String> refresh = new HashMap<>();
        refresh.put("refreshToken", refreshToken);
        return baseService.refreshAccessToken(bearerAccessToken, refresh).execute();
    }
}
