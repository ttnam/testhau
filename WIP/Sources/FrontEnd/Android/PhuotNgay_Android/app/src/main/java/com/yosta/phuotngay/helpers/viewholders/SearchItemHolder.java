package com.yosta.phuotngay.helpers.viewholders;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.globalapp.UIUtils;
import com.yosta.phuotngay.models.views.SearchItemView;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class SearchItemHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView textView;
    private AppCompatTextView txtTitle;

    /* private TextView txtAuthor, txtStatus;
     private CircleImageView imgAvatar;
 */
    public SearchItemHolder(View itemView) {
        super(itemView);
/*
        this.txtAuthor = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        this.txtStatus = (AppCompatTextView) itemView.findViewById(R.id.textView_description);
        this.imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);*/


        txtTitle = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        textView = (AppCompatTextView) itemView.findViewById(R.id.textView);

        UIUtils.setFont(itemView.getContext(), UIUtils.FONT_LATO_HEAVY, txtTitle);
        UIUtils.setFont(itemView.getContext(), UIUtils.FONT_LATO_MEDIUM, textView);
    }

    public void onSetEvent(SearchItemView searchItem) {
      /*  itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    public void onSetContent(SearchItemView searchItem) {
        //this.txtAuthor.setText("Phuc Henry");
    }
}
