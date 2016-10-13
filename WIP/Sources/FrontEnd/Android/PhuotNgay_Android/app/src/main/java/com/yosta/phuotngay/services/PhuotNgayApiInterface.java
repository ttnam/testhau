package com.yosta.phuotngay.services;

import com.google.gson.JsonObject;
import com.yosta.phuotngay.model.RequestResponse;
import com.yosta.phuotngay.model.follower.Followers;
import com.yosta.phuotngay.model.photo.Photos;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Phuc-Hau Nguyen on 10/3/2016.
 */

public interface PhuotNgayApiInterface {

    String URL_SERVER = "http://phuotngay01.mycloud.by/";
    String API_LOGIN = "api/users/login";
    String API_USERS = "api/users";
    String API_ALBUM = "api/users/album";
    String API_VERIFIED = "api/users/token";
    String API_FOLLOWERS = "api/users/followers";
    String API_SUBSCRIBERS = "api/users/subscribers";


    @POST(API_LOGIN)
    @Headers("Content-Type: application/json")
    Call<String> apiLogin(@Body JsonObject baseUser);

    @POST(API_VERIFIED)
    @Headers("Content-Type: application/json")
    Call<Boolean> apiVerify(@Body JsonObject baseUser);

    @POST(API_ALBUM)
    @Headers("Content-Type: application/json")
    Call<Photos> apiAlbum(@Body JsonObject baseUser);

    @POST(API_FOLLOWERS)
    @Headers("Content-Type: application/json")
    Call<Followers> apiFollowers(@Body JsonObject baseUser);

    @POST(API_SUBSCRIBERS)
    @Headers("Content-Type: application/json")
    Call<Followers> apiSubscribers(@Body JsonObject baseUser);

    @PUT(API_USERS)
    @Headers("Content-Type: application/json")
    Call<RequestResponse> apiUpdate(@Body JsonObject baseUser);
}

