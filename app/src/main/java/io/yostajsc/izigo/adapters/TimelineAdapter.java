package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.Timeline;
import io.yostajsc.izigo.ui.viewholder.TimelineViewHolder;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineViewHolder> {

    private Context mContext;
    private List<Timeline> mData;

    public TimelineAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_timeline, null);

        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new TimelineViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final TimelineViewHolder holder, final int position) {
        Timeline timeline = mData.get(position);

        holder.bind(
                DatetimeUtils.getTime(timeline.getTime()),
                DatetimeUtils.getDate(timeline.getTime()),
                timeline.getContent()
        );
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public void replaceAll(List<Timeline> timelines) {
        if (mData == null)
            this.mData = new ArrayList<>();
        clear();
        this.mData.addAll(timelines);
        notifyDataSetChanged();
    }

    public void add(Timeline timeline) {
        if (this.mData == null)
            this.mData = new ArrayList<>();
        this.mData.add(timeline);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null)
            mData.clear();
        notifyDataSetChanged();
    }

}

