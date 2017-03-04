package com.yosta.backend.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class CreateGroupResponse extends BaseResponse {

    @SerializedName("data")
    private String data;

    public String getGroupId(){
        return this.data;
    }
}
