package com.yosta.phuotngay.models.app;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({MessageType.TAKE_PHOTO, MessageType.FROM_GALLERY,
        MessageType.LOAD_DONE, MessageType.LOST_INTERNET,
        MessageType.INTERNET_CONNECTED})
public @interface MessageType {
    int TAKE_PHOTO = 901;
    int FROM_GALLERY = 902;
    int LOAD_DONE = 903;
    int LOST_INTERNET = 904;
    int INTERNET_CONNECTED = 905;
}
