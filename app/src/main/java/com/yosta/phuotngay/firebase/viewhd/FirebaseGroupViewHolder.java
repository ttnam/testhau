package com.yosta.phuotngay.firebase.viewhd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yosta.phuotngay.firebase.model.FirebaseGroup;

import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseGroupViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    public FirebaseGroupViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void onBind(FirebaseGroup group) {

    }
}
