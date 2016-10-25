package com.yosta.phuotngay.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yosta.phuotngay.globalapp.UIUtils;

/**
 * Created by Phuc-Hau Nguyen on 10/16/2016.
 */

public class DataBindingAdapter {

    @BindingAdapter(value = {"imageUrl"})
    public static void loadImage(AppCompatImageView imageView, String imageUrl) {
        Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView textView, String fontName) {
        String newFontName = "fonts/" + fontName + ".ttf";
        UIUtils.setFont(textView.getContext(), newFontName, textView);
    }
}
