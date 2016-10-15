package com.yosta.phuotngay.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.SightDetailActivity;
import com.yosta.phuotngay.model.place.Place;

public class PlaceViewModel extends BaseObservable {

    private Context mContext = null;
    private Place mPlace = null;

    public PlaceViewModel(Context context, Place place) {
        this.mContext = context;
        this.mPlace = place;
    }

    public String getSrcCompat() {
        return this.mPlace.getCover();
    }

    @BindingAdapter({"srcCompat"})
    public static void loadImage(AppCompatImageView imageView, String srcCompat) {
        Picasso.with(imageView.getContext())
                .load(srcCompat)
                //.placeholder(R.drawable.ic_sample)
                .into(imageView);
    }

    @Bindable
    public Spannable getName() {
        // return mPlace.getName();
        return new SpannableString(
                Html.fromHtml("ĐẢO NAM DU | <i>VIỆT NAM</i>"));
    }

    @Bindable
    public Spannable getDescription() {
        return new SpannableString(Html.fromHtml("From 500K"));
    }

    public View.OnClickListener onClickOpen() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SightDetailActivity.class));
            }
        };
    }
}
