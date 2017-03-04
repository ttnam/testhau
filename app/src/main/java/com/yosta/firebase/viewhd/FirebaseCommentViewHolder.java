package com.yosta.firebase.viewhd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.firebase.model.FirebaseComment;
import com.yosta.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseCommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView mTxtUserName;

    @BindView(R.id.txt_content)
    TextView txtContent;

    @BindView(R.id.tV_time)
    TextView tVTime;

/*    @BindView(R.id.image_view)
    CircleImageView mAvatar;*/

    private Context mContext;

    public FirebaseCommentViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void onBind(FirebaseComment comment) {
        // Glide.with(mContext).load(comment.getAvatar()).error(R.drawable.ic_avatar).into(mAvatar);
        this.mTxtUserName.setText(comment.getUsername());
        this.txtContent.setText(comment.getContent());
        this.tVTime.setText(AppUtils.builder(mContext).getPastTimeGap(comment.time()));
    }

}
