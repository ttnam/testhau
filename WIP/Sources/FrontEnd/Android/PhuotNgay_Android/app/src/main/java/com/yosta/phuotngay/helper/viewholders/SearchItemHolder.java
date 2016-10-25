package com.yosta.phuotngay.helper.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.globalapp.UIUtils;
import com.yosta.phuotngay.view.SearchItemView;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class SearchItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener , RecyclerView.OnItemTouchListener{

    private AppCompatTextView textView;
    private AppCompatTextView txtTitle;
    private Context mContext;

    private GestureDetector gestureDetector;

    /* private TextView txtAuthor, txtStatus;
     private CircleImageView imgAvatar;
 */
    public SearchItemHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);

/*
        this.txtAuthor = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        this.txtStatus = (AppCompatTextView) itemView.findViewById(R.id.textView_description);
        this.imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);*/


        txtTitle = (AppCompatTextView) itemView.findViewById(R.id.textView_title);
        textView = (AppCompatTextView) itemView.findViewById(R.id.textView);

        UIUtils.setFont(itemView.getContext(), UIUtils.FONT_LATO_HEAVY, txtTitle);
        UIUtils.setFont(itemView.getContext(), UIUtils.FONT_LATO_MEDIUM, textView);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition(); // gets item position
        if (position != RecyclerView.NO_POSITION) {
            Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
