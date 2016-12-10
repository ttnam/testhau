package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.FirebaseTripViewHolder;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class TripAdapter extends RecyclerView.Adapter<FirebaseTripViewHolder> {

    private Context mContext = null;
    private List<FirebaseTrip> mTrips = null;

    public TripAdapter(Context context) {
        this.mContext = context;
        this.mTrips = new ArrayList<>();
    }

    @Override
    public FirebaseTripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_trip, null);
        return new FirebaseTripViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(FirebaseTripViewHolder holder, int position) {
        FirebaseTrip trip = mTrips.get(position);
        holder.onBind(trip);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public void adds(List<FirebaseTrip> trips) {
        mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public int add(FirebaseTrip trip) {
        mTrips.add(trip);
        int index = mTrips.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        mTrips.clear();
    }
}
