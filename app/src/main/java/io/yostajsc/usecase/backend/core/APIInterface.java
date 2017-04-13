package io.yostajsc.usecase.backend.core;

import java.util.Map;

import io.yostajsc.izigo.models.user.Authorization;
import io.yostajsc.usecase.realm.user.FriendsRealm;
import io.yostajsc.usecase.backend.response.BaseResponse;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.izigo.models.notification.Notifications;
import io.yostajsc.izigo.models.user.User;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.usecase.realm.trip.OwnTrips;
import io.yostajsc.usecase.realm.trip.PublicTrips;
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
interface APIInterface {

    @FormUrlEncoded
    @POST("api/user/login")
    Call<BaseResponse<Authorization>> apiLogin(@Header("fbToken") String fbToken,
                                               @Field("email") String email,
                                               @Field("fbId") String fbId,
                                               @Field("firebaseUid") String firebaseUid,
                                               @Field("fcm") String fcm);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> apiUpdateUserInfo(@Header("authorization") String authorization,
                                         @FieldMap Map<String, String> fieldsFieldMap);

    @GET("api/trips")
    Call<BaseResponse<PublicTrips>> apiGetAllPublicTrips(@Header("authorization") String authorization);

    @GET("api/trips/own")
    Call<BaseResponse<OwnTrips>> apiGetAllOwnTrips(@Header("authorization") String authorization);

    @FormUrlEncoded
    @PUT("api/user")
    Call<BaseResponse> apiUpdateFcm(@Header("authorization") String authorization,
                                    @Field("fcm") String fcm);

    @GET("api/user")
    Call<BaseResponse<User>> apiGetUserInfo(@Header("authorization") String authorization);

    @GET("api/trips/{id}")
    Call<BaseResponse<Trip>> apiGetTripDetail(@Header("authorization") String authorization,
                                              @Path("id") String tripId);

    @FormUrlEncoded
    @POST("api/trips")
    Call<BaseResponse<String>> apiCreateTrips(@Header("authorization") String authorization,
                                              @Field("name") String groupName,
                                              @Field("arrive") String arrive,
                                              @Field("depart") String depart,
                                              @Field("description") String description,
                                              @Field("is_published") int is_published,
                                              @Field("status") int status,
                                              @Field("transfer") int transfer);

    @GET("api/user/friends")
    Call<BaseResponse<FriendsRealm>> apiGetFriendsList(@Header("authorization") String authorization,
                                                       @Header("fbToken") String fbAccessToken);

    @GET("api/group/{id}")
    Call<BaseResponse<String>> apiGetGroupDetail(@Header("authorization") String authorization,
                                                 @Path("id") String id);

    @GET("api/trips/{id}/comment")
    Call<BaseResponse<Comments>> apiGetComments(@Header("authorization") String authorization,
                                                @Path("id") String tripId);

    @GET("api/trips/{id}/activity")
    Call<BaseResponse<Timelines>> apiGetActivities(@Header("authorization") String authorization,
                                                   @Path("id") String tripId);

    @GET("api/trips/{id}/members")
    Call<BaseResponse<FriendsRealm>> apiGetMembers(@Header("authorization") String authorization,
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
    Call<BaseResponse<String>> apiUpdateTripName(@Header("authorization") String authorization,
                                                 @Path("id") String tripId,
                                                 @Field("name") String cover);

    @PUT("api/trips/{id}")
    @FormUrlEncoded
    Call<BaseResponse<String>> apiUpdateTripStatus(@Header("authorization") String authorization,
                                                   @Path("id") String tripId,
                                                   @Field("is_published") String cover);

    @PUT("api/trips/{id}/apiJoinGroup")
    Call<BaseResponse<String>> apiJoinGroup(@Header("authorization") String authorization,
                                            @Path("id") String tripId);

    @GET("api/notification")
    Call<BaseResponse<Notifications>> apiGetNotification(@Header("authorization") String authorization);

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
    @PUT("api/trips/{id}/apiVerify")
    Call<BaseResponse<String>> apiVerify(@Header("authorization") String authorization,
                                         @Path("id") String tripId,
                                         @Field("notiId") String notyId,
                                         @Field("apiVerify") int verify);

    @FormUrlEncoded
    @PUT("api/trips/{id}/apiKickMember")
    Call<BaseResponse<String>> apiKickMember(@Header("authorization") String authorization,
                                             @Path("id") String tripId,
                                             @Field("fbId") String fbId);
}
