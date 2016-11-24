package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.bindings.FilterViewModel;
import com.yosta.phuotngay.databinding.ItemFilterBinding;
import com.yosta.phuotngay.models.view.FilterView;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.BindingHolder> {

    private Context mContext;
    private List<FilterView> mFlterViews;

    public FilterAdapter(Context context) {
        this.mContext = context;
        this.mFlterViews = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFilterBinding itemFilterBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_filter,
                parent, false);

        return new BindingHolder(itemFilterBinding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        ItemFilterBinding itemFilterBinding =
                (ItemFilterBinding) holder.binding;

        FilterView filterView = this.mFlterViews.get(position);

        itemFilterBinding.setFilter(new FilterViewModel(filterView.getContent()));
    }

    @Override
    public int getItemCount() {
        return mFlterViews.size();
    }


    public void adds(List<FilterView> filterViews) {
        int positionStart = filterViews.size() - 1;
        this.mFlterViews.addAll(filterViews);
        notifyItemRangeChanged(positionStart, filterViews.size());
    }

    public void add(FilterView filterView) {
        mFlterViews.add(filterView);
        notifyItemInserted(mFlterViews.size() - 1);
    }

    public void remove(int position) {
        int itemCount = getItemCount();
        if (position < itemCount)
        {
            mFlterViews.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void clear() {
        mFlterViews.clear();
        notifyDataSetChanged();
    }

    static class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding = null;

        BindingHolder(ItemFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

