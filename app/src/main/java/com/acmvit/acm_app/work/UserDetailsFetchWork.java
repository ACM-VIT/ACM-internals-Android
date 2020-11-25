package com.acmvit.acm_app.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.acmvit.acm_app.repository.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UserDetailsFetchWork extends Worker {
    private final UserRepository userRepository;

    public UserDetailsFetchWork(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        userRepository = UserRepository.getInstance();
    }

    @NotNull
    @Override
    public ListenableWorker.Result doWork() {
        boolean status = false;
        try {
            status = userRepository.fetchUser();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status ? ListenableWorker.Result.success() : ListenableWorker.Result.retry();
    }

    public static void fetchUserUsingWM(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        WorkRequest sendTokenWork =
                new OneTimeWorkRequest.Builder(SendFCMTokenWork.class)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(context).enqueue(sendTokenWork);
    }

}
