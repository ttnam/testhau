package io.yostajsc.izigo.ui.bottomsheet.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.utils.AppUtils;

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
                AppUtils.onGetScreenHeight((Activity) mContext) / 3));
        ButterKnife.bind(this, itemView);
    }

    public void onBind(String cover, String name, String arrive) {
        Glide.with(mContext).load(cover).into(this.mImageView);
        this.mTextTitle.setText(name);
        this.mTextDuration.setText(arrive);
    }
}
