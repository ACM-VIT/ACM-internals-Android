package com.acmvit.acm_app.repository;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.util.Resource;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;

public class AuthRepository {
    private static AuthRepository instance;
    private static BackendService baseService;
    private static BackendService tokenizedService;
    private static ServiceGenerator serviceGenerator;
    private static SessionManager sessionManager;

    public static AuthRepository getInstance() {
        if(instance == null){
            instance = new AuthRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            baseService = serviceGenerator.createService(BackendService.class);
            tokenizedService = serviceGenerator.createTokenizedService(BackendService.class);
            sessionManager = AcmApp.getSessionManager();
        }
        return instance;
    }

    public LiveData<Resource<UserData>> loginByGoogle(String idToken){
        String bearerToken = "Bearer " + idToken;
        MutableLiveData<Resource<UserData>> resource = new MutableLiveData<>();
        baseService.getAccessToken(bearerToken).enqueue(new BackendNetworkCall<UserData>(resource){
            @Override
            public void performIfSuccess(UserData data) {
                sessionManager.addUserDetails(data.getUser());
                sessionManager.addToken(data.getToken());
            }
        });
        return resource;
    }

    public LiveData<Resource<Void>> logout(){
        MutableLiveData<Resource<Void>> resource = new MutableLiveData<>();
        tokenizedService.logout().enqueue(new BackendNetworkCall<Void>(resource){
            @Override
            public void performIfSuccess(Void data) {
                sessionManager.truncateSession();
            }
        });
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
