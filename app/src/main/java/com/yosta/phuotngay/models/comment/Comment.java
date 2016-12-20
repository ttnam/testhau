package com.yosta.phuotngay.models.comment;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.base.ViewBehavior;

import java.util.Calendar;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class Comment extends ViewBehavior {

    @SerializedName(value = "comment_id")
    private String mCommentId = null;

    @SerializedName(value = "created_time")
    private String mTime = null;

    @SerializedName(value = "message")
    private String mMessage = null;

    @SerializedName(value = "from")
    private String mUserId = null;

    @SerializedName(value = "user_name")
    private String mUserName = null;

    @SerializedName(value = "place")
    private String mPlaceId = null;

    public Comment() {
        this.mCommentId = null;
        this.mTime = null;
        this.mUserId = null;
        this.mPlaceId = null;
    }

    public Comment(String mMessage) {
        this.mMessage = mMessage;
    }


    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getCreatedTime() {
        mTime = Calendar.getInstance().getTime().toString();
        return mTime;
    }

    public String getUserName() {
        return mUserName;
    }


    public void setCreatedTime(String mTime) {
        this.mTime = mTime;
    }

    @Override
    public String toString() {
        return this.mMessage;
    }
}
