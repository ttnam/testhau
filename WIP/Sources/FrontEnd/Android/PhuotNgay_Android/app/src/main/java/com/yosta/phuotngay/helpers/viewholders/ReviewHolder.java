package com.yosta.phuotngay.helpers.viewholders;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.models.views.ReviewView;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ReviewHolder extends RecyclerView.ViewHolder {

    private TextView txtAuthor;
    private TextView txtDescription;

    public ReviewHolder(final View itemView) {
        super(itemView);

        txtAuthor = (TextView) itemView.findViewById(R.id.textView_title);
        txtDescription = (TextView) itemView.findViewById(R.id.textView_description);
    }

    public void onSetEvent(ReviewView reviewView) {

    }

    public void onSetData(ReviewView reviewView) {

    }
}
