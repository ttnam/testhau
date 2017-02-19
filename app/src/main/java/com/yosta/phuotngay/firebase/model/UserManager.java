package com.yosta.phuotngay.firebase.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
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

    public static final String PARAMETERS = "id, first_name, last_name, email, cover, gender, birthday, location";

    public User getFacebookUserInfo(JSONObject object) {
        User user = new User();
        try {
            String id = object.getString("id");
            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
            user.setAvatar(profile_pic.toString());
            user.setFbId(id);
            if (object.has("first_name"))
                user.setFirstName(object.getString("first_name"));
            if (object.has("last_name"))
                user.setLastName(object.getString("last_name"));
            if (object.has("email"))
                user.setEmail(object.getString("email"));
            if (object.has("gender"))
                user.setGender(object.getString("gender"));
            if (object.has("birthday"))
                user.setBirthday(object.getString("birthday"));
            if (object.has("location"))
                user.setLocation(object.getJSONObject("location").getString("name"));
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
