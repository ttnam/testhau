package com.yosta.backend.response;

/**
 * Created by Phuc-Hau Nguyen on 2/19/2017.
 */

public class LoginResponse extends BaseResponse {
    private String data;
    public String getAuthorization() {
        return data;
    }
}
