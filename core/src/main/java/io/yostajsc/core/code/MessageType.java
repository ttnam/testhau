package io.yostajsc.core.code;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({MessageType.TAKE_PHOTO, MessageType.FROM_GALLERY,
        MessageType.LOAD_DONE, MessageType.INTERNET_NO_CONNECTED,
        MessageType.INTERNET_CONNECTED,
        MessageType.ITEM_CLICK_INVITED, MessageType.USER_GPS,
        MessageType.PICK_LOCATION_FROM, MessageType.PICK_LOCATION_TO,
        MessageType.GPS_ON, MessageType.GPS_OFF})
public @interface MessageType {
    int TAKE_PHOTO = 901;
    int FROM_GALLERY = 902;
    int LOAD_DONE = 903;
    int INTERNET_NO_CONNECTED = 904;
    int INTERNET_CONNECTED = 905;
    int ITEM_CLICK_INVITED = 907;
    int USER_GPS = 908;
    int PICK_LOCATION_FROM = 909;
    int PICK_LOCATION_TO = 910;
    int GPS_ON = 911;
    int GPS_OFF = 912;
}