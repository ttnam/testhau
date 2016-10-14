package com.yosta.phuotngay.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.yosta.phuotngay.model.comment.Comment;

public class CommentViewModel extends BaseObservable {

    private Context context;
    private Comment comment;

    public CommentViewModel(Context context, Comment comment) {
        this.comment = comment;
        this.context = context;
    }

    public String getMessage() {

        return comment.getMessage();
    }
    public String getAuthor() {

        return comment.getUserName();
    }
    public String getCreatedTime() {

        return comment.getCreatedTime();
    }
}
