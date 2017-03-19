package io.yostajsc.izigo.services.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.firebase.FirebaseManager;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        StorageUtils.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        try {
            String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
            if (ValidateUtils.canUse(authorization))
                APIManager.connect().updateFcm(authorization, token);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
