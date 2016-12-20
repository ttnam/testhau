package com.yosta.phuotngay.firebase.model;

import com.yosta.phuotngay.helpers.app.DatetimeUtils;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/11/2016.
 */

public class FirebaseActivity implements Serializable {

    private String content;

    private long time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return DatetimeUtils.getTime(time, DatetimeUtils.DD_MM_YYYY);
    }
    public long getTime() {
        return time;
    }

    public String getHour() {
        return DatetimeUtils.getTime(time, DatetimeUtils.H_MM);
    }

    public void setTime(long time) {
        this.time = time;
    }
}
