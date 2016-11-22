package com.yosta.phuotngay.services;

import com.yosta.phuotngay.models.user.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public interface IPhuotNgayService {
    String API_BASE_URL = "http://your.api-base.url";

    @FormUrlEncoded
    @POST("/token")
    Call<AccessToken> getAccessToken(
            @Field("code") String code,
            @Field("grant_type") String grantType);
}
