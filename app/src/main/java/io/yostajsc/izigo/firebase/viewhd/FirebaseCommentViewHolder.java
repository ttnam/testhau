package io.yostajsc.izigo.firebase.viewhd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.firebase.model.FirebaseComment;
import io.yostajsc.utils.AppUtils;

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
