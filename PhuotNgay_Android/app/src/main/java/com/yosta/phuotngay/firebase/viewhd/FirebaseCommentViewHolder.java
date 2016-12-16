package com.yosta.phuotngay.firebase.viewhd;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseComment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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

    @BindView(R.id.image_view)
    CircleImageView mAvatar;

    private Context mContext;

    public FirebaseCommentViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void onBind(FirebaseComment comment) {
        Glide.with(mContext).load(comment.getAvatar()).error(R.drawable.ic_launcher).into(mAvatar);
        this.mTxtUserName.setText(comment.getUsername());
        this.txtContent.setText(comment.getContent());
        this.tVTime.setText(onGetTimeGap(comment.getTime()));
    }

    private String onGetTimeGap(long timeGap) {

        long second = timeGap / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        long day = hour / 24;
        long month = day / 12;

        Resources resources = mContext.getResources();
        String res;

        if (month == 0) {
            if (day == 0) {
                if (hour == 0) {
                    if (minute == 0) {
                        res = resources.getQuantityString(R.plurals.seconds, (int) second, (int) second);
                    } else {
                        res = resources.getQuantityString(R.plurals.minutes, (int) minute, (int) minute);
                    }
                } else {
                    res = resources.getQuantityString(R.plurals.hours, (int) hour, (int) hour);
                }
            } else {
                res = resources.getQuantityString(R.plurals.days, (int) day, (int) day);
            }
        } else {
            res = resources.getQuantityString(R.plurals.months, (int) month, (int) month);
        }

        return res;
    }
}
