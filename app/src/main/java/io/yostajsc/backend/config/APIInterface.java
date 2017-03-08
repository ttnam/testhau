package io.yostajsc.backend.config;

import java.util.List;
import java.util.Map;

import io.yostajsc.backend.response.BaseResponse;
import io.yostajsc.izigo.models.user.User;
import io.yostajsc.izigo.models.user.Friend;
import io.yostajsc.izigo.models.base.Locations;
import io.yostajsc.izigo.models.trip.BaseTrip;
import io.yostajsc.izigo.models.trip.Trip;
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
    Call<BaseResponse<String>> login(
            @Field("email") String email, @Field("fbId") String fbId,
            @Field("firebaseUid") String firebaseUid, @Field("fcm") String fcm
    );

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateProfile(@Header("authen") String authorization,
                                     @FieldMap Map<String, String> fieldsFieldMap);

    @GET("api/location")
    Call<BaseResponse<Locations>> getLocations(@Header("authen") String authorization);

    @GET("api/trips")
    Call<BaseResponse<List<BaseTrip>>> getTrips(@Header("authen") String authorization);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateFcm(@Header("authen") String authorization, @Field("fcm") String fcm);

    @GET("api/user")
    Call<BaseResponse<User>> getUserInfo(@Header("authen") String authorization);

    @GET("api/trips/{id}")
    Call<BaseResponse<Trip>> getTripDetail(@Header("authen") String authorization, @Path("id") String tripId);

    @POST("api/group")
    @FormUrlEncoded
    Call<BaseResponse<String>> createGroup(@Header("authen") String authorization,
                                           @Field("name") String name,
                                           @Field("avatar") String avatar,
                                           @Field("info") String info,
                                           @Field("members") String members);

    @GET("api/user/friends")
    Call<BaseResponse<List<Friend>>> getFriendsList(
            @Header("authen") String authorization,
            @Header("x-access-token") String fbAccessToken);

    @GET("api/group/{id}")
    Call<BaseResponse<String>> getGroupDetail(@Header("authen") String authorization, @Path("id") String id);
}
