package com.yosta.phuotngay.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Token implements Serializable {


    @SerializedName(value = "token")
    private String mToken;

    public Token(String mToken) {
        this.mToken = mToken;
    }



}
