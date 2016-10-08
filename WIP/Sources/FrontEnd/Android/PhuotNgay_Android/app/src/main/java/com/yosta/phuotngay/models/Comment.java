package com.yosta.phuotngay.models;

import com.yosta.phuotngay.models.base.ModelBehavior;

import java.util.Calendar;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class Comment extends ModelBehavior {

    private String mContent = null;
    private String mTime = null;

    public Comment() {
        mTime = Calendar.getInstance().getTime().toString();
    }

    public Comment(String content) {
        this.mContent = content;
        this.mTime = Calendar.getInstance().getTime().toString();
    }


    public String getContent() {
        return this.mContent;
    }

    public String getTime() {
        return this.mTime;
    }
}
