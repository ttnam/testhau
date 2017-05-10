package io.yostajsc.izigo.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({NotificationType.REQUEST_JOIN_TRIP, NotificationType.MEMBER_ADD_TO_TRIP, NotificationType.ACCEPT_JOIN_TRIP})
public @interface NotificationType {
    int REQUEST_JOIN_TRIP = 0; // Admin
    int MEMBER_ADD_TO_TRIP = 1; // Admin
    int ACCEPT_JOIN_TRIP = 2;
}
