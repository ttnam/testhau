package io.yostajsc.izigo.models.user;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.yostajsc.utils.AppUtils;

@IgnoreExtraProperties
public class User implements Serializable {

    @SerializedName("firebaseUid")
    private String fireBaseId;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("dateOfBirth")
    private String birthday;

    @SerializedName("email")
    private String email;

    private String fbId;
    private String name;

    @SerializedName("firstName")
    private String firstName;


    @SerializedName("lastName")
    private String lastName;

    @SerializedName("gender")
    private String gender;

    private String location;

    @SerializedName("memberShip")
    private long memberShip;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemberShip() {
        return AppUtils.builder().getTime(this.memberShip, AppUtils.DD_MM_YYYY);
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", firstName, lastName);
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
