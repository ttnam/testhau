package io.yostajsc.usecase.backend.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.yostajsc.izigo.models.user.Authorization;
import io.yostajsc.usecase.realm.user.FriendsRealm;
import io.yostajsc.usecase.backend.response.BaseResponse;
import io.yostajsc.constants.TripTypePermission;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.izigo.models.notification.Notifications;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.models.user.User;
import io.yostajsc.usecase.realm.trip.OwnTrips;
import io.yostajsc.usecase.realm.trip.PublicTrips;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class APIManager {

    private static final String TAG = APIManager.class.getSimpleName();

    private static String SERVER_DOMAIN = "http://izigo.jelasticlw.com.br/";

    private static final int CONNECT_TIME_OUT = 20;
    private static final int READ_TIME_OUT = 20;

    private static String mAuthorization = null;

    private APIInterface service = null;
    private static APIManager mInstance = null;

    private APIManager() {

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

        service = retrofit.create(APIInterface.class);
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

    public static APIManager connect() {
        if (mInstance == null) {
            mInstance = new APIManager();
        }
        mAuthorization = AppConfig.getInstance().getAuthorization();
        return mInstance;
    }

    public void login(String email, String fbId, String fireBaseUid,
                      final CallBackWith<Authorization> success,
                      final CallBackWith<String> fail) {

        Call<BaseResponse<Authorization>> call = service.apiLogin(
                AppConfig.getInstance().getFbToken(),
                email,
                fbId,
                fireBaseUid,
                AppConfig.getInstance().getFcmKey());

        call.enqueue(new Callback<BaseResponse<Authorization>>() {
            @Override
            public void onResponse(Call<BaseResponse<Authorization>> call, Response<BaseResponse<Authorization>> response) {
                if (response.isSuccessful()) {

                    BaseResponse<Authorization> loginRes = response.body();

                    // Update to sharePreferences
                    AppConfig.getInstance().updateAuthorization(loginRes.data());
                    if (loginRes.isSuccessful()) {
                        success.run(loginRes.data());
                    } else {
                        fail.run(loginRes.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Authorization>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void onUpdate(String authorization, @NonNull Map<String, String> data,
                         final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse> call = service.apiUpdateUserInfo(authorization, data);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.isSuccessful()) {
                        success.run();
                    } else {
                        fail.run(baseResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void getAllPublicTrips(final CallBackWith<PublicTrips> successful,
                                  final CallBack expired,
                                  final CallBackWith<String> fail) {
        try {
            Call<BaseResponse<PublicTrips>> call = service.apiGetAllPublicTrips(mAuthorization);
            call.enqueue(new Callback<BaseResponse<PublicTrips>>() {
                @Override
                public void onResponse(Call<BaseResponse<PublicTrips>> call, Response<BaseResponse<PublicTrips>> response) {
                    if (response.isSuccessful()) {
                        BaseResponse<PublicTrips> res = response.body();
                        if (res.isSuccessful()) {
                            successful.run(res.data());
                        } else if (res.isExpired()) {
                            expired.run();
                        } else {
                            fail.run(res.getDescription());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<PublicTrips>> call, Throwable throwable) {
                    fail.run(throwable.getMessage());
                    log(throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log(e.getMessage());
        }
    }


    public void getAllOwnTripsList(final CallBackWith<OwnTrips> onSuccessful,
                                   final CallBack expired,
                                   final CallBackWith<String> fail) {
        try {
            Call<BaseResponse<OwnTrips>> call = service.apiGetAllOwnTrips(mAuthorization);
            call.enqueue(new Callback<BaseResponse<OwnTrips>>() {
                @Override
                public void onResponse(Call<BaseResponse<OwnTrips>> call, Response<BaseResponse<OwnTrips>> response) {
                    if (response.isSuccessful()) {
                        BaseResponse<OwnTrips> res = response.body();
                        if (res.isSuccessful()) {
                            onSuccessful.run(res.data());
                        } else if (res.isExpired()) {
                            expired.run();
                        } else {
                            fail.run(res.getDescription());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<OwnTrips>> call, Throwable throwable) {
                    fail.run(throwable.getMessage());
                    log(throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    public void updateFcm(String fcm) {
        Call<BaseResponse> call = service.apiUpdateFcm(AppConfig.getInstance().getAuthorization(), fcm);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(TAG, response.body().getDescription());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void getTripDetail(String tripId,
                              final CallBackWith<Trip> success,
                              final CallBackWith<String> fail,
                              final CallBack expired) {
        Call<BaseResponse<Trip>> call = service.apiGetTripDetail(AppConfig.getInstance().getAuthorization(), tripId);

        call.enqueue(new Callback<BaseResponse<Trip>>() {
            @Override
            public void onResponse(Call<BaseResponse<Trip>> call, Response<BaseResponse<Trip>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Trip> res = response.body();
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
            public void onFailure(Call<BaseResponse<Trip>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void getUserInfo(final CallBackWith<User> success,
                            final CallBack expired, final CallBackWith<String> fail) {

        Call<BaseResponse<User>> call = service.apiGetUserInfo(AppConfig.getInstance().getAuthorization());
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> res = response.body();
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
            public void onFailure(Call<BaseResponse<User>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });

    }

    public void createTrips(String groupName, String arrive, String depart, String description,
                            int is_published, int status, int transfer,
                            final CallBack expired,
                            final CallBackWith<String> success,
                            final CallBackWith<String> fail) {
        Call<BaseResponse<String>> call = service.apiCreateTrips(AppConfig.getInstance().getAuthorization(), groupName, arrive, depart, description,
                is_published, status, transfer);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {

                if (response.isSuccessful()) {
                    BaseResponse<String> res = response.body();
                    if (res.isExpired()) {
                        expired.run();
                    } else if (res.isSuccessful()) {
                        success.run(res.data());
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

    public void getFriendsList(String fbAccessToken,
                               final CallBackWith<FriendsRealm> success,
                               final CallBackWith<String> fail,
                               final CallBack expired) {

        Call<BaseResponse<FriendsRealm>> call = service.apiGetFriendsList(mAuthorization, fbAccessToken);

        call.enqueue(new Callback<BaseResponse<FriendsRealm>>() {
            @Override
            public void onResponse(Call<BaseResponse<FriendsRealm>> call, Response<BaseResponse<FriendsRealm>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<FriendsRealm> res = response.body();
                    if (res.isExpired()) {
                        expired.run();
                    } else if (res.isSuccessful()) {
                        success.run(res.data());
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<FriendsRealm>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
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

    public void updateProfile(Map<String, String> body,
                              final CallBack expired,
                              final CallBack success,
                              final CallBackWith<String> fail) {
        Call<BaseResponse> call = service.apiUpdateUserInfo(AppConfig.getInstance().getAuthorization(), body);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse res = response.body();
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
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void getComments(String tripId,
                            final CallBack expired,
                            final CallBackWith<Comments> success,
                            final CallBackWith<String> fail) {
        Call<BaseResponse<Comments>> call = service.apiGetComments(AppConfig.getInstance().getAuthorization(), tripId);
        call.enqueue(new Callback<BaseResponse<Comments>>() {
            @Override
            public void onResponse(Call<BaseResponse<Comments>> call, Response<BaseResponse<Comments>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Comments> res = response.body();
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
            public void onFailure(Call<BaseResponse<Comments>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void getActivities(String tripId,
                              final CallBack expired,
                              final CallBackWith<Timelines> success,
                              final CallBackWith<String> fail) {
        Call<BaseResponse<Timelines>> call = service.apiGetActivities(AppConfig.getInstance().getAuthorization(), tripId);
        call.enqueue(new Callback<BaseResponse<Timelines>>() {
            @Override
            public void onResponse(Call<BaseResponse<Timelines>> call, Response<BaseResponse<Timelines>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Timelines> res = response.body();
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
            public void onFailure(Call<BaseResponse<Timelines>> call, Throwable throwable) {
                log(throwable.getMessage());
            }
        });
    }

    public void getMembers(String tripId,
                           final CallBackWith<FriendsRealm> success,
                           final CallBackWith<String> fail,
                           final CallBack expired) {
        Call<BaseResponse<FriendsRealm>> call = service.apiGetMembers(mAuthorization, tripId);
        call.enqueue(new Callback<BaseResponse<FriendsRealm>>() {
            @Override
            public void onResponse(Call<BaseResponse<FriendsRealm>> call, Response<BaseResponse<FriendsRealm>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<FriendsRealm> res = response.body();
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
            public void onFailure(Call<BaseResponse<FriendsRealm>> call, Throwable throwable) {
                log(throwable.getMessage());
            }
        });
    }

    public void apiTrackingTripView(String tripId) {

        Call<BaseResponse<String>> call = service.apiUpdateView(mAuthorization, tripId);

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

    public void updateTripInfo(String tripId, String data, int type,
                               final CallBack success,
                               final CallBackWith<String> fail, final CallBack expired) {

        Call<BaseResponse<String>> call = service.apiUpdateTripCover(AppConfig.getInstance().getAuthorization(), tripId, data);

        if (type == TripTypePermission.NAME) {
            call = service.apiUpdateTripName(AppConfig.getInstance().getAuthorization(), tripId, data);
        } else if (type == TripTypePermission.STATUS) {
            call = service.apiUpdateTripStatus(AppConfig.getInstance().getAuthorization(), tripId, data);
        }
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
                log(throwable.getMessage());
            }
        });
    }

    public void join(String tripId, final CallBack success, final CallBackWith<String> fail, final CallBack expired) {

        Call<BaseResponse<String>> call = service.apiJoinGroup(AppConfig.getInstance().getAuthorization(), tripId);
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

    public void getNotification(final CallBack expired,
                                final CallBackWith<Notifications> success, final CallBackWith<String> fail) {

        Call<BaseResponse<Notifications>> call = service.apiGetNotification(AppConfig.getInstance().getAuthorization());
        call.enqueue(new Callback<BaseResponse<Notifications>>() {
            @Override
            public void onResponse(Call<BaseResponse<Notifications>> call, Response<BaseResponse<Notifications>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Notifications> res = response.body();
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
            public void onFailure(Call<BaseResponse<Notifications>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void accept(String tripId, String notiId, int verify, final CallBack expired,
                       final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.apiAccept(AppConfig.getInstance().getAuthorization(), tripId, notiId, verify);

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

    public void verify(String tripId, String notiId, int verify, final CallBack expired,
                       final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.apiVerify(AppConfig.getInstance().getAuthorization(), tripId, notiId, verify);

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

    public void addMembers(String authorization, String tripId, String fbId, final CallBack expired,
                           final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.apiAddMember(authorization, tripId, fbId);

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

    public void kick(String tripId, String fbId,
                     final CallBack expired,
                     final CallBack success,
                     final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.apiKickMember(AppConfig.getInstance().getAuthorization(), tripId, fbId);

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
                log(throwable.getMessage());
            }
        });
    }

    private void log(String msg) {
        Log.e(TAG, msg);
    }
}
