package io.yostajsc.izigo.manager;

import org.json.JSONObject;

import java.net.URL;

import io.yostajsc.sdk.model.user.IgUser;

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

    public IgUser getFacebookUserInfo(JSONObject object) {
        IgUser igUser = new IgUser();
        try {
            String id = object.getString("id");
            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
            igUser.setAvatar(profile_pic.toString());
            igUser.setFbId(id);
            if (object.has("first_name") && object.has("last_name")) {
                igUser.setName(
                        object.getString("first_name") + object.getString("last_name"));
            }
            if (object.has("gender"))
                igUser.setGender(object.getString("gender"));
            if (object.has("email"))
                igUser.setEmail(object.getString("email"));
            if (object.has("cover"))
                igUser.setCover(object.getString("cover"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return igUser;
    }
}