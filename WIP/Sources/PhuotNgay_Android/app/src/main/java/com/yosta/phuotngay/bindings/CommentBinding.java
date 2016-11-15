package com.yosta.phuotngay.bindings;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.models.comment.Comment;

public class CommentBinding extends BaseObservable {

    private Context mContext;
    private Comment comment;

    public CommentBinding(Context context, Comment comment) {
        this.comment = comment;
        this.mContext = context;
    }

    @Bindable
    public String getMessage() {
        return comment.getMessage();
    }

    @Bindable
    public String getAuthor() {
        return comment.getUserName();
    }

    @Bindable
    public String getCreatedTime() {
        return comment.getCreatedTime();
    }


    @Bindable
    public boolean isInternetConnected() {
        return AppUtils.isNetworkConnected(mContext);
    }
}
