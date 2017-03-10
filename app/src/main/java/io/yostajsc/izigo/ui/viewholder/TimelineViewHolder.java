package io.yostajsc.izigo.ui.viewholder;

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

    @BindView(R.id.text_day)
    TextView textDay;

    @BindView(R.id.text_hour)
    TextView textHour;

    public TimelineViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String hour, String day, String content) {
        this.textDay.setText(day);
        this.textContent.setText(content);
        this.textHour.setText(hour);
    }
}
