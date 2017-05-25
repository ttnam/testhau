package io.yostajsc.izigo.usecase.service.firebase;

/**
 * Created by nphau on 4/26/17.
 */

public class FirebaseExecutor {

    public static class TripExecutor {

        public static void online(String tripId, String fbId, boolean isOnline) {
            FirebaseManager.inject().TRACK().child(tripId + "/" + fbId + "/isOnline")
                    .setValue(isOnline ? 1 : 0);
        }
    }
}
