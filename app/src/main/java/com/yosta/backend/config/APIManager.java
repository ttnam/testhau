package com.yosta.backend.config;

import android.util.Log;

import com.yosta.backend.response.CreateGroupResponse;
import com.yosta.backend.response.FriendsResponse;
import com.yosta.interfaces.CallBack;
import com.yosta.interfaces.CallBackFriendsParam;
import com.yosta.interfaces.CallBackLocationsParam;
import com.yosta.interfaces.CallBackStringParam;
import com.yosta.interfaces.CallBackTripParam;
import com.yosta.interfaces.CallBackTripsParam;
import com.yosta.backend.response.BaseResponse;
import com.yosta.backend.response.LocationResponse;
import com.yosta.backend.response.LoginResponse;
import com.yosta.backend.response.TripResponse;
import com.yosta.backend.response.TripsResponse;
import com.yosta.phuotngay.models.Friend;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
                        final CallBackStringParam success, final CallBackStringParam fail) {
        Call<LoginResponse> call = service.login(email, fbId, fireBaseUid, fcm);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    LoginResponse res = response.body();
                    if (res.IsSuccess()) {
                        success.run(res.getAuthorization());
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                fail.run(throwable.getMessage());
            }
        });
    }

    public void onUpdate(String authorization, @NotNull Map<String, String> data, final CallBack success, final CallBackStringParam fail) {

        Call<BaseResponse> call = service.updateProfile(authorization, data);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.code() == 200) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.IsSuccess()) {
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

    public void onGetLocation(String authorization, final CallBackLocationsParam success, final CallBackStringParam fail) {
        Call<LocationResponse> call = service.getLocations(authorization);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.code() == 200) {
                    LocationResponse locationResponse = response.body();
                    if (locationResponse.IsSuccess()) {
                        success.run(locationResponse.getLocations());
                    } else {
                        fail.run(locationResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }

    public void onGetTrips(String authorization, final CallBackTripsParam success, final CallBackStringParam fail) {
        Call<TripsResponse> call = service.getTrips(authorization);
        call.enqueue(new Callback<TripsResponse>() {
            @Override
            public void onResponse(Call<TripsResponse> call, Response<TripsResponse> response) {
                if (response.code() == 200) {
                    TripsResponse tripResponse = response.body();
                    if (tripResponse.IsSuccess()) {
                        success.run(tripResponse.getTrips());
                    } else {
                        fail.run(tripResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<TripsResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }

    public void onUpdateFcm(String authorization, String fcm) {
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

    public void onGetTripDetail(String authorization, String tripId, final CallBackTripParam success, final CallBackStringParam fail) {
        Call<TripResponse> call = service.getTripDetail(authorization, tripId);
        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (response.code() == 200) {

                    TripResponse tripResponse = response.body();
                    if (tripResponse.IsSuccess()) {
                        success.run(tripResponse.getTrip());
                    } else {
                        fail.run(tripResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }

    public void getUserInfo(String authorization, CallBack back) {
        Call<BaseResponse> call = service.getUserInfo(authorization);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }

    public void createGroup(String authorization, String name, String info, String members,
                            final CallBack expired,
                            final CallBackStringParam success,
                            final CallBackStringParam fail) {
        Call<CreateGroupResponse> call = service.createGroup(authorization, name, info, members);
        call.enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                if (response.code() == 200) {
                    CreateGroupResponse groupResponse = response.body();
                    if (groupResponse.IsSuccess()) {
                        success.run(groupResponse.getGroupId());
                    } else if (groupResponse.IsExpired()) {
                        expired.run();
                    } else {
                        fail.run(groupResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
                fail.run(throwable.getMessage());
            }
        });
    }

    public void getFriendsList(String authorization, String fbAccessToken,
                               final CallBackFriendsParam success, final CallBackStringParam fail,
                               final CallBack expired) {
        Call<FriendsResponse> call = service.getFriendsList(authorization, fbAccessToken);
        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                if (response.code() == 200) {
                    FriendsResponse friendsResponse = response.body();
                    if (friendsResponse.IsExpired()) {
                        expired.run();
                    } else if (friendsResponse.IsSuccess()) {
                        success.run(friendsResponse.getFriends());
                    } else {
                        fail.run(friendsResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
                fail.run(throwable.getMessage());
            }
        });
    }
}
