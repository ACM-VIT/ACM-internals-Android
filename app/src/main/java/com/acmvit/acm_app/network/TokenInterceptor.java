package com.acmvit.acm_app.network;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.AuthToken;
import com.acmvit.acm_app.db.pref.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class TokenInterceptor implements Interceptor {

    private final SessionManager sessionManager = AcmApp.getSessionManager();

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        AuthToken authToken = sessionManager.getToken();
        Request original = chain.request();

        Request.Builder requestBuilder = original
            .newBuilder()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .header(
                "Authorization",
                AuthToken.TOKEN_TYPE + " " + authToken.getAccessToken()
            )
            .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
