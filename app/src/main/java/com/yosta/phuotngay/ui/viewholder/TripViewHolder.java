package com.yosta.phuotngay.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class TripViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView mTextTitle;

    @BindView(R.id.tv_created_time)
    TextView mTextDuration;

    @BindView(R.id.image_view)
    AppCompatImageView mImageView;

    private Context mContext;

    public TripViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                AppHelper.onGetScreenHeight((Activity) mContext) / 3));
        ButterKnife.bind(this, itemView);
    }

    public void onBind(String cover, String name, String arrive) {
        Glide.with(mContext).load(cover).into(this.mImageView);
        this.mTextTitle.setText(name);
        this.mTextDuration.setText(arrive);
    }
}
