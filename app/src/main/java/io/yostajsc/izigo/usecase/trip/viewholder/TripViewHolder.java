package io.yostajsc.izigo.usecase.trip.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.sdk.utils.DimensionUtil;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.utils.GlideUtils;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class TripViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView mTextTitle;

    @BindView(R.id.tv_created_time)
    TextView mTextDuration;

    @BindView(R.id.text_views)
    TextView textViews;

    @BindView(R.id.image_view)
    AppCompatImageView mImageView;

    public TripViewHolder(View itemView) {
        super(itemView);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DimensionUtil.getScreenHeight(itemView.getContext()) / 3));
        ButterKnife.bind(this, itemView);
    }

    public void bind(String cover, String name, int views, String duration) {

        if (!TextUtils.isEmpty(cover))
            GlideUtils.showImage(cover, mImageView);

        this.mTextTitle.setText(name);
        this.mTextDuration.setText(duration);
        this.textViews.setText(String.valueOf(views));
    }
}
