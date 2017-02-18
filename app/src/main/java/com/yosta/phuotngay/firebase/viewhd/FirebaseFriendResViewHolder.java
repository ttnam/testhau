package com.yosta.phuotngay.firebase.viewhd;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseFriendResViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView textView;

    private Context mContext;

    public FirebaseFriendResViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = itemView.getContext();
        itemView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void onBind(FirebaseFriend friend) {
        textView.setText(friend.getName());
    }
}