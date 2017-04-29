package io.yostajsc.sdk.consts;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({IgError.NOT_AUTHORIZATION})
public @interface IgError {
    int NOT_AUTHORIZATION = 901;
}