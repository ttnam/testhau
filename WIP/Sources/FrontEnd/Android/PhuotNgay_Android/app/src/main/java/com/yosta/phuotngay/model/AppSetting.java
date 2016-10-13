package com.yosta.phuotngay.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by HenryPhuc on 5/23/2016.
 */
public class AppSetting implements Serializable {

    protected boolean isSync;
    protected boolean isNotify;

    public AppSetting() {

    }

    public AppSetting(boolean isSync, boolean isNotify) {
        this.isSync = isSync;
        this.isNotify = isNotify;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
