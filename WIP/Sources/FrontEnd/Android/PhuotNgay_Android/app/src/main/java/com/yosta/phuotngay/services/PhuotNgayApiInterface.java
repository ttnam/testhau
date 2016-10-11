package com.yosta.phuotngay.services;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Phuc-Hau Nguyen on 10/3/2016.
 */

public interface PhuotNgayApiInterface {

    String URL_SERVER = "http://phuotngay01.mycloud.by/";
    String API_LOGIN = "api/users/login";

    @POST(API_LOGIN)
    @Headers("Content-Type: application/json")
    Call<String> apiLogin(@Body JsonObject baseUser);

}

