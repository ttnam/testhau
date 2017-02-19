package com.yosta.phuotngay.services;

import com.yosta.phuotngay.models.user.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public interface IPhuotNgayService {

    @FormUrlEncoded
    @POST("/token")
    Call<AccessToken> getAccessToken(
            @Field("code") String code,
            @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("api/user/login")
    Call<PhuotNgayResponse> login(
            @Field("email") String email,
            @Field("fbId") String fbId,
            @Field("firebaseUid") String firebaseUid
    );

    @FormUrlEncoded
    @PUT("api/user/login")
    Call<PhuotNgayResponse> updateProfile(
            @Header("authen") String contentRange,
            @Field("email") String email,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("gender") String gender,
            @Field("avatar") String avatar
    );
}
