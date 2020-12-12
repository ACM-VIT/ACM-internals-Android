package com.acmvit.acm_app.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.util.Resource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackendNetworkCall<T> implements Callback<BackendResponse<T>> {
    private static final String TAG = "NetworkCall";

    private final MutableLiveData<Resource<T>> resource;
    @Override
    public void onResponse(@NotNull Call<BackendResponse<T>> call, Response<BackendResponse<T>> response) {
        BackendResponse<T> backendResponse = response.body();

        if(response.isSuccessful()){
            resource.setValue(Resource.success(backendResponse.getData()));
            performIfSuccess(backendResponse.getData());

        }else {
            if(backendResponse == null) {
                resource.setValue(Resource.error(response.message(), null));
            }else{
                resource.setValue(Resource.error(backendResponse.getMessage(), null));
            }
            try {
                Log.e(TAG, response.message() + response.body() + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<BackendResponse<T>> call, Throwable t) {
        resource.setValue(Resource.error(t.toString(), null));
        Log.e(TAG, "onFailure: ",t);
    }

    public void performIfSuccess(T data){}

    public BackendNetworkCall(MutableLiveData<Resource<T>> resource) {
        this.resource = resource;
    }

}
