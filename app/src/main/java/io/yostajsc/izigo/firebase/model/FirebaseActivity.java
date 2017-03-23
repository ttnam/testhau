package io.yostajsc.izigo.firebase.model;

import java.io.Serializable;

import io.yostajsc.core.utils.DatetimeUtils;

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
        return DatetimeUtils.getDate(time);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
