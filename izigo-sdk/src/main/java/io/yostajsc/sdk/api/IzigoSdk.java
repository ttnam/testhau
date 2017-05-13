package io.yostajsc.sdk.api;

import android.app.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.sdk.model.IgComment;
import io.yostajsc.sdk.model.IgNotification;
import io.yostajsc.sdk.model.Timeline;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.core.utils.LogUtils;
import io.yostajsc.sdk.consts.IgError;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.model.user.IgUser;
import io.yostajsc.sdk.model.token.IgToken;
import io.yostajsc.sdk.model.TripTypePermission;
import io.yostajsc.core.utils.PrefsUtils;

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

        public static void publishTrip(String tripId, boolean isPublish, IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().updateTripInfo(
                        auth,
                        tripId,
                        isPublish ? "1" : "0",
                        TripTypePermission.STATUS,
                        callback);
            } else {
                callback.onExpired();
            }
        }

        public static void changeCover(String tripId, String url, IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().updateTripInfo(
                        auth,
                        tripId,
                        url,
                        TripTypePermission.COVER, callback);
            } else {
                callback.onExpired();
            }
        }

        public static void changeName(String tripId, String name, IgCallback<Void, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().updateTripInfo(
                        auth,
                        tripId,
                        name,
                        TripTypePermission.NAME,
                        callback);
            } else {
                callback.onExpired();
            }
        }

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
                IzigoApiManager.connect().getTripDetail(auth, tripId, callback);
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

        public static void getActivities(String tripId, final IgCallback<List<Timeline>, String> callback) {
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

        public static void addTrip(String groupName, String arrive, String depart, String description, int transfer,
                                   final IgCallback<String, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().addTrip(auth, groupName, arrive, depart, description, transfer, callback);
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

        public static void getNotifications(final IgCallback<List<IgNotification>, String> callback) {
            if (IzigoSession.isLoggedIn()) {
                String auth = IzigoSession.getToken().getToken();
                IzigoApiManager.connect().getNotifications(auth, callback);
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
    }
}