package com.yosta.phuotngay.services.api;

import com.yosta.phuotngay.services.response.BaseResponse;
import com.yosta.phuotngay.services.response.LocationResponse;
import com.yosta.phuotngay.services.response.LoginResponse;
import com.yosta.phuotngay.services.response.TripResponse;
import com.yosta.phuotngay.services.response.TripsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST("api/user/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("fbId") String fbId,
            @Field("firebaseUid") String firebaseUid,
            @Field("fcm") String fcm
    );

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateProfile(@Header("authen") String authorization, @FieldMap Map<String, String> fieldsFieldMap);

    @GET("/api/location")
    Call<LocationResponse> getLocations(@Header("authen") String authorization);

    @GET("/api/trips")
    Call<TripsResponse> getTrips(@Header("authen") String authorization);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateFcm(@Header("authen") String authorization, @Field("fcm") String fcm);

    @GET("api/user")
    Call<BaseResponse> getUserInfo(@Header("authen") String authorization);

    @GET("/api/trips/{id}")
    Call<TripResponse> getTripDetail(@Header("authen") String authorization, @Path("id") String tripId);

}
