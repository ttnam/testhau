package io.yostajsc.izigo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.Timeline;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.ui.viewholder.TimelineViewHolder;
import io.yostajsc.utils.AppUtils;
import io.yostajsc.utils.DimensionUtil;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineViewHolder> {

    private Context mContext;
    private Timelines mTimelines;

    public TimelineAdapter(Context context) {
        this.mContext = context;
        this.mTimelines = new Timelines();
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_timeline, null);

        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT ));

        return new TimelineViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final TimelineViewHolder holder, final int position) {
        Timeline timeline = mTimelines.get(position);

        holder.bind(
                AppUtils.builder().getTime(timeline.getTime(), AppUtils.H_MM),
                AppUtils.builder().getTime(timeline.getTime(), AppUtils.DD_MM_YYYY),
                timeline.getContent()
        );
    }

    @Override
    public int getItemCount() {
        if (mTimelines == null)
            return 0;
        return mTimelines.size();
    }

    public void replaceAll(Timelines timelines) {
        if (mTimelines == null)
            this.mTimelines = new Timelines();
        clear();
        this.mTimelines.addAll(timelines);
        notifyDataSetChanged();
    }

    public void add(Timeline timeline) {
        if (this.mTimelines == null)
            this.mTimelines = new Timelines();
        this.mTimelines.add(timeline);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mTimelines != null)
            mTimelines.clear();
        notifyDataSetChanged();
    }

}

