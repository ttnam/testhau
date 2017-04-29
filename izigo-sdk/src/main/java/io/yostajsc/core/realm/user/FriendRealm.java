package io.yostajsc.core.realm.user;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Phuc-Hau Nguyen on 3/4/2017.
 */

public class FriendRealm extends RealmObject implements Serializable {

    private String fbId;
    private String name;
    private String avatar;

    public String getFbId() {
        return fbId;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
