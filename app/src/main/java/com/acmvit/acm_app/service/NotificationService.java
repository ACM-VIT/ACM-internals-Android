package com.acmvit.acm_app.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.repository.UserRepository;
import com.acmvit.acm_app.ui.MainActivity;
import com.acmvit.acm_app.util.Constants;
import com.acmvit.acm_app.util.GeneralUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    private UserRepository userRepository;

    public NotificationService() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        if (AcmApp.getSessionManager().getToken() != null) {
            //Send the token using WorkManager for Reliable sending
            userRepository.sendFCMTokenUsingWM(this);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data);

            String title = data.get(Constants.ProjectNotification.MSG_TITLE);
            String body = data.get(Constants.ProjectNotification.MSG_BODY);
            NotificationCompat.Builder builder = buildNotification(title, body);
            sendNotification(builder.build());
        }
    }

    private NotificationCompat.Builder buildNotification(
        String title,
        String body
    ) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        );

        return new NotificationCompat.Builder(
            this,
            Constants.ProjectNotification.CHANNEL_ID
        )
            .setContentIntent(resultPendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    private void sendNotification(Notification notification) {
        NotificationManager notificationManager = getSystemService(
            NotificationManager.class
        );
        if (
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
        ) {
            NotificationChannel channel = new NotificationChannel(
                Constants.ProjectNotification.CHANNEL_ID,
                Constants.ProjectNotification.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(Constants.ProjectNotification.CHANNEL_DISP);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(
            GeneralUtils.generateUniqueId(),
            notification
        );
    }
}
