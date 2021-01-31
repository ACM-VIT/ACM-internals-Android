package com.acmvit.acm_app.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.model.UserList;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembersRepository {

    public static MembersRepository instance;
    public static ServiceGenerator serviceGenerator;
    public static BackendService service;
    public static MutableLiveData<ArrayList<User>> users = new MutableLiveData<>();

    public static MembersRepository getInstance() {
        if (instance == null) {
            instance = new MembersRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            service =
                serviceGenerator.createTokenizedService(BackendService.class);
            instance.fetchAllUsers();
        }
        return instance;
    }

    public void fetchAllUsers() {
        if (users.getValue() == null) {
            try {
                service
                    .getAllUsers()
                    .enqueue(
                        new Callback<BackendResponse<UserList>>() {
                            @Override
                            public void onResponse(
                                @NotNull Call<BackendResponse<UserList>> call,
                                @NotNull Response<BackendResponse<UserList>> response
                            ) {
                                assert response.body() != null;
                                users.setValue(
                                    response.body().getData().getUsers()
                                );
                            }

                            @Override
                            public void onFailure(
                                @NotNull Call<BackendResponse<UserList>> call,
                                @NotNull Throwable t
                            ) {}
                        }
                    );
            } catch (Exception e) {
                Log.e("Fetch data", e.toString());
            }
        }
    }
}
