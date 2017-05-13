package io.yostajsc.izigo.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({NotificationType.REQUEST_JOIN_TRIP, NotificationType.MEMBER_ADD_TO_TRIP, NotificationType.ACCEPT_JOIN_TRIP,
        NotificationType.SYSTEM, NotificationType.ADMIN_TO_USER})
public @interface NotificationType {
    int REQUEST_JOIN_TRIP = 0; // có user xin vào trip
    int MEMBER_ADD_TO_TRIP = 1; // member add người mới vào trip
    int ACCEPT_JOIN_TRIP = 2; // (chưa phải member) thông báo cho thèn dc add, xác nhận được add vào group
    int ADMIN_TO_USER = 3; // Thông báo từ admin cho user : trong content có thêm 1 trường message
    int SYSTEM = 4; // Thông báo chung từ hệ thống

}
