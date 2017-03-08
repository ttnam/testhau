package io.yostajsc.backend.config;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.Map;

import io.yostajsc.backend.response.BaseResponse;
import io.yostajsc.izigo.interfaces.CallBack;
import io.yostajsc.izigo.interfaces.CallBackParam;
import io.yostajsc.izigo.models.Friend;
import io.yostajsc.izigo.models.User;
import io.yostajsc.izigo.models.base.Locations;
import io.yostajsc.izigo.models.trip.BaseTrip;
import io.yostajsc.izigo.models.trip.Trip;
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

    public static String SERVER_DOMAIN = "http://phuotngay.jelasticlw.com.br/";

    private static Retrofit retrofit = null;
    private APIInterface service = null;
    private static OkHttpClient.Builder httpClient = null;
    private static APIManager mInstance = null;

    private APIManager() {
        httpClient = new OkHttpClient.Builder();

        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_DOMAIN)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
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
                        final CallBackParam<String> success, final CallBackParam<String> fail) {

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
                fail.run(throwable.getMessage());
            }
        });
    }

    public void onUpdate(String authorization, @NonNull Map<String, String> data,
                         final CallBack success, final CallBackParam<String> fail) {

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
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }

    public void getLocation(String authorization,
                            final CallBackParam<Locations> success,
                            final CallBackParam<String> fail) {

        Call<BaseResponse<Locations>> call = service.getLocations(authorization);

        call.enqueue(new Callback<BaseResponse<Locations>>() {
            @Override
            public void onResponse(Call<BaseResponse<Locations>> call, Response<BaseResponse<Locations>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Locations> res = response.body();
                    if (res.isSuccessful()) {
                        success.run(res.data());
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Locations>> call, Throwable throwable) {
                fail.run(throwable.getMessage());
            }
        });
    }

    public void onGetTrips(String authorization,
                           final CallBackParam<List<BaseTrip>> success,
                           final CallBackParam<String> fail) {
        Call<BaseResponse<List<BaseTrip>>> call = service.getTrips(authorization);
        call.enqueue(new Callback<BaseResponse<List<BaseTrip>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<BaseTrip>>> call, Response<BaseResponse<List<BaseTrip>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<BaseTrip>> res = response.body();
                    if (res.isSuccessful()) {
                        success.run(res.data());
                    } else {
                        fail.run(res.getDescription());
                    }

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<BaseTrip>>> call, Throwable t) {

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
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    public void getTripDetail(String authorization, String tripId,
                              final CallBackParam<Trip> success,
                              final CallBackParam<String> fail) {
        Call<BaseResponse<Trip>> call = service.getTripDetail(authorization, tripId);

        call.enqueue(new Callback<BaseResponse<Trip>>() {
            @Override
            public void onResponse(Call<BaseResponse<Trip>> call, Response<BaseResponse<Trip>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Trip> res = response.body();
                    if (res.isSuccessful()) {
                        success.run(res.data());
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Trip>> call, Throwable t) {

            }
        });
    }

    public void getUserInfo(String authorization, final CallBackParam<User> success,
                            final CallBack expired, final CallBackParam<String> fail) {

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
                fail.run(throwable.getMessage());
            }
        });

    }

    public void createGroup(String authorization, String name, String avatar, String info, String members,
                            final CallBack expired,
                            final CallBackParam<String> success,
                            final CallBackParam<String> fail) {
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
                fail.run(throwable.getMessage());
            }
        });
    }

    public void getFriendsList(String authorization, String fbAccessToken,
                               final CallBackParam<List<Friend>> success,
                               final CallBackParam<String> fail,
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
            public void onFailure(Call<BaseResponse<List<Friend>>> call, Throwable t) {

            }
        });
    }


    public void getGroupDetail(String authorization, String groupId,
                               final CallBackParam<String> success,
                               final CallBackParam<String> fail,
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
                fail.run(throwable.getMessage());
            }
        });
    }

    public void updateProfile(String authorization, Map<String, String> body,
                              final CallBack expired,
                              final CallBack success,
                              final CallBackParam<String> fail) {
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
                fail.run(throwable.getMessage());
            }
        });
    }
}
