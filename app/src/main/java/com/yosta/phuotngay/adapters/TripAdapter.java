package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.AppHelper;
import com.yosta.phuotngay.models.trip.BaseTrip;
import com.yosta.phuotngay.ui.viewholder.TripViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {

    private Context mContext = null;
    private List<BaseTrip> mTrips = null;

    public TripAdapter(Context context) {
        this.mContext = context;
        this.mTrips = new ArrayList<>();
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_trip, null);
        return new TripViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        BaseTrip baseTrip = mTrips.get(position);
        holder.onBind(baseTrip.getCover(), baseTrip.getName(),
                AppHelper.builder(mContext).getTimeGap(
                        baseTrip.getDuration()
                )
        );
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public void replaceAll(List<BaseTrip> trips) {
        this.mTrips.clear();
        this.mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public BaseTrip getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mTrips.get(position);
    }

    public int add(BaseTrip trip) {
        this.mTrips.add(trip);
        int index = this.mTrips.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mTrips.clear();
    }
}
