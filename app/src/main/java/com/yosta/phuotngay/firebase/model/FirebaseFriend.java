package com.yosta.phuotngay.firebase.model;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseFriend implements Serializable {

    private String name;

    public FirebaseFriend() {
        name = "BAC " + System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
