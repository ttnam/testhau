package io.yostajsc.backend.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.yostajsc.backend.response.BaseResponse;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.interfaces.OnConnectionTimeoutListener;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.izigo.models.notification.Notifications;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.models.trip.Trips;
import io.yostajsc.izigo.models.user.Friend;
import io.yostajsc.izigo.models.user.User;
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
    private static final int CONNECT_TIME_OUT = 5;
    private static final int READ_TIME_OUT = 5;

    private static OnConnectionTimeoutListener mTimeoutListener = null;

    private APIInterface service = null;
    private static APIManager mInstance = null;

    private APIManager(OnConnectionTimeoutListener timeoutListener) {


        mTimeoutListener = timeoutListener;

        // OkHttp
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        httpClient.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                return onOnIntercept(chain);
            }
        });

        // G son
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
            if (mTimeoutListener != null)
                mTimeoutListener.onConnectionTimeout();
        }

        return chain.proceed(chain.request());
    }

    public static APIManager connect(OnConnectionTimeoutListener timeoutListener) {
        if (mInstance == null) {
            mInstance = new APIManager(timeoutListener);
        }
        return mInstance;
    }

    public void onLogin(String email, String fbId, String fireBaseUid, String fcm,
                        final CallBackWith<String> success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.login(email, fbId, fireBaseUid, fcm);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call,
                                   Response<BaseResponse<String>> response) {

                if (response.isSuccessful()) {
                    BaseResponse<String> loginRes = response.body();
                    if (loginRes.isSuccessful()) {
                        success.run(loginRes.data());
                    } else {
                        fail.run(loginRes.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void onUpdate(String authorization, @NonNull Map<String, String> data,
                         final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse> call = service.updateProfile(authorization, data);
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

    public void getTripsList(String authorization, final CallBack expired,
                             final CallBackWith<Trips> success, final CallBackWith<String> fail) {

        Call<BaseResponse<Trips>> call = service.getTrips(authorization);
        call.enqueue(new Callback<BaseResponse<Trips>>() {
            @Override
            public void onResponse(Call<BaseResponse<Trips>> call, Response<BaseResponse<Trips>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Trips> res = response.body();
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
            public void onFailure(Call<BaseResponse<Trips>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });

    }


    public void getOwnTripsList(String authorization, final CallBack expired,
                                final CallBackWith<Trips> success, final CallBackWith<String> fail) {

        Call<BaseResponse<Trips>> call = service.getOwnTrips(authorization);
        call.enqueue(new Callback<BaseResponse<Trips>>() {
            @Override
            public void onResponse(Call<BaseResponse<Trips>> call, Response<BaseResponse<Trips>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Trips> res = response.body();
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
            public void onFailure(Call<BaseResponse<Trips>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });

    }

    public void updateFcm(String authorization, String fcm) {
        Call<BaseResponse> call = service.updateFcm(authorization, fcm);
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

    public void getTripDetail(String authorization, String tripId,
                              final CallBackWith<Trip> success,
                              final CallBack expired,
                              final CallBackWith<String> fail) {
        Call<BaseResponse<Trip>> call = service.getTripDetail(authorization, tripId);

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

    public void getUserInfo(String authorization, final CallBackWith<User> success,
                            final CallBack expired, final CallBackWith<String> fail) {

        Call<BaseResponse<User>> call = service.getUserInfo(authorization);
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

    public void createTrips(String authorization,
                            String groupName,
                            String arrive,
                            String depart,
                            String description,
                            int is_published,
                            int status,
                            int transfer,
                            final CallBack expired,
                            final CallBackWith<String> success,
                            final CallBackWith<String> fail) {
        Call<BaseResponse<String>> call = service.createTrips(authorization, groupName, arrive, depart, description,
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

    public void getFriendsList(String authorization, String fbAccessToken,
                               final CallBackWith<List<Friend>> success,
                               final CallBackWith<String> fail,
                               final CallBack expired) {

        Call<BaseResponse<List<Friend>>> call = service.getFriendsList(authorization, fbAccessToken);

        call.enqueue(new Callback<BaseResponse<List<Friend>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Friend>>> call, Response<BaseResponse<List<Friend>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<Friend>> res = response.body();
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
            public void onFailure(Call<BaseResponse<List<Friend>>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }


    public void getGroupDetail(String authorization, String groupId,
                               final CallBackWith<String> success,
                               final CallBackWith<String> fail,
                               final CallBack expired) {
        Call<BaseResponse<String>> call = service.getGroupDetail(authorization, groupId);
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
                              final CallBack expired,
                              final CallBack success,
                              final CallBackWith<String> fail) {
        Call<BaseResponse> call = service.updateProfile(authorization, body);
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

    public void getComments(String authorization, String tripId,
                            final CallBack expired,
                            final CallBackWith<Comments> success,
                            final CallBackWith<String> fail) {
        Call<BaseResponse<Comments>> call = service.getComments(authorization, tripId);
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

    public void getActivities(String authorization, String tripId,
                              final CallBack expired,
                              final CallBackWith<Timelines> success,
                              final CallBackWith<String> fail) {
        Call<BaseResponse<Timelines>> call = service.getActivities(authorization, tripId);
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
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public void updateView(String authorization, String tripId,
                           final CallBack expired,
                           final CallBack success,
                           final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.updateView(authorization, tripId);

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

    public void updateCover(String authorization, String tripId, String cover,
                            final CallBack expired,
                            final CallBack success,
                            final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.updateCover(authorization, tripId, cover);

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

    public void join(String authorization, String tripId, final CallBack expired,
                     final CallBack success, final CallBackWith<String> fail) {

        Call<BaseResponse<String>> call = service.join(authorization, tripId);
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

    public void getNotification(String authorization, final CallBack expired,
                                final CallBackWith<Notifications> success, final CallBackWith<String> fail) {

        Call<BaseResponse<Notifications>> call = service.getNoti(authorization);
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
}
