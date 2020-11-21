package com.acmvit.acm_app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.acmvit.acm_app.repository.AuthRepository;
import com.acmvit.acm_app.repository.UserRepository;

import java.io.IOException;

public class SendFCMTokenWork extends Worker {
    public static final String TOKEN_KEY = "FCM_TOKEN";
    private final UserRepository userRepository;

   public SendFCMTokenWork(
       @NonNull Context context,
       @NonNull WorkerParameters params) {
       super(context, params);
       userRepository = UserRepository.getInstance();
   }

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
}