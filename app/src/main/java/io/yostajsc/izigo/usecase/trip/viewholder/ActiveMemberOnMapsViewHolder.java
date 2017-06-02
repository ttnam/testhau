package io.yostajsc.izigo.usecase.trip.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.utils.GlideUtils;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class ActiveMemberOnMapsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_name)
    TextView textName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;

    @BindView(R.id.text_view_distance)
    TextView textDistance;

    @BindView(R.id.icon_view)
    View iconView;

    private Context mContext;

    public ActiveMemberOnMapsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = itemView.getContext();
    }

    public void bind(@NonNull String url, @NonNull String name,
                     String distance, String time, boolean isVisible) {

        GlideUtils.showAvatar(url, imageAvatar);

        this.textName.setText(name);
        if (distance == null || time == null) {
            this.textDistance.setVisibility(View.GONE);
        } else {
            this.textDistance.setText(distance + " - " + time);
        }

        iconView.setBackgroundDrawable(isVisible ?
                mContext.getResources().getDrawable(R.drawable.ic_style_circle_green_none) :
                mContext.getResources().getDrawable(R.drawable.ic_style_circle_yellow_none));
    }

}