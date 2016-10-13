package com.yosta.phuotngay.helpers.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yosta.circleimageview.CircleImageView;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.globalapp.AppUtils;
import com.yosta.phuotngay.model.comment.Comment;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class CommentHolder extends RecyclerView.ViewHolder {

    private Context context = null;
    private CircleImageView imgAvatar = null;
    private View onlineStatus = null;
    private AppCompatTextView txtAuthor = null, txtStatus = null, txtTime = null;


    public CommentHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        this.imgAvatar = (CircleImageView) itemView.findViewById(R.id.image);
        this.txtTime = (AppCompatTextView) itemView.findViewById(R.id.textView_time);
        this.txtAuthor = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        this.onlineStatus = itemView.findViewById(R.id.online_status);
        this.txtStatus = (AppCompatTextView) itemView.findViewById(R.id.textView_description);
    }

    public void onSetEvent(Comment comment) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onSetContent(Comment comment) {
        this.txtAuthor.setText("Phuc Henry");
        this.txtStatus.setText(comment.getMessage());
        if (AppUtils.isNetworkConnected(this.context)) {
            this.onlineStatus.setVisibility(View.GONE);
            this.txtTime.setText(comment.getCreatedTime());
        } else {
            this.onlineStatus.setVisibility(View.VISIBLE);
            this.txtTime.setText("Will appear when online");
        }
    }
}
