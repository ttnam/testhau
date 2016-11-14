package com.yosta.phuotngay.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.helpers.app.UIUtils;

/**
 * Created by Phuc-Hau Nguyen on 10/16/2016.
 */

public class DataBindingAdapter {

    @BindingAdapter(value = {"imageUrl"})
    public static void loadImage(AppCompatImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView textView, String fontName) {
        String newFontName = "fonts/" + fontName + ".ttf";
        UIUtils.setFont(textView.getContext(), newFontName, textView);
    }
}
