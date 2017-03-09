package io.yostajsc.izigo.models.photo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Phuc-Hau Nguyen on 3/9/2017.
 */

public class BasePhotoInfo extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private String mId;


    @SerializedName("url")
    private String mUrl;



    public String getUrl() {
        return this.mUrl;
    }
}
