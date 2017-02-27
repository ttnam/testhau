package com.yosta.phuotngay.services.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class BaseResponse implements Serializable {

    @SerializedName("responseCode")
    private int responseCode;

    @SerializedName("description")
    private String description;


    public boolean IsSuccess() {
        return responseCode == 1;
    }

    public String getDescription() {
        return description;
    }
}
