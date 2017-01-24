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
import com.yosta.phuotngay.ui.view.FilterView;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.BindingHolder> {

    private Context mContext;
    private List<FilterView> mFilterViews;

    public FilterAdapter(Context context) {
        this.mContext = context;
        this.mFilterViews = new ArrayList<>();
        this.mFilterViews.add(new FilterView(
                this.mContext.getResources().getStringArray(R.array.arr_during_times)[0]
        ));
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

        FilterView filterView = this.mFilterViews.get(position);

        itemFilterBinding.setFilter(new FilterViewModel(filterView.getContent()));
    }

    @Override
    public int getItemCount() {
        return mFilterViews.size();
    }


    public void adds(List<FilterView> filterViews) {
        int positionStart = filterViews.size() - 1;
        this.mFilterViews.addAll(filterViews);
        notifyItemRangeChanged(positionStart, filterViews.size());
    }

    public void add(FilterView filterView) {
        mFilterViews.add(filterView);
        notifyItemInserted(mFilterViews.size() - 1);
    }

    public void remove(int position) {
        int itemCount = getItemCount();
        if (position < itemCount) {
            mFilterViews.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void clear() {
        mFilterViews.clear();
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

