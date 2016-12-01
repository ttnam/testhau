package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.bindings.FilterViewModel;
import com.yosta.phuotngay.bindings.TimelineViewModel;
import com.yosta.phuotngay.databinding.ItemFilterBinding;
import com.yosta.phuotngay.databinding.ItemTimelineBinding;
import com.yosta.phuotngay.models.view.FilterView;
import com.yosta.phuotngay.models.view.TimelineView;

import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.BindingHolder> {

    private Context mContext;
    private List<TimelineView> mTimelineViews;

    public TimelineAdapter(Context context) {
        this.mContext = context;
        this.mTimelineViews = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTimelineBinding itemTimelineBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_timeline,
                parent, false);

        return new BindingHolder(itemTimelineBinding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        ItemTimelineBinding binding =
                (ItemTimelineBinding) holder.binding;

        TimelineView timelineView = this.mTimelineViews.get(position);
        binding.setTimeline(new TimelineViewModel(timelineView.getContent()));
    }

    @Override
    public int getItemCount() {
        return mTimelineViews.size();
    }


    public void adds(List<TimelineView> timelineViews) {
        int positionStart = timelineViews.size() - 1;
        this.mTimelineViews.addAll(timelineViews);
        notifyItemRangeChanged(positionStart, timelineViews.size());
    }

    public void add(TimelineView view) {
        mTimelineViews.add(view);
        notifyItemInserted(mTimelineViews.size() - 1);
    }

    public void remove(int position) {
        int itemCount = getItemCount();
        if (position < itemCount) {
            mTimelineViews.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void clear() {
        mTimelineViews.clear();
        notifyDataSetChanged();
    }

    static class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding = null;

        BindingHolder(ItemTimelineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

