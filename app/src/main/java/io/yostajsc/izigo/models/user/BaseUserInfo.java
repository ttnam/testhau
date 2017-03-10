package io.yostajsc.izigo.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Phuc-Hau Nguyen on 3/9/2017.
 */

public class BaseUserInfo extends RealmObject implements Serializable {

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("fbId")
    private String fbId;

    @SerializedName("name")
    private String name;

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }
}
