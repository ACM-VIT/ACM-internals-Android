package com.acmvit.acm_app.repository;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import io.reactivex.Single;

public class GeneralRepository {
    private static GeneralRepository instance;
    private static StorageReference storageReference;

    public static GeneralRepository getInstance() {
        if (instance == null) {
            instance = new GeneralRepository();
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return instance;
    }

    public Single<String> saveImageToCloud(String picUri, String location, String name) {
        return Single.create(emitter -> {
            StorageReference uploadRef = storageReference.child(location).child(name);
            uploadRef.putFile(Uri.parse(picUri)).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    if (task.getException() != null) {
                        emitter.onError(task.getException());
                    } else {
                        emitter.onError(new Throwable("Unable to upload the Image"));
                    }
                    return null;
                }
                return uploadRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Uri uri = task.getResult();
                if (!task.isSuccessful() || uri == null) {
                    if (task.getException() != null) {
                        emitter.onError(task.getException());
                    } else {
                        emitter.onError(new Throwable("Unable to upload the Image"));
                    }
                    return;
                }
                emitter.onSuccess(uri.toString());
            });
        });
    }

}


