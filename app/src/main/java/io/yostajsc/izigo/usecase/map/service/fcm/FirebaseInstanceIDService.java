package io.yostajsc.izigo.usecase.map.service.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.izigo.usecase.firebase.FirebaseManager;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PrefsUtils.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        try {
            IzigoSdk.UserExecutor.updateFcm(token);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
