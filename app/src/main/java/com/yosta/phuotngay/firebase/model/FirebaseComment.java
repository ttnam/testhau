package com.yosta.phuotngay.firebase.model;

import com.yosta.phuotngay.helpers.AppHelper;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/16/2016.
 */

public class FirebaseComment implements Serializable {

    private long commentId;
    private String avatar;
    private String userid;
    private String username;
    private String content;
    private long createdtime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public long getCreatedtime() {
        return createdtime;
    }

    public long time() {
        return AppHelper.builder().getTimeStep(createdtime);
    }

    public void setCreatedtime(long createdtime) {
        this.createdtime = createdtime;
    }

}
