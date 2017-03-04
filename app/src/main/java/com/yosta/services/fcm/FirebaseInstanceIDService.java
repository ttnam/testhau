
package com.yosta.services.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yosta.phuotngay.configs.AppDefine;
import com.yosta.firebase.FirebaseManager;
import com.yosta.utils.StorageUtils;
import com.yosta.utils.validate.ValidateUtils;
import com.yosta.backend.config.APIManager;

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
                APIManager.connect().onUpdateFcm(authorization, token);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
