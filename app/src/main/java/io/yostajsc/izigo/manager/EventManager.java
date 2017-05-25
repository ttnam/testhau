package io.yostajsc.izigo.manager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import io.yostajsc.sdk.consts.CallBack;
import io.yostajsc.sdk.consts.CallBackWith;


/**
 * Created by Phuc-Hau Nguyen on 2/16/2017.
 */

public class EventManager {

    private final String TAG = EventManager.class.getSimpleName();

    private static EventManager mInstance = null;

    private TextWatcher textWatcher = null;

    private EventManager() {

    }

    public static EventManager connect() {
        if (mInstance == null) {
            mInstance = new EventManager();
        }
        return mInstance;
    }

    public TextWatcher addTextWatcherEvent(final CallBack callBack) {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callBack.run();
            }
        };
        return textWatcher;
    }

    public FacebookCallback<LoginResult> registerFacebookCallback(final CallBackWith<AccessToken> callback) {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                callback.run(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.getMessage());
            }
        };
    }

}
