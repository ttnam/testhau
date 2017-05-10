package io.yostajsc.izigo.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PageType.NOTIFICATION})
public @interface PageType {
    int NOTIFICATION = 2;
}
