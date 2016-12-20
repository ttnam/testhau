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

        int pluralsIds[] = {R.plurals.seconds, R.plurals.minutes, R.plurals.hours,
                R.plurals.days, R.plurals.months, R.plurals.years};

        int timeConst[] = {1000, 60, 60, 24, 30, 12};
        int timeValue = (int) (timeGap / timeConst[0]);

        Resources resources = mContext.getResources();
        String res = resources.getQuantityString(R.plurals.seconds, timeValue, timeValue);

        for (int i = 1; i < pluralsIds.length; ++i) {
            timeValue /= timeConst[i];
            if (timeValue != 0) {
                res = resources.getQuantityString(pluralsIds[i], timeValue, timeValue);
            }
        }
        return res;
    }
}
