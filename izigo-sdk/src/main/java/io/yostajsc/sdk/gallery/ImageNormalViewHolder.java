package io.yostajsc.sdk.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.sdk.R;

/**
 * Created by nphau on 5/25/17.
 */

public class ImageNormalViewHolder extends RecyclerView.ViewHolder {

    protected Context mContext;
    protected AppCompatImageView imageView;
    OnClick mOnClickListener = null;

    public ImageNormalViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
    }

    public void setOnClickListener(OnClick onClickListener) {
        mOnClickListener = onClickListener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(getAdapterPosition());
            }
        });
    }

    public void bind(String url, OnClick onClickListener) {
        if (!TextUtils.isEmpty(url)) {

            Glide.clear(imageView);
            Glide.with(mContext)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.ic_style_rect_round_corners_gray_none)
                    .into(imageView);
        }
        setOnClickListener(onClickListener);
    }

    protected void bind(Bitmap bitmap) {
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
    }

    public interface OnClick {
        void onClick(int position);
    }
}
