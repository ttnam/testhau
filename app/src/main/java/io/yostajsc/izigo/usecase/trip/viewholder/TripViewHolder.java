package io.yostajsc.izigo.usecase.trip.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.ui.UiUtils;
import io.yostajsc.sdk.utils.DimensionUtil;
import io.yostajsc.izigo.R;

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

    private Context mContext;

    public TripViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DimensionUtil.getScreenHeight(mContext) / 3));
        ButterKnife.bind(this, itemView);
    }

    public void bind(String cover, String name, int views, String duration) {

        if (!TextUtils.isEmpty(cover))
            UiUtils.showImage(mContext, cover, mImageView);

        this.mTextTitle.setText(name);
        this.mTextDuration.setText(duration);
        this.textViews.setText(String.valueOf(views));
    }
}
