package io.yostajsc.izigo.firebase.model;

import java.io.Serializable;

import io.yostajsc.utils.AppUtils;

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
        return AppUtils.builder().getTime(time, AppUtils.DD_MM_YYYY);
    }

    public long getTime() {
        return time;
    }

    public String getHour() {
        return AppUtils.builder().getTime(time, AppUtils.H_MM);
    }

    public void setTime(long time) {
        this.time = time;
    }
}
