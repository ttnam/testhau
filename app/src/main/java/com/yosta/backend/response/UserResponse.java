package com.yosta.backend.response;

import com.google.gson.annotations.SerializedName;
import com.yosta.firebase.model.User;

/**
 * Created by Phuc-Hau Nguyen on 2/28/2017.
 */

public class UserResponse extends BaseResponse {

    @SerializedName("data")
    private User data;
}
