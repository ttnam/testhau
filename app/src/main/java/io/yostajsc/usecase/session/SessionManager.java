package io.yostajsc.usecase.session;

import android.support.annotation.IntDef;

import com.facebook.AccessToken;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.yostajsc.izigo.configs.AppConfig;

/**
 * Created by nphau on 4/13/17.
 */

public class SessionManager {

    public static
    @TOKEN
    int isExpired() {
        int res = TOKEN.STILL_LIVE;
        if (AppConfig.getInstance().isExpired()) {
            res = TOKEN.APP_TOKEN_EXPIRED;
        }
        return res;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOKEN.APP_TOKEN_EXPIRED, TOKEN.FACEBOOK_TOKEN_EXPIRED, TOKEN.STILL_LIVE})
    public @interface TOKEN {
        int FACEBOOK_TOKEN_EXPIRED = 901;
        int APP_TOKEN_EXPIRED = 902;
        int STILL_LIVE = 903;
    }

}
