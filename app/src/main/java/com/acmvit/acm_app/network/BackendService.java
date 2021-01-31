package com.acmvit.acm_app.network;

import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.model.UserList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BackendService {
    @POST("/App/v1/access/login/google")
    Call<BackendResponse<UserData>> getAccessToken(
        @Header("Authorization") String token
    );

    @POST("/App/v1/access/token/refresh")
    Call<BackendResponse<UserData>> refreshAccessToken(
        @Header("Authorization") String token,
        @Body HashMap<String, String> refreshToken
    );

    @PUT("/App/v1/user/update")
    Call<BackendResponse<UserData>> updateUser(
        @Body HashMap<String, String> changeBody
    );

    @POST("/App/v1/access/login/discord")
    Call<BackendResponse<UserData>> addDiscord(
        @Header("discord_token") String discordToken
    );

    @GET("/App/v1/user/fetch/byId/{id}")
    Call<BackendResponse<UserData>> fetchUserById(@Path("id") String id);

    @DELETE("/App/v1/access/logout")
    Call<BackendResponse<Void>> logout();

    @GET("/App/v1/user/fetch/all")
    Call<BackendResponse<UserList>> getAllUsers();
}
