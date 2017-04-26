package io.yostajsc.usecase.backend.sdk;

import io.yostajsc.constants.TripTypePermission;
import io.yostajsc.usecase.backend.core.IzigoApiManager;
import io.yostajsc.usecase.backend.interfaces.IGCallback;

/**
 * Created by nphau on 4/26/17.
 */

public class IzigoSdkExecutor {

    public static class TripExecutor {

        public static void publishTrip(String tripId, boolean isPublish, IGCallback<Void, String> callback) {
            IzigoApiManager.connect().updateTripInfo(tripId, isPublish ? "1" : "0", TripTypePermission.STATUS, callback);
        }

        public static void changeCover(String tripId, String url, IGCallback<Void, String> callback) {
            IzigoApiManager.connect().updateTripInfo(tripId, url, TripTypePermission.COVER, callback);
        }

        public static void changeName(String tripId, String name, IGCallback<Void, String> callback) {
            IzigoApiManager.connect().updateTripInfo(tripId, name, TripTypePermission.NAME, callback);
        }

        public static void increaseTripView(String tripId) {
            IzigoApiManager.connect().apiTrackingTripView(tripId);
        }
    }

}
