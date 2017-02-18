package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;
import com.yosta.phuotngay.firebase.viewhd.FirebaseFriendResViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class FriendsResAdapter extends RecyclerView.Adapter<FirebaseFriendResViewHolder> {

    private Context mContext = null;
    private List<FirebaseFriend> mTrips = null;

    private int lastPosition = -1;


    public FriendsResAdapter(Context context) {
        this.mContext = context;
        this.mTrips = new ArrayList<>();
    }

    @Override
    public FirebaseFriendResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_type_2, null);
        return new FirebaseFriendResViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(FirebaseFriendResViewHolder holder, int position) {
        FirebaseFriend trip = mTrips.get(position);
        holder.onBind(trip);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public void adds(List<FirebaseFriend> trips) {
        this.mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public FirebaseFriend getItem(int position) {
        if (position < 1 || position >= getItemCount()) {
            return null;
        }
        return this.mTrips.get(position);
    }

    public void add(FirebaseFriend trip) {
        this.mTrips.add(trip);
        notifyDataSetChanged();
        notifyItemInserted(this.mTrips.size() - 1);
    }

    public void remove(int position) {
        this.mTrips.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        this.mTrips.clear();
    }
}
