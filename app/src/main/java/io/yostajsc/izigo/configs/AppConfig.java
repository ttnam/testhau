package io.yostajsc.izigo.configs;

import android.support.multidex.MultiDexApplication;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.yostajsc.core.utils.FontUtils;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.firebase.FirebaseManager;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends MultiDexApplication {

    public static final String TAG = AppConfig.class.getSimpleName();

    private static AppConfig mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initFireBase();
        onApplyRealm();
        applyFont();
    }


    public static synchronized AppConfig getInstance() {
        return mInstance;
    }

    private void initFireBase() {
        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        StorageUtils.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, token);

    }

    private void onApplyRealm() {
        Realm.init(this);
        final RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name("izigo.realm")
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

    private void applyFont() {
        FontUtils.overrideFont(getApplicationContext(), "serif",
                "fonts/AvenirNext-Regular.ttf");
    }

}
