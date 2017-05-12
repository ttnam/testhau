package io.yostajsc.sdk.api;

import java.util.List;
import java.util.Map;

import io.yostajsc.sdk.model.IgNotification;
import io.yostajsc.sdk.model.Timeline;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.sdk.model.user.IgUser;
import io.yostajsc.sdk.model.IgComment;
import io.yostajsc.sdk.model.token.IgToken;
import io.yostajsc.sdk.response.BaseResponse;
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
interface IzigoApiInterface {

    @FormUrlEncoded
    @POST("api/user/login")
    Call<BaseResponse<IgToken>> login(@Header("fbToken") String fbToken,
                                      @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> updateUserInfo(@Header("authorization") String authorization,
                                      @FieldMap Map<String, String> fields);

    @GET("api/trips")
    Call<BaseResponse<List<IgTrip>>> apiGetAllPublicTrips(@Header("authorization") String authorization);

    @GET("api/trips/own")
    Call<BaseResponse<List<IgTrip>>> apiGetAllOwnTrips(@Header("authorization") String authorization);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> apiUpdateFcm(@Header("authorization") String authorization,
                                    @Field("fcm") String fcm);

    @GET("api/user")
    Call<BaseResponse<IgUser>> apiGetUserInfo(@Header("authorization") String authorization);

    @GET("api/trips/{id}")
    Call<BaseResponse<IgTrip>> apiGetTripDetail(@Header("authorization") String authorization,
                                                @Path("id") String tripId);

    @FormUrlEncoded
    @POST("api/trips")
    Call<BaseResponse<String>> addTrip(@Header("authorization") String authorization,
                                       @Field("name") String groupName,
                                       @Field("arrive") String arrive,
                                       @Field("depart") String depart,
                                       @Field("description") String description,
                                       @Field("is_published") int is_published,
                                       @Field("status") int status,
                                       @Field("transfer") int transfer);

    @FormUrlEncoded
    @POST("api/trips/{id}/comment")
    Call<BaseResponse> addComment(@Header("authorization") String authorization,
                                  @Path("id") String tripId,
                                  @Field("content") String content);

    @GET("api/user/friends")
    Call<BaseResponse<List<IgFriend>>> apiGetFriendsList(@Header("authorization") String authorization,
                                                         @Header("fbToken") String fbAccessToken);

    @GET("api/group/{id}")
    Call<BaseResponse<String>> apiGetGroupDetail(@Header("authorization") String authorization,
                                                 @Path("id") String id);

    @GET("api/trips/{id}/comment")
    Call<BaseResponse<List<IgComment>>> apiGetComments(@Header("authorization") String authorization,
                                                       @Path("id") String tripId);

    @GET("api/trips/{id}/activity")
    Call<BaseResponse<List<Timeline>>> apiGetActivities(@Header("authorization") String authorization,
                                                        @Path("id") String tripId);

    @GET("api/trips/{id}/members")
    Call<BaseResponse<List<IgFriend>>> apiGetMembers(@Header("authorization") String authorization,
                                                     @Path("id") String tripId);

    @PUT("api/trips/{id}/view")
    Call<BaseResponse<String>> apiUpdateView(@Header("authorization") String authorization,
                                             @Path("id") String tripId);

    @FormUrlEncoded
    @PUT("api/trips/{id}")
    Call<BaseResponse<String>> apiUpdateTripCover(@Header("authorization") String authorization,
                                                  @Path("id") String tripId,
                                                  @Field("cover") String cover);

    @FormUrlEncoded
    @PUT("api/trips/{id}")
    Call<BaseResponse<String>> changeName(@Header("authorization") String authorization,
                                          @Path("id") String tripId,
                                          @Field("name") String cover);

    @PUT("api/trips/{id}")
    @FormUrlEncoded
    Call<BaseResponse<String>> publish(@Header("authorization") String authorization,
                                       @Path("id") String tripId,
                                       @Field("is_published") String cover);

    @PUT("api/trips/{id}/join")
    Call<BaseResponse<String>> apiJoinGroup(@Header("authorization") String authorization,
                                            @Path("id") String tripId);

    @GET("api/notification")
    Call<BaseResponse<List<IgNotification>>> apiGetNotification(@Header("authorization") String authorization);

    @FormUrlEncoded
    @PUT("api/trips/{id}/apiAccept")
    Call<BaseResponse<String>> apiAccept(@Header("authorization") String authorization,
                                         @Path("id") String tripId,
                                         @Field("notiId") String notyId,
                                         @Field("apiVerify") int verify);

    @FormUrlEncoded
    @PUT("api/trips/{id}/add")
    Call<BaseResponse<String>> apiAddMember(@Header("authorization") String authorization,
                                            @Path("id") String tripId,
                                            @Field("fbId") String fbId);

    @FormUrlEncoded
    @PUT("api/trips/{id}/verify")
    Call<BaseResponse<String>> apiVerify(@Header("authorization") String authorization,
                                         @Path("id") String tripId,
                                         @Field("notiId") String notyId,
                                         @Field("apiVerify") int verify);

    @FormUrlEncoded
    @PUT("api/trips/{id}/kick")
    Call<BaseResponse<String>> apiKickMember(@Header("authorization") String authorization,
                                             @Path("id") String tripId,
                                             @Field("fbId") String fbId);
}
