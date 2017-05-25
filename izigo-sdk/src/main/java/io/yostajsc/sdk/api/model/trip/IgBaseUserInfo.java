package io.yostajsc.sdk.api.model.trip;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nphau on 5/10/17.
 */

public class IgBaseUserInfo implements Serializable {

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("fbId")
    private String fbId;

    @SerializedName("name")
    private String name;

    public IgBaseUserInfo() {
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

}