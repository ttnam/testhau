package io.yostajsc.izigo.usecase.trip.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class TimelineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_content)
    TextView textContent;

    @BindView(R.id.text_time)
    TextView textTime;

    public TimelineViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String hour, String day, String content) {
        this.textTime.setText(String.format("%s, %s", hour, day));
        this.textContent.setText(content);
    }

}
