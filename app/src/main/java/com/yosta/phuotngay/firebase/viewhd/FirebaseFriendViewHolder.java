package com.yosta.phuotngay.firebase.viewhd;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;
import com.yosta.interfaces.ItemClickCallBack;
import com.yosta.phuotngay.models.app.MessageType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseFriendViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private boolean toggle = false;
    private Resources resources = null;
    private ItemClickCallBack itemClickCallBack;

    @BindView(R.id.button)
    Button button;

    public FirebaseFriendViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = itemView.getContext();
        resources = mContext.getResources();

        itemView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @OnClick(R.id.button)
    public void onInvite() {
        toggle = !toggle;
        if (toggle) {
            button.setBackground(resources.getDrawable(R.drawable.ic_style_rect_round_corners_accent_white));
            button.setTextColor(resources.getColor(R.color.colorAccentDark));
            button.setText(resources.getString(R.string.all_invited));
            itemClickCallBack.onClick(MessageType.ITEM_CLICK_INVITED, getAdapterPosition());
        } else {
            button.setBackground(resources.getDrawable(R.drawable.ic_style_button_round_corners_accent_reflector));
            button.setTextColor(resources.getColor(android.R.color.white));
            button.setText(resources.getString(R.string.all_invite));
            itemClickCallBack.onClick(MessageType.ITEM_CLICK_INVITE, getAdapterPosition());
        }
    }

    public void onBind(FirebaseFriend group, ItemClickCallBack callBack) {
        this.itemClickCallBack = callBack;
    }
}