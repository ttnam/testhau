
package com.yosta.phuotngay.services.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.services.api.APIManager;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        StorageHelper.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    private void sendRegistrationToServer(String token) {
        String authen = StorageHelper.inject(this).getString(User.AUTHORIZATION);
        APIManager.connect().onUpdateFcm(authen, authen);
    }
}
