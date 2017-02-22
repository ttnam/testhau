package com.yosta.phuotngay.services.api;

import com.yosta.phuotngay.services.response.BaseResponse;
import com.yosta.phuotngay.services.response.LocationResponse;
import com.yosta.phuotngay.services.response.LoginResponse;
import com.yosta.phuotngay.services.response.TripResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST("api/user/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("fbId") String fbId,
            @Field("firebaseUid") String firebaseUid
    );

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateProfile(@Header("authen") String authen, @FieldMap Map<String, String> fieldsFieldMap);

    @GET("/api/location")
    Call<LocationResponse> getLocations(@Header("authen") String authen);

    @GET("/api/trips")
    Call<TripResponse> getTrips(@Header("authen") String authen);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateFcm(@Header("authen") String authen,@Field("fcm") String fcm);
}
