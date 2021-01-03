package com.acmvit.acm_app.work;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.acmvit.acm_app.repository.UserRepository;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class SendFCMTokenWork extends Worker {

    public static final String TOKEN_KEY = "FCM_TOKEN";
    private final UserRepository userRepository;

    public SendFCMTokenWork(
        @NonNull Context context,
        @NonNull WorkerParameters params
    ) {
        super(context, params);
        userRepository = UserRepository.getInstance();
    }

    @NotNull
    @Override
    public Result doWork() {
        String fcmToken = getInputData().getString(TOKEN_KEY);
        boolean status = false;
        try {
            status = userRepository.sendFCMToken(fcmToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status ? Result.success() : Result.retry();
    }

    public static void sendTokenUsingWM(Context context, String token) {
        Data data = new Data.Builder()
            .putString(SendFCMTokenWork.TOKEN_KEY, token)
            .build();

        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

        WorkRequest sendTokenWork = new OneTimeWorkRequest.Builder(
            SendFCMTokenWork.class
        )
            .setConstraints(constraints)
            .setInputData(data)
            .build();

        WorkManager.getInstance(context).enqueue(sendTokenWork);
    }
}
