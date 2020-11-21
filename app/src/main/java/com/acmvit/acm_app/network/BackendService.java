package com.acmvit.acm_app.network;


import com.acmvit.acm_app.model.AuthData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BackendService {
    //Get Google Access Token
    @POST("/App/v1/access/login/google")
    Call<BackendResponse<AuthData>> getAccessToken(@Header("Authorization") String token);

    @POST("/App/v1/access/token/refresh")
    Call<BackendResponse<AuthData>> refreshAccessToken(@Header("Authorization") String token,
                                                       @Body HashMap<String, String> refreshToken);
}
