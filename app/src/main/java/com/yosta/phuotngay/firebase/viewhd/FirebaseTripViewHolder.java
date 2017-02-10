package com.yosta.phuotngay.firebase.viewhd;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.helpers.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTripViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView mTvTitle;

    @BindView(R.id.tv_created_time)
    TextView mTvCreatedTime;

    @BindView(R.id.image_view)
    AppCompatImageView mImageView;

    private Context mContext;

    public FirebaseTripViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                AppHelper.onGetScreenHeight((Activity) mContext) / 3));
        ButterKnife.bind(this, itemView);
    }

    public void onBind(FirebaseTrip trip) {
        Glide.with(mContext).load(trip.getCover()).into(this.mImageView);
        this.mTvTitle.setText(trip.getName());
        this.mTvCreatedTime.setText(trip.getCreatedtime());
    }
}
