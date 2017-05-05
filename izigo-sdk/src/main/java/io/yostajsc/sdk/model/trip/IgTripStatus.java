package io.yostajsc.sdk.model.trip;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({IgTripStatus.ONGOING, IgTripStatus.FINISHED, IgTripStatus.PREPARED})
public @interface IgTripStatus {
    int PREPARED = 0;
    int ONGOING = 1;
    int FINISHED = 2;
}