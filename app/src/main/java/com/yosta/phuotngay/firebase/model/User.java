package com.yosta.phuotngay.firebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {

    public static final String AUTHORIZATION = "AUTHORIZATION";

    private String fireBaseId;
    private String avatar;
    private String cover;
    private String authen;
    private String birthday;
    private String email;
    private String fbId;
    private String firstName;
    private String lastName;
    private String gender;
    private String location;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public long getMemberShip() {
        return memberShip;
    }

    public void setMemberShip(long memberShip) {
        this.memberShip = memberShip;
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


    public String getAuthen() {
        return authen;
    }

    public void setAuthen(String authen) {
        this.authen = authen;
    }

    public boolean IsValid() {
        return authen != null && fireBaseId != null && !authen.equals("") && !fireBaseId.equals("");
    }
}
