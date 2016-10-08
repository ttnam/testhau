package com.yosta.phuotngay.config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Phuc-Hau Nguyen on 9/12/2016.
 */
public interface ApiConfig {

    String URL_SERVER = "";
    String URL_LOGIN = "";

    String URL_FB = "https://graph.facebook.com";
    String URL_FB_VERSION = "/v2.5/";
    @GET(URL_FB + URL_FB_VERSION)
    Call<Void> GetFacebookUserInfo(
            @Query("name") String event_name);

}
