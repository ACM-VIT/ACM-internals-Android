package com.acmvit.acm_app.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.util.Resource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackendNetworkCall<T> implements Callback<BackendResponse<T>> {

    private static final String TAG = "NetworkCall";

    private final MutableLiveData<Resource<T>> resource;

    @Override
    public void onResponse(
            @NotNull Call<BackendResponse<T>> call,
            Response<BackendResponse<T>> response
    ) {
        BackendResponse<T> backendResponse = response.body();

        if (response.isSuccessful()) {
            resource.setValue(Resource.success(backendResponse.getData()));
            performIfSuccess(backendResponse.getData());
        } else {
            if (response.code() == 400 || response.code() == 404) {
                resource.setValue(Resource.noData());
                return;
            }
            if (backendResponse == null) {
                Gson gson = new Gson();
                try {
                    Log.e(TAG, response.message() + response.body() + response.errorBody().string());

                    Type type = new TypeToken<BackendResponse<T>>(){}.getType();
                    BackendResponse<T> errResponse = gson.fromJson(response.errorBody().string(), type);
                    resource.setValue(Resource.error(errResponse == null ? null : errResponse.getMessage(), null));
                } catch (IOException e) {
                    resource.setValue(Resource.error(response.message(), null));
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "onResponse: ");
                resource.setValue(Resource.error(backendResponse.getMessage(), null));
            }

        }
    }

    @Override
    public void onFailure(@NotNull Call<BackendResponse<T>> call, Throwable t) {
        resource.setValue(Resource.error(t.toString(), null));
        Log.e(TAG, "onFailure: ", t);
    }

    public void performIfSuccess(T data) {
    }

    public BackendNetworkCall(MutableLiveData<Resource<T>> resource) {
        this.resource = resource;
    }
}
