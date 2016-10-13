package com.yosta.phuotngay.helpers.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.SightDetailActivity;
import com.yosta.phuotngay.helpers.globalapp.UIUtils;
import com.yosta.phuotngay.models.views.ItemTripView;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ItemTripViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView imgCover = null;
    private RelativeLayout layoutCover = null;
    private AppCompatTextView txtTitle = null, txtPhotos = null, txtDescription = null;

    private Context context = null;

    public ItemTripViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        // imgCover = (AppCompatImageView) itemView.findViewById(R.id.image);
        layoutCover = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
        txtTitle = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        //txtPhotos = (AppCompatTextView) itemView.findViewById(R.id.textView);
        txtDescription = (AppCompatTextView) itemView.findViewById(R.id.textView_description);

        UIUtils.setFont(context, UIUtils.FONT_LATO_BOLD, txtTitle);
        UIUtils.setFont(context, UIUtils.FONT_LATO_HEAVY, txtDescription);
    }

    public void onSetEvent(ItemTripView note) {
        layoutCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.getContext().startActivity(new Intent(itemView.getContext(), SightDetailActivity.class));
            }
        });
    }

    public void onSetContent(ItemTripView itemTripView) {
        txtTitle.setText(Html.fromHtml("ĐẢO NAM DU | <i>VIỆT NAM</i>"));
        txtDescription.setText(Html.fromHtml("From 500K"));
    }
}
