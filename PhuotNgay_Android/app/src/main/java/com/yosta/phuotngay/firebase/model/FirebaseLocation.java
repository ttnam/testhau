package com.yosta.phuotngay.firebase.model;

import com.yosta.phuotngay.helpers.app.DatetimeUtils;

/**
 * Created by Phuc-Hau Nguyen on 12/11/2016.
 */

public class FirebaseLocation {

    private String lat;
    private String lng;
    private String name;

    private long time;

    public String getTime() {
        return DatetimeUtils.getTime(DatetimeUtils.timeStampToDate(time));
    }

    public void setTime(long time) {
        this.time = time;
    }
}
