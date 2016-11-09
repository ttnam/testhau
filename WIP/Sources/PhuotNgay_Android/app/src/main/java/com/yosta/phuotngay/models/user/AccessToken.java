package com.yosta.phuotngay.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class AccessToken implements Serializable {

    private String accessToken;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if (!Character.isUpperCase(tokenType.charAt(0))) {
            tokenType = Character
                    .toString(tokenType.charAt(0))
                    .toUpperCase() + tokenType.substring(1);
        }

        return tokenType;
    }

}
