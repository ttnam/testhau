package com.yosta.phuotngay.services;

import com.yosta.phuotngay.interfaces.CallBack;
import com.yosta.phuotngay.interfaces.CallBackStringParam;
import com.yosta.phuotngay.services.model.BaseResponse;
import com.yosta.phuotngay.services.model.LoginResponse;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class PhuotNgayService {

    public static String SERVER_DOMAIN = "http://phuotngay.jelasticlw.com.br/";

    private static Retrofit retrofit = null;
    private IPhuotNgayService service = null;
    private static OkHttpClient.Builder httpClient = null;
    private static PhuotNgayService mInstance = null;

    private PhuotNgayService() {
        httpClient = new OkHttpClient.Builder();
        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_DOMAIN)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IPhuotNgayService.class);
    }

    public static PhuotNgayService connect() {
        if (mInstance == null) {
            mInstance = new PhuotNgayService();
        }
        return mInstance;
    }

    public void onLogin(String email, String fbId, String firebaseUid,
                        final CallBackStringParam success, final CallBackStringParam fail) {
        Call<LoginResponse> call = service.login(email, fbId, firebaseUid);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    LoginResponse res = response.body();
                    if (res.getResponseCode() == 1) {
                        success.run(res.getAuthen());
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }

    public void onUpdate(String authen, @NotNull Map<String, String> data, final CallBack success, final CallBackStringParam fail) {

        Call<BaseResponse> call = service.updateProfile(authen, data);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.code() == 200) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getResponseCode() == 1) {
                        success.run();
                    } else {
                        fail.run(baseResponse.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }
}
