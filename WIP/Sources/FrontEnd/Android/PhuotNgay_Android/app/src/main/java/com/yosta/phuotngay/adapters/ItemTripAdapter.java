package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.viewholders.ItemTripViewHolder;
import com.yosta.phuotngay.models.views.ItemTripView;

import java.util.ArrayList;
import java.util.List;

public class ItemTripAdapter extends RecyclerView.Adapter<ItemTripViewHolder> {

    private Context mContext;
    private List<ItemTripView> mViewsList;

    public ItemTripAdapter(Context context) {
        this.mContext = context;
        this.mViewsList = new ArrayList<>();
    }

    @Override
    public ItemTripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.view_item_main_trip, parent, false);
        return new ItemTripViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ItemTripViewHolder holder, final int position) {
        ItemTripView note = mViewsList.get(position);
        if (note != null) {
            holder.onSetEvent(note);
            holder.onSetContent(note);
        }
    }

    @Override
    public int getItemCount() {
        return mViewsList.size();
    }

    public void addTrips(List<ItemTripView> noteList) {
        int positionStart = getItemCount();
        mViewsList.addAll(noteList);
        notifyItemRangeInserted(positionStart, noteList.size());
    }

    public void addTrip(ItemTripView note) {
        mViewsList.add(0, note);
        notifyItemInserted(0);
    }

    public void clear() {
        mViewsList.clear();
    }

}

