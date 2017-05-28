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
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    Call<BaseResponse<List<IgTrip>>> apiGetAllPublicTrips(@Header("authorization") String authorization);

    // type: 0 == admin, type 1 == not admin
    @GET("trips/own")
    Call<BaseResponse<List<IgTrip>>> apiGetAllOwnTrips(@Header("authorization") String authorization, @Query("type") int type);

    @FormUrlEncoded
    @PUT("user")
    Call<BaseResponse> apiUpdateFcm(@Header("authorization") String authorization, @Field("fcm") String fcm);

    @GET("user")
    Call<BaseResponse<IgUser>> apiGetUserInfo(@Header("authorization") String authorization);

    @GET("trips/{id}")
    Call<BaseResponse<IgTrip>> apiGetTripDetail(@Header("authorization") String authorization,
                                                @Path("id") String tripId);

    @FormUrlEncoded
    @POST("trips")
    Call<BaseResponse<String>> addTrip(@Header("authorization") String authorization,
                                       @Field("name") String groupName,
                                       @Field("arrive") String arrive,
                                       @Field("depart") String depart,
                                       @Field("description") String description,
                                       @Field("transfer") int transfer);

    @FormUrlEncoded
    @POST("trips/{id}/comment")
    Call<BaseResponse> addComment(@Header("authorization") String authorization,
                                  @Path("id") String tripId,
                                  @Field("content") String content);

    @GET("user/friends")
    Call<BaseResponse<List<IgFriend>>> getFriendsList(@Header("authorization") String authorization,
                                                      @Header("fbToken") String fbAccessToken);

    @GET("group/{id}")
    Call<BaseResponse<String>> apiGetGroupDetail(@Header("authorization") String authorization,
                                                 @Path("id") String id);

    @GET("trips/{id}/comment")
    Call<BaseResponse<List<IgComment>>> apiGetComments(@Header("authorization") String authorization,
                                                       @Path("id") String tripId);

    @GET("trips/{id}/activity")
    Call<BaseResponse<List<IgTimeline>>> apiGetActivities(@Header("authorization") String authorization,
                                                          @Path("id") String tripId);

    @GET("trips/{id}/members")
    Call<BaseResponse<List<IgFriend>>> getMembers(@Header("authorization") String authorization,
                                                  @Path("id") String tripId);

    @PUT("trips/{id}/view")
    Call<BaseResponse<String>> apiUpdateView(@Header("authorization") String authorization,
                                             @Path("id") String tripId);

    @FormUrlEncoded
    @PUT("trips/{id}")
    Call<BaseResponse<String>> apiUpdateTripCover(@Header("authorization") String authorization,
                                                  @Path("id") String tripId,
                                                  @Field("cover") String cover);

    @FormUrlEncoded
    @PUT("trips/{id}")
    Call<BaseResponse<String>> changeName(@Header("authorization") String authorization,
                                          @Path("id") String tripId,
                                          @Field("name") String cover);

    @FormUrlEncoded
    @PUT("trips/{id}")
    Call<BaseResponse<String>> changeStatus(@Header("authorization") String authorization,
                                            @Path("id") String tripId,
                                            @Field("status") String status);

    @PUT("trips/{id}")
    @FormUrlEncoded
    Call<BaseResponse<String>> publish(@Header("authorization") String authorization,
                                       @Path("id") String tripId,
                                       @Field("is_published") String publish);

    @PUT("trips/{id}/join")
    Call<BaseResponse<String>> join(@Header("authorization") String authorization,
                                    @Path("id") String tripId);

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
    Call<BaseResponse<String>> apiAddMember(@Header("authorization") String authorization,
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
    Call<BaseResponse<String>> apiKickMember(@Header("authorization") String authorization,
                                             @Path("id") String tripId,
                                             @Field("fbId") String fbId);

    @Multipart
    @POST("trips/{id}/album")
    Call<BaseResponse> uploadImages(
            @Header("authorization") String authorization,
            @Path("id") String tripId,
            @Part MultipartBody.Part[] images);

    @FormUrlEncoded
    @DELETE("trips/{triId}/album")
    Call<BaseResponse> deleteImage(@Header("authorization") String authorization,
                                   @Path("triId") String tripId, @Field("id") String imageId);

    @Multipart
    @POST("trips/{tripId}/cover")
    Call<BaseResponse> uploadCover(
            @Header("authorization") String authorization,
            @Part("tripId") RequestBody tripId,
            @Part MultipartBody.Part image);
}
