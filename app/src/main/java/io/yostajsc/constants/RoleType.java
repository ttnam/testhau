package io.yostajsc.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({RoleType.GUEST, RoleType.MEMBER, RoleType.ADMIN})
public @interface RoleType {
    int GUEST = 0;
    int MEMBER = 1;
    int ADMIN = 2;
}
