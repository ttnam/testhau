package io.yostajsc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.yostajsc.core.utils.FontUtils;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.usecase.firebase.FirebaseManager;
import io.yostajsc.utils.LocationService;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends Application {

    public static final String TAG = AppConfig.class.getSimpleName();

    public static final String AUTHORIZATION = "AUTHORIZATION";
    public static final String TRIP_ID = "TRIP_ID";
    public static final String FB_ID = "FB_ID";
    public static final String KEY_PICK_LOCATION = "KEY_PICK_LOCATION";
    public static final String PARAMETERS = "id, first_name, last_name, email, cover, gender, birthday, location";
    public static final String PAGE_ID = "PAGE_ID";

    public static List<IgImage> igImages = new ArrayList<>();

    private static AppConfig mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        onApplyFont();
        onApplyRealm();
        onApplyFireBase();
        onApplyFacebookSDK();
    }

    public static synchronized AppConfig getInstance() {
        return mInstance;
    }

    private void onApplyFacebookSDK() {
        FacebookSdk.sdkInitialize(this);
    }

    private void onApplyFireBase() {
        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        PrefsUtils.inject(this).save(FirebaseManager.FIRE_BASE_TOKEN, token);
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

    private void onApplyFont() {
        FontUtils.overrideFont(this, "SERIF", "fonts/Roboto-Regular.ttf");
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void logout() {
        PrefsUtils.inject(this).removes(AppConfig.AUTHORIZATION);
        LoginManager.getInstance().logOut();
    }

    public void startLocationServer(String tripId) {
        PrefsUtils.inject(this).save(AppConfig.TRIP_ID, tripId);
        startService(new Intent(this, LocationService.class));
    }

    public void stopLocationService() {
        stopService(new Intent(this, LocationService.class));
    }

    public String getFbToken() {
        return AccessToken.getCurrentAccessToken().getToken();
    }

    public String getFcmKey() {
        return PrefsUtils.inject(this).getString(FirebaseManager.FIRE_BASE_TOKEN);
    }
}
