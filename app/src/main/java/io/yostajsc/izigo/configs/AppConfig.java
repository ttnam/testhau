package io.yostajsc.izigo.configs;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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

public class AppConfig extends Application {

    public static final String TAG = AppConfig.class.getSimpleName();
    public static final String AUTHORIZATION = "AUTHORIZATION";

    public static final String USER = "USER";
    public static final String TRIP_ID = "TRIP_ID";
    public static final String FB_ID = "FB_ID";
    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String KEY_PICK_LOCATION = "KEY_PICK_LOCATION";
    public static final String PARAMETERS = "id, first_name, last_name, email, cover, gender, birthday, location";
    public static final String PAGE_ID = "PAGE_ID";

    private static AppConfig mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        onApplyFireBase();
        onApplyRealm();
        applyFont();
    }


    public static synchronized AppConfig getInstance() {
        return mInstance;
    }

    private void onApplyFireBase() {
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
        FontUtils.overrideFont(this, "serif", "fonts/Montserrat-Regular.ttf");
    }

    public String getAuthorization() {
        return StorageUtils.inject(this).getString(AUTHORIZATION);
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void logout() {
        StorageUtils.inject(this).removes(AppConfig.AUTHORIZATION);
        LoginManager.getInstance().logOut();
    }
}
