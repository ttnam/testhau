package com.yosta.phuotngay.services;

import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yosta.phuotngay.helpers.SharedPresUtils;
import com.yosta.phuotngay.models.BaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Phuc-Hau Nguyen on 10/3/2016.
 */

public class PhuotNgayApiService {

    private static Activity mActivity = null;
    private static PhuotNgayApiService ourInstance = null;
    private static SharedPresUtils presUtils = null;
    private PhuotNgayApiInterface apiInterface = null;

    private Gson mGson = null;
    private JsonParser mParser = null;

    private PhuotNgayApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PhuotNgayApiInterface.URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(PhuotNgayApiInterface.class);

        mGson = new Gson();
        mParser = new JsonParser();
    }

    public static PhuotNgayApiService getInstance(Activity activity) {

        mActivity = activity;

        if (ourInstance == null) {
            ourInstance = new PhuotNgayApiService();
        }
        if (presUtils == null) {
            presUtils = new SharedPresUtils(mActivity);
        }

        return ourInstance;
    }

    public void ApiLogin(String username, String password, Callback<String> callback) {
        String json = mGson.toJson(new BaseUser(username, password));
        JsonObject object = mParser.parse(json).getAsJsonObject();
        Call<String> userCall = apiInterface.apiLogin(object);
        userCall.enqueue(callback);
    }
}
