package io.yostajsc.sdk.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.sdk.R;
import io.yostajsc.sdk.glide.CropCircleTransformation;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public class GlideUtils {

    public static void showImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_style_rect_round_none_gray_none)
                .into(imageView);
    }

    public static void showAvatar(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .error(R.drawable.ic_profile_holder)
                .into(imageView);
    }
}
