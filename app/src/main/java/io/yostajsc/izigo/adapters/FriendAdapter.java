package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.core.interfaces.ItemClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.user.Friend;
import io.yostajsc.izigo.ui.viewholder.FriendViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private Context mContext = null;
    private List<Friend> mFriends = null;
    private ItemClick<Integer, Integer> mItemClick;
    private boolean mIsClose = false;

    public FriendAdapter(Context context, boolean isClose, @NonNull ItemClick<Integer, Integer> itemClick) {
        this.mContext = context;
        this.mFriends = new ArrayList<>();
        this.mItemClick = itemClick;
        this.mIsClose = isClose;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_member, null);
        return new FriendViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = mFriends.get(position);
        holder.bind(friend, mIsClose, mItemClick);
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public void adds(List<Friend> friends) {
        this.mFriends.addAll(friends);
        notifyDataSetChanged();
    }

    public Friend getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mFriends.get(position);
    }

    public int add(Friend trip) {
        this.mFriends.add(trip);
        int index = this.mFriends.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void replaceAll(List<Friend> friends) {
        if (this.mFriends != null) {
            clear();
            this.mFriends.addAll(friends);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        this.mFriends.clear();
    }
}
