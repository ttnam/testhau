package com.yosta.phuotngay.models.app;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({MessageType.TAKE_PHOTO, MessageType.FROM_GALLERY})
public @interface MessageType {
    int TAKE_PHOTO = 0;
    int FROM_GALLERY = 1;
}
