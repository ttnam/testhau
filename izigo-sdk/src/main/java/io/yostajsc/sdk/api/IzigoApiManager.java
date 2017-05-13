package io.yostajsc.sdk.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.yostajsc.sdk.model.IgComment;
import io.yostajsc.sdk.model.IgNotification;
import io.yostajsc.sdk.model.IgTimeline;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.model.user.IgUser;
import io.yostajsc.sdk.model.TripTypePermission;
import io.yostajsc.sdk.model.token.IgToken;
import io.yostajsc.sdk.response.BaseResponse;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

class IzigoApiManager {

    private static final String TAG = IzigoApiManager.class.getSimpleName();

    private static String SERVER_DOMAIN = "http://izigo2.jelasticlw.com.br/";

    private static final int CONNECT_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;

    private IzigoApiInterface service = null;
    private static IzigoApiManager mInstance = null;

    private IzigoApiManager() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        httpClient.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                return onOnIntercept(chain);
            }
        });

        // Gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_DOMAIN)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(IzigoApiInterface.class);
    }

    private okhttp3.Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            okhttp3.Response response = chain.proceed(chain.request());
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(),
                    response.body().string())).build();
        } catch (SocketTimeoutException exception) {
            Log.e(TAG, "SocketTimeoutException");
        }

        return chain.proceed(chain.request());
    }

    public static IzigoApiManager connect() {
        if (mInstance == null) {
            mInstance = new IzigoApiManager();
        }
        return mInstance;
    }

    void login(String fbToken, @FieldMap Map<String, String> fields, final IgCallback<IgToken, String> callback) {

        Call<BaseResponse<IgToken>> call = service.login(fbToken, fields);

        call.enqueue(new Callback<BaseResponse<IgToken>>() {
            @Override
            public void onResponse(Call<BaseResponse<IgToken>> call, Response<BaseResponse<IgToken>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<IgToken> loginRes = response.body();
                    if (loginRes.isSuccessful()) {
                        callback.onSuccessful(loginRes.data());
                    } else {
                        callback.onFail(loginRes.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<IgToken>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
                log(throwable.getMessage());
            }
        });
    }

    public void getAllPublicTrips(String authorization, final IgCallback<List<IgTrip>, String> callback) {
        try {
            Call<BaseResponse<List<IgTrip>>> call = service.apiGetAllPublicTrips(authorization);
            call.enqueue(new Callback<BaseResponse<List<IgTrip>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<IgTrip>>> call, Response<BaseResponse<List<IgTrip>>> response) {
                    if (response.isSuccessful()) {
                        BaseResponse<List<IgTrip>> res = response.body();
                        if (res.isSuccessful()) {
                            callback.onSuccessful(res.data());
                        } else if (res.isExpired()) {
                            callback.onExpired();
                        } else {
                            callback.onFail(res.getDescription());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<IgTrip>>> call, Throwable throwable) {
                    callback.onFail(throwable.getMessage());
                    log(throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log(e.getMessage());
        }
    }


    public void getAllOwnTripsList(String authorization, int type, final IgCallback<List<IgTrip>, String> callback) {
        try {
            Call<BaseResponse<List<IgTrip>>> call = service.apiGetAllOwnTrips(authorization, type);
            call.enqueue(new Callback<BaseResponse<List<IgTrip>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<IgTrip>>> call, Response<BaseResponse<List<IgTrip>>> response) {
                    if (response.isSuccessful()) {
                        BaseResponse<List<IgTrip>> res = response.body();
                        if (res.isSuccessful()) {
                            callback.onSuccessful(res.data());
                        } else if (res.isExpired()) {
                            callback.onExpired();
                        } else {
                            callback.onFail(res.getDescription());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<IgTrip>>> call, Throwable throwable) {
                    callback.onFail(throwable.getMessage());
                    log(throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    public void updateFcm(String authorization, String fcm, final IgCallback<Void, String> callback) {
        Call<BaseResponse> call = service.apiUpdateFcm(authorization, fcm);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful())
                    callback.onSuccessful(null);
                else
                    callback.onFail(null);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
                log(throwable.getMessage());
            }
        });
    }

    public void getTripDetail(String authorization, String tripId, final IgCallback<IgTrip, String> callback) {

        Call<BaseResponse<IgTrip>> call = service.apiGetTripDetail(authorization, tripId);

        call.enqueue(new Callback<BaseResponse<IgTrip>>() {
            @Override
            public void onResponse(Call<BaseResponse<IgTrip>> call, Response<BaseResponse<IgTrip>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<IgTrip> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<IgTrip>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void getUserInfo(String authorization, final IgCallback<IgUser, String> callback) {

        Call<BaseResponse<IgUser>> call = service.apiGetUserInfo(authorization);
        call.enqueue(new Callback<BaseResponse<IgUser>>() {
            @Override
            public void onResponse(Call<BaseResponse<IgUser>> call, Response<BaseResponse<IgUser>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<IgUser> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<IgUser>> call, Throwable throwable) {
                log(throwable.getMessage());
                callback.onFail(throwable.getMessage());
            }
        });

    }

    public void addTrip(String authorization, String groupName, String arrive, String depart, String description, int transfer,
                        final IgCallback<String, String> callback) {
        Call<BaseResponse<String>> call = service.addTrip(authorization, groupName, arrive, depart, description, transfer);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {

                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isExpired())
                        callback.onExpired();
                    else if (res.isSuccessful())
                        callback.onSuccessful(res.data());
                    else
                        callback.onFail(res.getDescription());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void getFriendsList(String authorization, String fbAccessToken, final IgCallback<List<IgFriend>, String> callback) {

        Call<BaseResponse<List<IgFriend>>> call = service.apiGetFriendsList(authorization, fbAccessToken);

        call.enqueue(new Callback<BaseResponse<List<IgFriend>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<IgFriend>>> call, Response<BaseResponse<List<IgFriend>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<IgFriend>> res = response.body();
                    if (res.isExpired()) {
                        callback.onExpired();
                    } else if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<IgFriend>>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }


    public void getGroupDetail(String authorization, String groupId,
                               final CallBackWith<String> success,
                               final CallBackWith<String> fail,
                               final CallBack expired) {
        Call<BaseResponse<String>> call = service.apiGetGroupDetail(authorization, groupId);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful()) {
                        success.run(res.data());
                    } else if (res.isExpired()) {
                        expired.run();
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void updateProfile(String authorization, Map<String, String> body,
                              final IgCallback<Void, String> callback) {
        Call<BaseResponse> call = service.updateUserInfo(authorization, body);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(null);
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void getComments(String authorization, String tripId, final IgCallback<List<IgComment>, String> callback) {
        Call<BaseResponse<List<IgComment>>> call = service.apiGetComments(authorization, tripId);
        call.enqueue(new Callback<BaseResponse<List<IgComment>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<IgComment>>> call, Response<BaseResponse<List<IgComment>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<IgComment>> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<IgComment>>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void getActivities(String authorization, String tripId,
                              final IgCallback<List<IgTimeline>, String> callback) {

        Call<BaseResponse<List<IgTimeline>>> call = service.apiGetActivities(authorization, tripId);
        call.enqueue(new Callback<BaseResponse<List<IgTimeline>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<IgTimeline>>> call, Response<BaseResponse<List<IgTimeline>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<IgTimeline>> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<IgTimeline>>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void getMembers(String authorization, String tripId, final IgCallback<List<IgFriend>, String> callback) {
        Call<BaseResponse<List<IgFriend>>> call = service.getMembers(authorization, tripId);
        call.enqueue(new Callback<BaseResponse<List<IgFriend>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<IgFriend>>> call, Response<BaseResponse<List<IgFriend>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<IgFriend>> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<IgFriend>>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void apiTrackingTripView(String authorization, String tripId) {

        Call<BaseResponse<String>> call = service.apiUpdateView(authorization, tripId);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (!res.isSuccessful()) {
                        log(res.data());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                log(throwable.getMessage());
            }
        });
    }

    public void updateTripInfo(String authorization, String tripId, String data, int type,
                               final IgCallback<Void, String> callback) {

        Call<BaseResponse<String>> call = service.apiUpdateTripCover(authorization, tripId, data);

        if (type == TripTypePermission.NAME) {
            call = service.changeName(authorization, tripId, data);
        } else if (type == TripTypePermission.STATUS) {
            call = service.publish(authorization, tripId, data);
        }
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(null);
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
                log(throwable.getMessage());
            }
        });
    }

    public void join(String authorization, String tripId, final IgCallback<Void, String> callback) {


        Call<BaseResponse<String>> call = service.join(authorization, tripId);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful())
                        callback.onSuccessful(null);
                    else if (res.isExpired())
                        callback.onExpired();
                    else
                        callback.onFail(res.getDescription());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void getNotification(String authorization, final IgCallback<List<IgNotification>, String> callback) {

        Call<BaseResponse<List<IgNotification>>> call = service.getNotification(authorization);
        call.enqueue(new Callback<BaseResponse<List<IgNotification>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<IgNotification>>> call, Response<BaseResponse<List<IgNotification>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<IgNotification>> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(res.data());
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<IgNotification>>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void accept(String authorization, String tripId, String notiId, int verify, final CallBack expired,
                       final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.accept(authorization, tripId, notiId, verify);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful()) {
                        success.run();
                    } else if (res.isExpired()) {
                        expired.run();
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void verify(String authorization, String tripId, String notiId, int verify, final CallBack expired,
                       final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.apiVerify(authorization, tripId, notiId, verify);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful()) {
                        success.run();
                    } else if (res.isExpired()) {
                        expired.run();
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void addMembers(String authorization, String tripId, String fbId,
                           final IgCallback<Void, String> callback) {

        Call<BaseResponse<String>> call = service.apiAddMember(authorization, tripId, fbId);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(null);
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    public void kickMember(String authorization, String tripId, String fbId,
                           final IgCallback<Void, String> callback) {

        Call<BaseResponse<String>> call = service.apiKickMember(authorization, tripId, fbId);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(null);
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }


    void addComment(String authorization, String tripId, String content,
                    final IgCallback<Void, String> callback) {

        Call<BaseResponse> call = service.addComment(authorization, tripId, content);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse res = response.body();
                    if (res.isSuccessful()) {
                        callback.onSuccessful(null);
                    } else if (res.isExpired()) {
                        callback.onExpired();
                    } else {
                        callback.onFail(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                callback.onFail(throwable.getMessage());
            }
        });
    }

    void log(String msg) {
        Log.e(TAG, msg);
    }
}
