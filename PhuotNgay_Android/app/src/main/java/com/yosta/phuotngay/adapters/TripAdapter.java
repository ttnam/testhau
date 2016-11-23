package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.bindings.TripViewModel;
import com.yosta.phuotngay.databinding.ItemPlaceBinding;
import com.yosta.phuotngay.models.trip.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.BindingHolder> {

    private Context mContext = null;
    private List<Trip> mTrips = null;

    public TripAdapter(Context context) {
        this.mContext = context;
        this.mTrips = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemPlaceBinding placeBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_place,
                parent, false);

        return new BindingHolder(placeBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemPlaceBinding placeBinding = (ItemPlaceBinding) holder.binding;

        placeBinding.setTrip(new TripViewModel(
                "NAM DU | VIỆT NAM",
                "http://halongwave.com/data/VINH-HA-LONG2.jpg",
                "FROM 500K"
        ));
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public void addPlaces(List<Trip> trips) {
        mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public int addPlace(Trip trip) {
        mTrips.add(trip);
        int index = mTrips.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        mTrips.clear();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding = null;

        public BindingHolder(ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
