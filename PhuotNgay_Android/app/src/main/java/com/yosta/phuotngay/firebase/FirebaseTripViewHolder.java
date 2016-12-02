package com.yosta.phuotngay.firebase;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.app.DatetimeUtils;
import com.yosta.phuotngay.models.trip.FirebaseTrip;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTripViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private TextView mTvTitle, mTvCreatedTime;
    private AppCompatImageView mImageView;

    public FirebaseTripViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        this.mTvTitle = (TextView) itemView.findViewById(R.id.text_view);
        this.mTvCreatedTime = (TextView) itemView.findViewById(R.id.tv_created_time);
        this.mImageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);

    }

    public void onBind(FirebaseTrip trip) {
        this.mTvTitle.setText(trip.getName());
        Glide.with(mContext).load(trip.getCover()).into(this.mImageView);
        String time = DatetimeUtils.getTime(
                DatetimeUtils.timeStampToDate(
                        trip.getCreatedTime()));
        this.mTvCreatedTime.setText(time);
    }
}
