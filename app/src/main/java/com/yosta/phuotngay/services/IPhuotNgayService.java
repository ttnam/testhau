package com.yosta.phuotngay.services;

import com.yosta.phuotngay.services.model.BaseResponse;
import com.yosta.phuotngay.services.model.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public interface IPhuotNgayService {

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
}
