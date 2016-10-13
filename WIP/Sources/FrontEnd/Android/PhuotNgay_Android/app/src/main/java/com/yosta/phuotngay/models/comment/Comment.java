package com.yosta.phuotngay.models.comment;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.base.ModelBehavior;

import java.util.Calendar;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class Comment extends ModelBehavior {

    @SerializedName(value = "comment_id")
    private String mCommentId = null;

    @SerializedName(value = "created_time")
    private String mTime = null;

    @SerializedName(value = "from")
    private String mUserId = null;

    @SerializedName(value = "place")
    private String mPlaceId = null;

    public Comment() {
    }
}
