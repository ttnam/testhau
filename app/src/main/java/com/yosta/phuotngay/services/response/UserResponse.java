package com.yosta.phuotngay.services.response;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.firebase.model.User;

/**
 * Created by Phuc-Hau Nguyen on 2/28/2017.
 */

public class UserResponse extends BaseResponse {

    @SerializedName("data")
    private User data;
}
