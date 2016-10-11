package com.yosta.phuotngay.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/10/2016.
 */

public class BaseUser implements Serializable {


    @SerializedName(value = "password")
    private String password;
    @SerializedName(value = "userId")
    private String userId;

    public BaseUser(String userid, String password) {
        this.userId = userid;
        this.password = password;
    }
}
