package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.RealmList;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.ui.viewholder.TripViewHolder;
import io.yostajsc.utils.AppUtils;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {

    private Context mContext = null;
    private List<Trip> mTrips = null;

    public TripAdapter(Context context) {
        this.mContext = context;
        this.mTrips = new RealmList<>();
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_trip, null);
        return new TripViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        Trip baseTrip = mTrips.get(position);
        holder.onBind(
                baseTrip.getCover(),
                baseTrip.getTripName(),
                AppUtils.builder(mContext).getTimeGap(
                        baseTrip.getDuration()
                )
        );
    }

    @Override
    public int getItemCount() {
        if (mTrips == null)
            return 0;
        return mTrips.size();
    }

    public void replaceAll(List<Trip> trips) {
        if (this.mTrips == null)
            return;
        clear();
        this.mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public Trip getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mTrips.get(position);
    }

    public int add(@NonNull Trip trip) {
        this.mTrips.add(trip);
        int index = this.mTrips.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mTrips.clear();
    }
}
