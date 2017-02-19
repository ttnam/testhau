package com.yosta.phuotngay.services;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class PhuotNgayResponse {

    private int responseCode;
    private String description;
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
        private String authen;

        public String getAuthen() {
            return authen;
        }
    }
}
