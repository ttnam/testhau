
package com.yosta.phuotngay.services.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yosta.phuotngay.configs.AppDefine;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.helpers.validate.ValidateHelper;
import com.yosta.phuotngay.services.api.APIManager;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        StorageHelper.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        try {
            String authorization = StorageHelper.inject(this).getString(AppDefine.AUTHORIZATION);
            if (ValidateHelper.canUse(authorization))
                APIManager.connect().onUpdateFcm(authorization, token);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
