package io.yostajsc.usecase.realm.user;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nphau on 4/11/17.
 */

public class BaseUserInfoRealm extends RealmObject implements Serializable {

    @PrimaryKey
    private String mAuthorization;
    private String mEmail;
    private String mFaceBookUId;
    private String mFireBaseUId;

    public BaseUserInfoRealm() {

    }

    public BaseUserInfoRealm(String authorization, String email, String faceBookUId, String fireBaseUId) {
        this.mAuthorization = authorization;
        this.mEmail = email;
        this.mFaceBookUId = faceBookUId;
        this.mFireBaseUId = fireBaseUId;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFaceBookUId() {
        return mFaceBookUId;
    }

    public String getFireBaseUId() {
        return mFireBaseUId;
    }
}
