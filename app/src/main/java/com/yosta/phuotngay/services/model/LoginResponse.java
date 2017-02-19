package com.yosta.phuotngay.services.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class LoginResponse {

    @SerializedName("responseCode")
    private int responseCode;

    @SerializedName("description")
    private String description;

    @SerializedName("data")
    private Data data;

    public int getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthen() {
        return data.getAuthen();
    }

    private class Data {
        @SerializedName("authen")
        private String authen;

        public String getAuthen() {
            return authen;
        }
    }
}
