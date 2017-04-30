package io.yostajsc.sdk.model.trip;

/**
 * Created by nphau on 4/7/17.
 */

public interface TripModel {

    String getId();

    String getName();

    String getCoverUrl();

    int getNumberOfViews();

    long getDurationTimeInMillis();

}
