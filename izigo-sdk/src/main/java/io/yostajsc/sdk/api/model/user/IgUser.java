package io.yostajsc.sdk.api.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.yostajsc.sdk.utils.DatetimeUtils;

public class IgUser implements Serializable {

    @SerializedName("firebaseUid")
    private String fireBaseId;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("email")
    private String email;

    private String fbId;

    @SerializedName("name")
    private String name;

    private String cover;

    @SerializedName("gender")
    private String gender;

    private String location;

    @SerializedName("memberShip")
    private long memberShip;

    public IgUser() {
        // Default constructor required for calls to DataSnapshot.getValue(IgUser.class)
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemberShip() {
        return DatetimeUtils.getDate(this.memberShip);
    }

    public String getFullName() {
        return name;
    }

    public String getFireBaseId() {
        return fireBaseId;
    }

    public void setFireBaseId(String fireBaseId) {
        this.fireBaseId = fireBaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
