package io.yostajsc.sdk.api;

import android.app.Application;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.yostajsc.sdk.api.model.IgSuggestion;
import io.yostajsc.sdk.consts.CallBack;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.api.cache.IgCache;
import io.yostajsc.sdk.api.model.IgComment;
import io.yostajsc.sdk.api.model.IgNotification;
import io.yostajsc.sdk.api.model.IgTimeline;
import io.yostajsc.sdk.api.model.user.IgFriend;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.sdk.consts.IgError;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.user.IgUser;
import io.yostajsc.sdk.api.model.token.IgToken;
import io.yostajsc.sdk.utils.LogUtils;
import io.yostajsc.sdk.utils.PrefsUtils;
import retrofit2.http.FieldMap;

/**
 * Created by nphau on 4/26/17.
 */

public class IzigoSdk {

    private static Application mApplication;
    private static IzigoSdk mInstance = null;

    private IzigoSdk(Application application) {
        mApplication = application;
    }

    public static IzigoSdk init(Application application) {
        if (mInstance == null)
            mInstance = new IzigoSdk(application);
        return mInstance;
    }

    private static class IzigoSession {

        private static boolean isLoggedIn() {
            IgToken token = getToken();
            return token != null && !token.isExpired();
        }

        private static IgToken getToken() {
            return PrefsUtils.inject(mApplication).getToken();
        }

        private static void setToken(IgToken mToken) {
            PrefsUtils.inject(mApplication).setToken(mToken);
        }

        private static void removeToken() {
            PrefsUtils.inject(mApplication).removes();
        }
    }

    public static class TripExecutor {

        private final static String TAG = TripExecutor.class.getSimpleName();

        public static void increaseTripView(String tripId) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().apiTrackingTripView(auth, tripId);
            } else {
                LogUtils.log(TAG, IgError.NOT_AUTHORIZATION);
            }
        }

        public static void getPublishTrip(final IgCallback<List<IgTrip>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getAllPublicTrips(auth, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void getOwnTrip(int type, final IgCallback<List<IgTrip>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getAllOwnTripsList(auth, type, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void getTripDetail(String tripId, final IgCallback<IgTrip, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getTripDetail(auth, tripId, new IgCallback<IgTrip, String>() {
                    @Override
                    public void onSuccessful(IgTrip igTrip) {
                        IgCache.TripCache.cacheAlbum(igTrip.getAlbum());
                        callback.onSuccessful(igTrip);
                    }

                    @Override
                    public void onFail(String error) {
                        callback.onFail(error);
                    }

                    @Override
                    public void onExpired() {
                        callback.onExpired();
                    }
                });
            } else {
                callback.onExpired();
            }
        }

        public static void getMembers(String tripId, final IgCallback<List<IgFriend>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getMembers(auth, tripId, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void addMembers(String tripId, String fbId, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().addMembers(auth, tripId, fbId, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void kickMembers(String tripId, String fbId, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().kickMember(auth, tripId, fbId, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void getActivities(String tripId, final IgCallback<List<IgTimeline>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getActivities(auth, tripId, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void getComments(String tripId, final IgCallback<List<IgComment>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                IzigoApiManager.connect().getComments(IzigoSession.getToken().getToken(), tripId, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void addComment(String tripId, String content, final CallBackWith<String> onFail) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().addComment(auth, tripId, content, new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {

                    }

                    @Override
                    public void onFail(String error) {
                        onFail.run(error);
                    }

                    @Override
                    public void onExpired() {

                    }
                });
            }
        }

        public static void addActivity(String tripId, String content, long time,
                                       final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().addActivity(auth, tripId, content, time, callback);
            }
        }

        public static void addTrip(@FieldMap Map<String, String> fields, final IgCallback<String, String> callback) {

            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().addTrip(auth, fields, callback);
            }
        }

        public static void updateTrip(String tripId, @FieldMap Map<String, String> fields, final IgCallback<Void, String> callback) {

            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().updateTrip(auth, tripId, fields, callback);
            }
        }

        public static void updateTripStatus(String tripId, String status, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                final Map<String, String> fields = new HashMap<>();
                fields.put("status", status);
                IzigoApiManager.connect().updateTrip(auth, tripId, fields, callback);
            }
        }

        public static void updateTripPublishStatus(String tripId, boolean isPublished, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                final Map<String, String> fields = new HashMap<>();
                fields.put("is_published", isPublished ? "1" : "0");
                IzigoApiManager.connect().updateTrip(auth, tripId, fields, callback);
            }
        }

        public static void join(String tripId, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().join(auth, tripId, callback);
            }
        }

        public static void uploadAlbum(String tripId, List<File> files, final IgCallback<Void, String> callback) {
            try {
                if (IzigoSession.isLoggedIn()) {
                    String auth = IzigoSession.getToken().getToken();
                    IzigoApiManager.connect().uploadAlbum(auth, tripId, files, callback);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void deleteImage(String tripId, String imageId, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().deleteImage(auth, tripId, imageId, callback);
            }
        }

        public static void uploadCover(File file, String mimeType, String tripId, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().uploadCover(auth, file, mimeType, tripId, callback);
            }
        }

        public static void getSuggestion(double lat, double lng, final IgCallback<List<IgSuggestion>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getSuggestion(auth, lat, lng, callback);
            }
        }
    }

    public static class UserExecutor {

        private final static String TAG = UserExecutor.class.getSimpleName();

        public static void getInfo(final IgCallback<IgUser, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getUserInfo(auth, callback);
            } else {
                callback.onExpired();
            }
        }

        public static boolean isLoggedIn() {
            return IzigoSession.isLoggedIn();
        }

        public static void logOut() {
            IzigoSession.removeToken();
        }

        public static void login(String fbToken, String email, final String fbId,
                                 String fireBaseUid, String fcm,
                                 final String avatar, final String name, String gender,
                                 final CallBack onSuccess, final CallBackWith<String> onFail) {

            Map<String, String> fields = new HashMap<>();
            fields.put("fcm", fcm);
            fields.put("name", name);
            fields.put("fbId", fbId);
            fields.put("email", email);
            fields.put("avatar", avatar);
            fields.put("gender", gender);
            fields.put("firebaseUid", fireBaseUid);

            IzigoApiManager.connect().login(fbToken, fields, new IgCallback<IgToken, String>() {
                @Override
                public void onSuccessful(IgToken igToken) {
                    igToken.setFbId(fbId);
                    igToken.setAvatar(avatar);
                    igToken.setName(name);
                    IzigoSession.setToken(igToken);
                    onSuccess.run();
                }

                @Override
                public void onFail(String error) {
                    onFail.run(error);
                }

                @Override
                public void onExpired() {
                    LogUtils.log(TAG, "onExpired");
                }
            });
        }

        public static void updateInfo(Map<String, String> body,
                                      final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().updateProfile(auth, body, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void updateFcm(String fcm) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().updateFcm(auth, fcm, new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {

                    }

                    @Override
                    public void onFail(String error) {
                        LogUtils.log(TAG, error);
                    }

                    @Override
                    public void onExpired() {
                        LogUtils.log(TAG, "onExpired");
                    }
                });
            }
        }

        public static void getFriends(String fbToken, final IgCallback<List<IgFriend>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getFriendsList(auth, fbToken, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void getNotifications(String type, final IgCallback<List<IgNotification>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getNotification(auth, type, callback);
            } else {
                callback.onExpired();
            }
        }

        public static String getOwnFbId() {
            return IzigoSession.getToken().getFbId();
        }

        public static String getOwnName() {
            IgToken igToken = IzigoSession.getToken();
            return igToken.getName();
        }

        public static String getOwnAvatar() {
            IgToken igToken = IzigoSession.getToken();
            return igToken.getAvatar();
        }

        public static void accept(String tripId, String notiId, int verify, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().accept(auth, tripId, notiId, verify, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void verify(String tripId, String notiId, int verify, final IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().verify(auth, tripId, notiId, verify, callback);
            } else {
                callback.onExpired();
            }
        }
    }
}