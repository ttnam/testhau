package io.yostajsc.sdk.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nphau on 5/27/17.
 */

public class IgSuggestion implements Serializable {

    @SerializedName("id")
    private String mId;

    @SerializedName("link")
    private String mLink;

    @SerializedName("name")
    private String mName;

    @SerializedName("type")
    private String mType;

    @SerializedName("cover")
    private String mCover;

    @SerializedName("location")
    private String mLocation;

    @SerializedName("description")
    private String mDescription;

    public IgSuggestion() {
    }


    public String getId() {
        return mId;
    }

    public double getLat() {
        if (mLocation == null)
            return 0;
        return Double.parseDouble(mLocation.split(", ")[0]);
    }

    public double getLng() {
        if (mLocation == null)
            return 0;
        return Double.parseDouble(mLocation.split(", ")[1]);
    }

    public String getLink() {
        return mLink;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getCover() {
        return mCover;
    }

    public String getDescription() {
        return mDescription;
    }
}
