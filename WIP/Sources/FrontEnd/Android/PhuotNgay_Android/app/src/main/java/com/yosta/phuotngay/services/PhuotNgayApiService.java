package com.yosta.phuotngay.services;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yosta.phuotngay.globalapp.SharedPresUtils;
import com.yosta.phuotngay.model.BaseUser;
import com.yosta.phuotngay.model.RequestResponse;
import com.yosta.phuotngay.model.follower.Followers;
import com.yosta.phuotngay.model.photo.Photos;
import com.yosta.phuotngay.model.user.Token;
import com.yosta.phuotngay.model.user.User;

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
    private String jsonSamples = null;

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
        jsonSamples = mGson.toJson(new BaseUser(username, password));
        JsonObject object = mParser.parse(jsonSamples).getAsJsonObject();
        Call<String> userCall = apiInterface.apiLogin(object);
        userCall.enqueue(callback);
    }

    public void ApiVerify(Token token, Callback<Boolean> callback) {
        jsonSamples = mGson.toJson(token);
        JsonObject object = mParser.parse(jsonSamples).getAsJsonObject();
        Call<Boolean> apiVerifyCall = apiInterface.apiVerify(object);
        apiVerifyCall.enqueue(callback);
    }

    public void ApiAlbum(Token token, Callback<Photos> callback) {
        jsonSamples = mGson.toJson(token);
        JsonObject object = mParser.parse(jsonSamples).getAsJsonObject();
        Call<Photos> imagesCall = apiInterface.apiAlbum(object);
        imagesCall.enqueue(callback);
    }

    public void ApiFollowers(Token token, Callback<Followers> callback) {
        jsonSamples = mGson.toJson(token);
        JsonObject object = mParser.parse(jsonSamples).getAsJsonObject();
        Call<Followers> followersCall = apiInterface.apiFollowers(object);
        followersCall.enqueue(callback);
    }

    public void ApiSubscribers(Token token, Callback<Followers> callback) {
        jsonSamples = mGson.toJson(token);
        JsonObject object = mParser.parse(jsonSamples).getAsJsonObject();
        Call<Followers> subscribersCall = apiInterface.apiSubscribers(object);
        subscribersCall.enqueue(callback);
    }

    public void ApiUpdate(User user, Callback<RequestResponse> callback) {
        jsonSamples = mGson.toJson(user);
        JsonObject object = mParser.parse(jsonSamples).getAsJsonObject();
        Call<RequestResponse> update = apiInterface.apiUpdate(object);
        update.enqueue(callback);
    }
}

