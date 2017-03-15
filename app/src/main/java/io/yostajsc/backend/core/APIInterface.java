package io.yostajsc.backend.core;

import java.util.List;
import java.util.Map;

import io.yostajsc.backend.response.BaseResponse;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.izigo.models.trip.Trips;
import io.yostajsc.izigo.models.user.User;
import io.yostajsc.izigo.models.user.Friend;
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

    @GET("api/trips")
    Call<BaseResponse<Trips>> getTrips(@Header("authen") String authorization);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateFcm(@Header("authen") String authorization, @Field("fcm") String fcm);

    @GET("api/user")
    Call<BaseResponse<User>> getUserInfo(@Header("authen") String authorization);

    @GET("api/trips/{id}")
    Call<BaseResponse<Trip>> getTripDetail(@Header("authen") String authorization, @Path("id") String tripId);

    @POST("api/trips")
    @FormUrlEncoded
    Call<BaseResponse<String>> createTrips(@Header("authen") String authorization,
                                           @Field("name") String groupName,
                                           @Field("arrive") String arrive,
                                           @Field("depart") String depart,
                                           @Field("description") String description,
                                           @Field("is_published") int is_published,
                                           @Field("status") int status,
                                           @Field("transfer") int transfer);

    @GET("api/user/friends")
    Call<BaseResponse<List<Friend>>> getFriendsList(
            @Header("authen") String authorization,
            @Header("x-access-token") String fbAccessToken);

    @GET("api/group/{id}")
    Call<BaseResponse<String>> getGroupDetail(@Header("authen") String authorization, @Path("id") String id);

    @GET("api/trips/{id}/comment")
    Call<BaseResponse<Comments>> getComments(@Header("authen") String authorization, @Path("id") String tripId);

    @GET("api/trips/own")
    Call<BaseResponse<Trips>> getOwnTrips(@Header("authen") String authorization);

    @GET("api/trips/{id}/activity")
    Call<BaseResponse<Timelines>> getActivities(@Header("authen") String authorization, @Path("id") String tripId);

    @PUT("api/trips/{id}/view")
    Call<BaseResponse<String>> updateView(@Header("authen") String authorization, @Path("id") String tripId);

    @PUT("api/trips/{id}")
    @FormUrlEncoded
    Call<BaseResponse<String>> updateCover(@Header("authen") String authorization,
                                           @Path("id") String tripId, @Field("cover") String cover);

    @PUT("api/trips/{id}/join")
    Call<BaseResponse<String>> join(@Header("authen") String authorization,
                                    @Path("id") String tripId);

}
