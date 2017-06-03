package io.yostajsc.sdk.api;

import java.util.List;
import java.util.Map;

import io.yostajsc.sdk.api.model.IgNotification;
import io.yostajsc.sdk.api.model.IgSuggestion;
import io.yostajsc.sdk.api.model.IgTimeline;
import io.yostajsc.sdk.api.model.user.IgFriend;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.sdk.api.model.user.IgUser;
import io.yostajsc.sdk.api.model.IgComment;
import io.yostajsc.sdk.api.model.token.IgToken;
import io.yostajsc.sdk.api.response.BaseResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */
interface IzigoApiInterface {


    @POST("user/login")
    @FormUrlEncoded
    Call<BaseResponse<IgToken>> login(@Header("fbToken") String fbToken, @FieldMap Map<String, String> fields);


    @PUT("user")
    @FormUrlEncoded
    Call<BaseResponse> updateUserInfo(@Header("authorization") String authorization, @FieldMap Map<String, String> fields);

    @GET("trips")
    Call<BaseResponse<List<IgTrip>>> getAllPublicTrips(@Header("authorization") String authorization);

    // type: 0 == admin, type 1 == not admin
    @GET("trips/own")
    Call<BaseResponse<List<IgTrip>>> getAllOwnTrips(@Header("authorization") String authorization, @Query("type") int type);

    @FormUrlEncoded
    @PUT("user")
    Call<BaseResponse> updateFcm(@Header("authorization") String authorization, @Field("fcm") String fcm);

    @GET("user")
    Call<BaseResponse<IgUser>> getUserInfo(@Header("authorization") String authorization);

    @GET("trips/{id}")
    Call<BaseResponse<IgTrip>> getTripDetail(@Header("authorization") String authorization,
                                             @Path("id") String tripId);

    @FormUrlEncoded
    @POST("trips")
    Call<BaseResponse<String>> addTrip(@Header("authorization") String authorization, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("trips/{id}/comment")
    Call<BaseResponse> addComment(@Header("authorization") String authorization,
                                  @Path("id") String tripId,
                                  @Field("content") String content);

    @PUT("trips/{id}/join")
    Call<BaseResponse<String>> join(@Header("authorization") String authorization, @Path("id") String tripId);

    @PUT("trips/{id}/view")
    Call<BaseResponse<String>> updateTripView(@Header("authorization") String authorization, @Path("id") String tripId);

    @GET("trips/{id}/members")
    Call<BaseResponse<List<IgFriend>>> getMembers(@Header("authorization") String authorization, @Path("id") String tripId);

    @GET("trips/{id}/comment")
    Call<BaseResponse<List<IgComment>>> getComments(@Header("authorization") String authorization, @Path("id") String tripId);

    @GET("trips/{id}/activity")
    Call<BaseResponse<List<IgTimeline>>> getActivities(@Header("authorization") String authorization, @Path("id") String tripId);

    @GET("user/friends")
    Call<BaseResponse<List<IgFriend>>> getFriendsList(@Header("authorization") String authorization, @Header("fbToken") String fbAccessToken);

    @GET("notification")
    Call<BaseResponse<List<IgNotification>>> getNotification(
            @Header("authorization") String authorization,
            @Query("type") String type);

    @GET("suggestion")
    Call<BaseResponse<List<IgSuggestion>>> getSuggestion(@Header("authorization") String authorization,
                                                         @Query("lat") double lat,
                                                         @Query("lng") double lng);

    @FormUrlEncoded
    @PUT("trips/{id}/accept")
    Call<BaseResponse<String>> accept(@Header("authorization") String authorization,
                                      @Path("id") String tripId,
                                      @Field("notiId") String notyId,
                                      @Field("verify") int verify);

    @FormUrlEncoded
    @PUT("trips/{id}/add")
    Call<BaseResponse<String>> addMember(@Header("authorization") String authorization,
                                         @Path("id") String tripId,
                                         @Field("fbId") String fbId);

    @FormUrlEncoded
    @PUT("trips/{id}/verify")
    Call<BaseResponse<String>> verify(@Header("authorization") String authorization,
                                      @Path("id") String tripId,
                                      @Field("notiId") String notyId,
                                      @Field("verify") int verify);

    @FormUrlEncoded
    @PUT("trips/{id}/kick")
    Call<BaseResponse<String>> kick(@Header("authorization") String authorization,
                                    @Path("id") String tripId,
                                    @Field("fbId") String fbId);

    @Multipart
    @POST("trips/{id}/album")
    Call<BaseResponse> uploadTripAlbum(
            @Header("authorization") String authorization, @Path("id") String tripId,
            @Part MultipartBody.Part[] images);

    /**
     * All of trip api here
     * Hack it if you want
     */
    @FormUrlEncoded
    @PUT("trips/{id}")
    Call<BaseResponse<String>> updateTrip(@Header("authorization") String authorization,
                                          @Path("id") String tripId,
                                          @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "trips/{tripId}/album", hasBody = true)
    Call<BaseResponse> deleteImage(@Header("authorization") String authorization,
                                   @Path("tripId") String tripId, @Field("id") String imageId);

    @Multipart
    @PUT("trips/{tripId}/cover")
    Call<BaseResponse> uploadTripCover(
            @Header("authorization") String authorization,
            @Path("tripId") String tripId,
            @Part MultipartBody.Part image);
}
