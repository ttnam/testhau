package io.yostajsc.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({StatusType.BEFORE, StatusType.ONGOING, StatusType.FINISED})
public @interface StatusType {
    int BEFORE = 0;
    int ONGOING = 1;
    int FINISED = 2;
}
