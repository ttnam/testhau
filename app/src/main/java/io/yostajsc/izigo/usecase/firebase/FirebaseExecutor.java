package io.yostajsc.izigo.usecase.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import io.yostajsc.core.interfaces.CallBackWith;

/**
 * Created by nphau on 4/26/17.
 */

public class FirebaseExecutor {

    public static class TripExecutor {

        public static void changeCover(String tripId, Uri uri, final CallBackWith<Uri> callBack) {
            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("images/covers/" + tripId)
                    .putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Uri webUri = task.getResult().getDownloadUrl();
                    callBack.run(webUri);
                }
            });
        }
    }
}
