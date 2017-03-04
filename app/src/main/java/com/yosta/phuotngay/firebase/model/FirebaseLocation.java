package com.yosta.phuotngay.firebase.model;

import com.yosta.utils.AppUtils;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/11/2016.
 */

public class FirebaseLocation implements Serializable {

    private String lat;
    private String lng;
    private String name;

    private long time;

    public String getTime() {
        return AppUtils.builder().getTime(time, AppUtils.DD_MM_YYYY);
    }

    public void setTime(long time) {
        this.time = time;
    }
}
