package io.yostajsc.sdk.api.model.trip;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nphau on 3/20/17.
 */

public class BaseTripInfo implements Serializable {

    @SerializedName("tripId")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("cover")
    private String mCover;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getCover() {
        return mCover;
    }
}
