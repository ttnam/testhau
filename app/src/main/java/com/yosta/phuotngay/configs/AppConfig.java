package com.yosta.phuotngay.configs;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.helpers.StorageHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFireBase();
        onApplyRealm();
    }

    private void initFireBase() {
        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        StorageHelper.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, token);
    }

    private void onApplyRealm() {
        Realm.init(this);
        final RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name("PhuongNgay.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }
}
