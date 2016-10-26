package com.yosta.phuotngay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.view.SearchItemView;
import com.yosta.phuotngay.helper.viewholders.SearchItemHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemHolder> {

    private Context mContext;
    private List<SearchItemView> mViewsList;

    public SearchItemAdapter(Context context) {
        this.mContext = context;
        this.mViewsList = new ArrayList<>();
    }

    @Override
    public SearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.view_item_comment, parent, false);
        return new SearchItemHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final SearchItemHolder holder, final int position) {
        SearchItemView searchItem = mViewsList.get(position);
        if (searchItem != null) {
        }
    }

    public void addViews(List<SearchItemView> searchItems) {
        mViewsList.addAll(searchItems);
        notifyDataSetChanged();
    }

    public void addView(SearchItemView searchItem) {
        mViewsList.add(searchItem);
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
