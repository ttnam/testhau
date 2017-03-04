package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.firebase.model.FirebaseGroup;
import com.yosta.firebase.viewhd.FirebaseGroupViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class GroupAdapter extends RecyclerView.Adapter<FirebaseGroupViewHolder> {

    private Context mContext = null;
    private List<FirebaseGroup> mTrips = null;

    public GroupAdapter(Context context) {
        this.mContext = context;
        this.mTrips = new ArrayList<>();
    }

    @Override
    public FirebaseGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
        return new FirebaseGroupViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(FirebaseGroupViewHolder holder, int position) {
        FirebaseGroup trip = mTrips.get(position);
        holder.onBind(trip);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public void adds(List<FirebaseGroup> trips) {
        this.mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public FirebaseGroup getItem(int position) {
        if (position < 1 || position >= getItemCount()) {
            return null;
        }
        return this.mTrips.get(position);
    }

    public int add(FirebaseGroup trip) {
        this.mTrips.add(trip);
        int index = this.mTrips.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mTrips.clear();
    }
}
