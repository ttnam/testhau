package io.yostajsc.izigo.firebase.viewhd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.firebase.model.FirebaseActivity;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseActivityViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_content)
    TextView mTxtContent;

    @BindView(R.id.tv_time)
    TextView mTxtTime;

    @BindView(R.id.tV_hour)
    TextView mTvHour;

    public FirebaseActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(FirebaseActivity activity) {
        this.mTxtTime.setText(activity.getDate());
        this.mTxtContent.setText(activity.getContent());
        this.mTvHour.setText(activity.getHour());
    }
}
