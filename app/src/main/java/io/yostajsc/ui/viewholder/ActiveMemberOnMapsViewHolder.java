package io.yostajsc.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.glide.CropCircleTransformation;
import io.yostajsc.izigo.R;

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


    @BindView(R.id.image_close)
    AppCompatImageView imageClose;

    private Context mContext;

    public ActiveMemberOnMapsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = itemView.getContext();
    }

    public void bind(@NonNull String avatar, @NonNull String name, @NonNull String distance) {

        Glide.with(mContext)
                .load(avatar)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

        this.textName.setText(name);
        this.textDistance.setText(distance);
        imageClose.setVisibility(View.GONE);
    }

}