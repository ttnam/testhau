package com.yosta.phuotngay.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class RequestResponse implements Serializable {

    @SerializedName(value = "success")
    private boolean mSuccess = false;

    public RequestResponse(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public boolean IsSuccess() {
        return mSuccess;
    }
}
