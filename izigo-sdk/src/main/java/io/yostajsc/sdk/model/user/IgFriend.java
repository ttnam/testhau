package io.yostajsc.sdk.model.user;

import java.io.Serializable;


/**
 * Created by Phuc-Hau Nguyen on 3/4/2017.
 */

public class IgFriend implements Serializable {

    private String fbId;
    private String name;
    private String avatar;


    public IgFriend() {
    }

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
