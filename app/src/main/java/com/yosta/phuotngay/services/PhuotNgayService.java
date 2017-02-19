package com.yosta.phuotngay.services;

import com.yosta.phuotngay.interfaces.CallBack;
import com.yosta.phuotngay.interfaces.CallBackStringParam;

import org.jetbrains.annotations.NotNull;

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

    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public void onLogin(String email, String fbId, String firebaseUid,
                        final CallBackStringParam success, final CallBackStringParam fail) {
        Call<PhuotNgayResponse> call = service.login(email, fbId, firebaseUid);
        call.enqueue(new Callback<PhuotNgayResponse>() {
            @Override
            public void onResponse(Call<PhuotNgayResponse> call, Response<PhuotNgayResponse> response) {
                if (response.code() == 200) {
                    PhuotNgayResponse res = response.body();
                    if (res.getResponseCode() == 1) {
                        success.run(res.getAuthen());
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<PhuotNgayResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }

    public void onUpdate(@NotNull String authen, @NotNull String email, @NotNull String firstName, @NotNull String lastName,
                         @NotNull String gender, @NotNull String avatar, final CallBack success, final CallBackStringParam fail) {

        Call<PhuotNgayResponse> call = service.updateProfile(authen, email, firstName, lastName, gender, avatar);
        call.enqueue(new Callback<PhuotNgayResponse>() {
            @Override
            public void onResponse(Call<PhuotNgayResponse> call, Response<PhuotNgayResponse> response) {
                if (response.code() == 200) {
                    PhuotNgayResponse res = response.body();
                    if (res.getResponseCode() == 1) {
                        success.run();
                    } else {
                        fail.run(res.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<PhuotNgayResponse> call, Throwable t) {
                fail.run(t.getMessage());
            }
        });
    }
}
