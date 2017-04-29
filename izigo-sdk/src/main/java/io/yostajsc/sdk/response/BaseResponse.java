package io.yostajsc.sdk.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class BaseResponse<T> implements Serializable {

    @SerializedName("responseCode")
    private int responseCode;

    @SerializedName("description")
    private String description;

    @SerializedName("data")
    private T mData;

    public T data() {
        return this.mData;
    }

    public boolean isSuccessful() {
        return responseCode == 1;
    }

    public boolean isExpired() {
        return responseCode == 0;
    }

    public String getDescription() {
        return description;
    }
}
