package com.yosta.phuotngay.firebase.viewhd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseActivity;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseActivityViewHolder extends RecyclerView.ViewHolder {


    private TextView mTxtContent, mTxtTime, mTvHour;

    public FirebaseActivityViewHolder(View itemView) {
        super(itemView);
        this.mTxtContent = (TextView) itemView.findViewById(R.id.txt_content);
        this.mTxtTime = (TextView) itemView.findViewById(R.id.tv_time);
        this.mTvHour = (TextView) itemView.findViewById(R.id.tV_hour);
    }

    public void onBind(FirebaseActivity activity) {
        this.mTxtTime.setText(activity.getDate());
        this.mTxtContent.setText(activity.getContent());
        this.mTvHour.setText(activity.getHour());
    }
}
