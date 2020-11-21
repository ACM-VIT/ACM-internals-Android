package com.acmvit.acm_app.network;

import com.acmvit.acm_app.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static ServiceGenerator instance;
    private final Retrofit baseClient;
    private Retrofit backendClient;

    public static ServiceGenerator getInstance() {
        if(instance == null){
            instance = new ServiceGenerator();
        }
        return instance;
    }

    public ServiceGenerator() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Constants.Backend.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);

        baseClient = retrofitBuilder.client(clientBuilder.build()).build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return baseClient.create(serviceClass);
    }
}
