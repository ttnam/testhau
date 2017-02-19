package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;
import com.yosta.phuotngay.firebase.viewhd.FirebaseFriendViewHolder;
import com.yosta.phuotngay.interfaces.ItemClickCallBack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class FriendAdapter extends RecyclerView.Adapter<FirebaseFriendViewHolder> {

    private Context mContext = null;
    private List<FirebaseFriend> mTrips = null;
    private ItemClickCallBack itemClickCallBack;

    public FriendAdapter(Context context, @NotNull ItemClickCallBack callBack) {
        this.mContext = context;
        this.mTrips = new ArrayList<>();
        this.itemClickCallBack = callBack;
    }

    @Override
    public FirebaseFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_type_1, null);
        return new FirebaseFriendViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(FirebaseFriendViewHolder holder, int position) {
        FirebaseFriend trip = mTrips.get(position);
        holder.onBind(trip, itemClickCallBack);
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
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mTrips.get(position);
    }

    public int add(FirebaseFriend trip) {
        this.mTrips.add(trip);
        int index = this.mTrips.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mTrips.clear();
    }
}
