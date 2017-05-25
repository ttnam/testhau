package io.yostajsc.izigo.usecase.view.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.glide.CropCircleTransformation;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView textName;

    @BindView(R.id.txt_content)
    TextView textContent;

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.image_view)
    AppCompatImageView mImageAvatar;

    private Context mContext;

    public CommentViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(String avatar, String name, String content, String createdTime) {
        if (!TextUtils.isEmpty(avatar))
            Glide.with(mContext)
                    .load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(mImageAvatar);
        if (!TextUtils.isEmpty(name))
            textName.setText(name);
        if (!TextUtils.isEmpty(content))
            textContent.setText(content);
        if (!TextUtils.isEmpty(createdTime))
            textTime.setText(createdTime);
    }

}
