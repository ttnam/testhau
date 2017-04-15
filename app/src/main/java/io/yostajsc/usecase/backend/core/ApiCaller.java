package io.yostajsc.usecase.backend.core;

import io.yostajsc.core.utils.ValidateUtils;

/**
 * Created by nphau on 4/15/17.
 */

public class ApiCaller {

    public static void callApiUpdateTripView(String tripId) {
        try {
            if (ValidateUtils.canUse(tripId)) {
                APIManager.connect().apiTrackingTripView(tripId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
