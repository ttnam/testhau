package com.yosta.phuotngay.firebase.model;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Phuc-Hau Nguyen on 1/24/2017.
 */
public class UserManager {

    private static UserManager ourInstance = new UserManager();

    public static UserManager inject() {
        return ourInstance;
    }

    private UserManager() {
    }

    public User getFacebookUserInfo(JSONObject object) {
        User user = new User();
        try {
            String id = object.getString("id");
            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
            user.setAvatar(profile_pic.toString());
            user.setFbId(id);
            if (object.has("cover"))
                user.setCover(object.getJSONObject("cover").getString("source"));
            if (object.has("name"))
                user.setLastName(object.getString("name"));
            if (object.has("first_name"))
                user.setFirstName(object.getString("first_name"));
            if (object.has("last_name"))
                user.setLastName(object.getString("last_name"));
            if (object.has("link"))
                user.setLastName(object.getString("link"));
            if (object.has("gender"))
                user.setGender(object.getString("gender"));
            if (object.has("locale"))
                user.setEmail(object.getString("locale"));
            if (object.has("email"))
                user.setEmail(object.getString("email"));
            if (object.has("picture"))
                user.setEmail(object.getString("picture"));
            if (object.has("timezone"))
                user.setEmail(object.getString("timezone"));
            if (object.has("birthday"))
                user.setBirthday(object.getString("birthday"));
            if (object.has("updated_time"))
                user.setBirthday(object.getString("updated_time"));
            if (object.has("location"))
                user.setLocation(object.getJSONObject("location").getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}