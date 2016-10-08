package com.yosta.phuotngay.helpers.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.SightDetailActivity;
import com.yosta.phuotngay.activities.dialogs.DialogComment;
import com.yosta.phuotngay.helpers.AppUtils;
import com.yosta.phuotngay.models.views.ItemTripView;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ItemTripViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView imgCover = null;
    private RelativeLayout layoutCover = null;
    private LinearLayout layoutComment = null;
    private AppCompatTextView txtTitle = null, txtPhotos = null, txtDescription;

    private Context context = null;

    public ItemTripViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imgCover = (AppCompatImageView) itemView.findViewById(R.id.image);
        layoutComment = (LinearLayout) itemView.findViewById(R.id.layout);
        layoutCover = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
        txtTitle = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        txtPhotos = (AppCompatTextView) itemView.findViewById(R.id.textView);
        txtDescription = (AppCompatTextView) itemView.findViewById(R.id.textView_description);

        AppUtils.setFont(context, "fonts/Lato-Heavy.ttf", txtTitle);
        AppUtils.setFont(context, "fonts/Lato-Regular.ttf", txtPhotos);
        AppUtils.setFont(context, "fonts/Lato-LightItalic.ttf", txtDescription);
    }

    public void onSetEvent(ItemTripView note) {
        layoutCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.getContext().startActivity(new Intent(itemView.getContext(), SightDetailActivity.class));
            }
        });
        layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogComment commentDialog = new DialogComment(context);
                commentDialog.show();
            }
        });
    }

    public void onSetContent(ItemTripView itemTripView) {

    }
}
