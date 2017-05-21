package io.yostajsc.izigo;

import android.app.Application;
import android.content.Intent;

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
import io.yostajsc.izigo.usecase.service.firebase.FirebaseManager;
import io.yostajsc.izigo.usecase.service.location.LocationService;
import io.yostajsc.sdk.model.trip.IgTrip;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends Application {

    private static final String TAG = AppConfig.class.getSimpleName();

    public static final String FB_ID = "FB_ID";
    public static final String PAGE_ID = "PAGE_ID";
    public static final String IS_AVAILABLE = "IS_AVAILABLE";
    public static final String KEY_USER_ROLE = "KEY_USER_ROLE";
    public static final String AUTHORIZATION = "AUTHORIZATION";
    public static final String KEY_PICK_LOCATION = "KEY_PICK_LOCATION";
    public static final String PARAMETERS = "id, first_name, last_name, email, cover, gender, birthday, location";

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
        try {
            Realm.init(this);
            final RealmConfiguration configuration = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .name("izigo.realm")
                    .schemaVersion(42)
                    .build();
            Realm.setDefaultConfiguration(configuration);
            Realm.getInstance(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

    private void onApplyFont() {
        FontUtils.overrideFont(this, "SERIF", "fonts/Nunito-Regular.ttf");
    }

    public void logout() {
        PrefsUtils.inject(this).removes(AppConfig.AUTHORIZATION);
        LoginManager.getInstance().logOut();
    }

    public void startLocationServer() {
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


    public String getCurrentTripId() {
        return PrefsUtils.inject(AppConfig.getInstance()).getString(IgTrip.TRIP_ID);
    }

    public void setCurrentTripId(String tripId) {
        PrefsUtils.inject(this).save(IgTrip.TRIP_ID, tripId);
    }

    public void setAvailable(boolean available) {
        PrefsUtils.inject(this).save(IS_AVAILABLE, available);
    }

    public boolean isAvailable() {
        return PrefsUtils.inject(this).getBoolean(IS_AVAILABLE);
    }
}
