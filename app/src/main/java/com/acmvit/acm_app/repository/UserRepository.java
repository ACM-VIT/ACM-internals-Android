package com.acmvit.acm_app.repository;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.work.SendFCMTokenWork;
import com.acmvit.acm_app.work.UserDetailsFetchWork;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.service.AuthService;
import com.acmvit.acm_app.util.Constants;
import com.acmvit.acm_app.util.Resource;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private static UserRepository instance;
    private static BackendService tokenizedService;
    private static ServiceGenerator serviceGenerator;
    private static AuthService authService;
    private static SessionManager sessionManager;
    private static StorageReference dpReference;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            tokenizedService = serviceGenerator.createTokenizedService(BackendService.class);
            authService = AuthService.getInstance();
            sessionManager = AcmApp.getSessionManager();
            dpReference = FirebaseStorage.getInstance()
                    .getReference(Constants.Backend.DP_STORAGE_LOC);
        }
        return instance;
    }

    public LiveData<Resource<UserData>> updateUser(String name, String disp) {
        MutableLiveData<Resource<UserData>> resource = new MutableLiveData<>();
        HashMap<String, String> uploadBody = new HashMap<>();

        uploadBody.put("full_name", name);
        uploadBody.put("description", disp);

        tokenizedService.updateUser(uploadBody)
                .enqueue(new BackendNetworkCall<UserData>(resource) {
                    @Override
                    public void performIfSuccess(UserData data) {
                        sessionManager.addUserDetails(data.getUser());
                    }
                });

        return resource;
    }

    public LiveData<Resource<UserData>> updateUser(String name, String disp, String dpUri) {
        MutableLiveData<Resource<UserData>> resource = new MutableLiveData<>();
        HashMap<String, String> uploadBody = new HashMap<>();
        String fileName = sessionManager.getUserDetails().getId() + ".jpg";
        StorageReference uploadRef = dpReference.child(fileName);

        UploadTask uploadTask = uploadRef.putFile(Uri.parse(dpUri));
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "updateUser: " + task.getException());
                resource.setValue(Resource.error("Unable to upload the picture", null));
                return null;
            }

            return uploadRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            Uri uri = task.getResult();
            if (!task.isSuccessful() || uri == null) {
                return;
            }
            uploadBody.put("full_name", name);
            uploadBody.put("description", disp);
            uploadBody.put("profilePic", uri.toString());

            tokenizedService.updateUser(uploadBody)
                    .enqueue(new BackendNetworkCall<UserData>(resource) {
                        @Override
                        public void performIfSuccess(UserData data) {
                            sessionManager.addUserDetails(data.getUser());
                        }
                    });
        });

        return resource;
    }


    public LiveData<Resource<UserData>> addDiscordUsingIntent(Context context, Intent data) {
        Log.d(TAG, "addDiscordUsingIntent: ");
        MutableLiveData<Resource<UserData>> authData = new MutableLiveData<>();
        authService.getDiscordAccessTokenFromIntent(context, data, token -> {
            if (token == null) {
                authData.setValue(Resource.error("Bad Access Token", null));
                return;
            }
            String bearerDiscordToken = "Bearer " + token;
            tokenizedService.addDiscord(bearerDiscordToken)
                    .enqueue(new BackendNetworkCall<UserData>(authData) {
                        @Override
                        public void performIfSuccess(UserData data) {
                            sessionManager.addUserDetails(data.getUser());
                        }
                    });
        });

        return authData;
    }

    public void sendFCMTokenUsingWM(Context context) {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
            SendFCMTokenWork.sendTokenUsingWM(context, s);
        });
    }

    public void fetchUserUsingWM(Context context) {
        UserDetailsFetchWork.fetchUserUsingWM(context);
    }

    public LiveData<Resource<UserData>> fetchUserDetails() {
        String id = sessionManager.getUserDetails().getId();
        MutableLiveData<Resource<UserData>> userData = new MutableLiveData<>();
        tokenizedService.fetchUserById(id).enqueue(new BackendNetworkCall<UserData>(userData) {
            @Override
            public void performIfSuccess(UserData data) {
                sessionManager.addUserDetails(data.getUser());
            }
        });
        return userData;
    }

    //Synchronous update of FCM token
    @WorkerThread
    public boolean sendFCMToken(String token) throws IOException {
        HashMap<String, String> updateBody = new HashMap<>();
        updateBody.put("fcm_token", token);
        Response<BackendResponse<UserData>> response =
                tokenizedService.updateUser(updateBody).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getStatusCode().equals(Constants.Backend.SUCCESS_STATUS);
        }
        return false;
    }

    @WorkerThread
    public boolean fetchUser() throws IOException {
        String id = sessionManager.getUserDetails().getId();
        Response<BackendResponse<UserData>> response =
                tokenizedService.fetchUserById(id).execute();
        BackendResponse<UserData> body = response.body();
        if (response.isSuccessful() && body != null) {
            sessionManager.addUserDetails(body.getData().getUser());
            return body.getStatusCode().equals(Constants.Backend.SUCCESS_STATUS);
        }
        return false;
    }

}
