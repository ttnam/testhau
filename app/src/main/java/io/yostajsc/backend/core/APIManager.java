package io.yostajsc.backend.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import io.yostajsc.backend.response.BaseResponse;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.models.trip.Trips;
import io.yostajsc.izigo.models.user.Friend;
import io.yostajsc.izigo.models.user.User;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class APIManager {

    private static final String TAG = APIManager.class.getSimpleName();

    private static String SERVER_DOMAIN = "http://phuotngay.jelasticlw.com.br/";

    private APIInterface service = null;
    private static APIManager mInstance = null;

    private APIManager() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_DOMAIN)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(APIInterface.class);
    }

    public static APIManager connect() {
        if (mInstance == null) {
            mInstance = new APIManager();
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

    public void createGroup(String authorization, String name, String avatar, String info, String members,
                            final CallBack expired,
                            final CallBackWith<String> success,
                            final CallBackWith<String> fail) {
        Call<BaseResponse<String>> call = service.createGroup(authorization, name, avatar, info, members);
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
}
