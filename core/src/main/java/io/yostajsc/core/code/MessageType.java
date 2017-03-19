package io.yostajsc.core.code;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({MessageType.TAKE_PHOTO, MessageType.FROM_GALLERY,
        MessageType.LOAD_DONE, MessageType.LOST_INTERNET,
        MessageType.INTERNET_CONNECTED, MessageType.ITEM_CLICK_INVITE,
        MessageType.ITEM_CLICK_INVITED, MessageType.USER_GPS,
        MessageType.PICK_LOCATION_FROM, MessageType.PICK_LOCATION_TO})
public @interface MessageType {
    int TAKE_PHOTO = 901;
    int FROM_GALLERY = 902;
    int LOAD_DONE = 903;
    int LOST_INTERNET = 904;
    int INTERNET_CONNECTED = 905;
    int ITEM_CLICK_INVITE = 906;
    int ITEM_CLICK_INVITED = 907;
    int USER_GPS = 908;
    int PICK_LOCATION_FROM = 909;
    int PICK_LOCATION_TO = 910;
}