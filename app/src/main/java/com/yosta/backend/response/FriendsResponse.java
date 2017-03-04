package com.yosta.backend.response;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.Friend;

import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 3/4/2017.
 */

public class FriendsResponse extends BaseResponse {

    @SerializedName("data")
    private List<Friend> friends;

    public List<Friend> getFriends() {
        return friends;
    }


}
