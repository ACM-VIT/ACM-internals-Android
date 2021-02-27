package com.acmvit.acm_app.repository;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.db.AcmDb;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.model.UserList;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.util.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembersRepository {
    public static MembersRepository instance;
    public static ServiceGenerator serviceGenerator;
    public static BackendService service;
    public static MutableLiveData<ArrayList<User>> users = new MutableLiveData<>();
    private static AcmDb localDb;

    public static MembersRepository getInstance() {
        if (instance == null) {
            instance = new MembersRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            service = serviceGenerator.createTokenizedService(BackendService.class);
            localDb = AcmApp.getAcmDb();
            instance.fetchAllUsers();
        }
        return instance;
    }

    public void fetchAllUsers() {
        if (users.getValue() == null) {
            try {
                service.getAllUsers().enqueue(new Callback<BackendResponse<UserList>>() {
                    @Override
                    public void onResponse(@NotNull Call<BackendResponse<UserList>> call, @NotNull Response<BackendResponse<UserList>> response) {
                        assert response.body() != null;
                        users.setValue(response.body().getData().getUsers());
                    }

                    @Override
                    public void onFailure(@NotNull Call<BackendResponse<UserList>> call, @NotNull Throwable t) {

                    }
                });

            } catch (Exception e) {
                Log.e("Fetch data", e.toString());
            }
        }

    }


    public LiveData<Resource<List<User>>> getAllUsers() {
        return new NetworkBoundResource<UserList, List<User>>() {
            @Override
            protected Completable saveCallResult(UserList item) {
                return localDb.userDao().insertUsers(item.getUsers());
            }

            @Override
            protected LiveData<List<User>> loadFromDb() {
                return localDb.userDao().getAllUsers();
            }

            @Override
            protected void createCall(MutableLiveData<Resource<UserList>> reqItem) {
                service.getAllUsers().enqueue(new BackendNetworkCall<>(reqItem));
            }
        }.getAsLiveData();
    }

}
