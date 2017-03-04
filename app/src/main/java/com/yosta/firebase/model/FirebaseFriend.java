package com.yosta.firebase.model;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseFriend implements Serializable {

    private long id;
    private String name;

    private FirebaseFriend(long id) {
        this.id = id;
    }

    public FirebaseFriend() {
        this(System.currentTimeMillis());
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

    public long getId() {
        return id;
    }
}
