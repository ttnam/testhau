package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.viewholders.ReviewHolder;
import com.yosta.phuotngay.models.views.ReviewView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private Context mContext;
    private List<ReviewView> mViewsList;

    public ReviewAdapter(Context context) {
        this.mContext = context;
        this.mViewsList = new ArrayList<>();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.view_item_review, parent, false);
        return new ReviewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ReviewHolder holder, final int position) {
        ReviewView reviewView = mViewsList.get(position);
        assert reviewView != null;
        holder.onSetEvent(reviewView);
        holder.onSetData(reviewView);
    }

    public void addViews(List<ReviewView> reviewViews) {
        mViewsList.addAll(reviewViews);
        notifyDataSetChanged();
    }

    public void addView(ReviewView reviewView) {
        mViewsList.add(reviewView);
        notifyDataSetChanged();
    }

    public void clear() {
        mViewsList.clear();
    }

    @Override
    public int getItemCount() {
        return mViewsList.size();
    }
}

