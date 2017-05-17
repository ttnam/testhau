package io.yostajsc.sdk.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TripTypePermission.COVER, TripTypePermission.NAME, TripTypePermission.IS_PUBLISH,
        TripTypePermission.STATUS})
public @interface TripTypePermission {
    int COVER = 0;
    int NAME = 1;
    int IS_PUBLISH = 2;
    int STATUS = 3;
}
