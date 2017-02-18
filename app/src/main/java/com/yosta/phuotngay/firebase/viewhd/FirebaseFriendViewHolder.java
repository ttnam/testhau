package com.yosta.phuotngay.firebase.viewhd;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;
import com.yosta.phuotngay.helpers.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseFriendViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.button)
    Button button;

    private Context mContext;

    public FirebaseFriendViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);

        itemView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        int width = AppHelper.onGetScreenWidth((Activity) mContext);

        /*button.setLayoutParams(new LinearLayout.LayoutParams(
                (int) (width * 0.2),
                ViewGroup.LayoutParams.WRAP_CONTENT));*/
    }

    public void onBind(FirebaseFriend group) {

    }
}
