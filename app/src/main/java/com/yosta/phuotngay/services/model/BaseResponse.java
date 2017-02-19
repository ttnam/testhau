package com.yosta.phuotngay.services.model;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class BaseResponse {

    private int responseCode;
    private String description;
    private String data;

    public int getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
