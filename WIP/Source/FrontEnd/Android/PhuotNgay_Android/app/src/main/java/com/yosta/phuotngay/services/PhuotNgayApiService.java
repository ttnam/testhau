package com.yosta.phuotngay.services;

import android.content.Context;

import com.yosta.phuotngay.helpers.SharedPresUtils;

/**
 * Created by Phuc-Hau Nguyen on 10/3/2016.
 */

public class PhuotNgayApiService {

    private Context context = null;
    private static PhuotNgayApiService ourInstance = null;
    private static SharedPresUtils presUtils = null;

    private PhuotNgayApiService() {
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EventHubAPI.URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        eventHubAPI = retrofit.create(EventHubAPI.class);*/
    }

    public PhuotNgayApiService getInstance(Context context) {

        this.context = context;

        if (ourInstance == null) {
            ourInstance = new PhuotNgayApiService();
        }
        if (presUtils == null) {
            presUtils = new SharedPresUtils(context);
        }

        return ourInstance;
    }
}
