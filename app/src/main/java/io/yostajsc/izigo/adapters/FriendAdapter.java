package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.interfaces.ItemClickCallBack;
import io.yostajsc.izigo.models.Friend;
import io.yostajsc.izigo.ui.bottomsheet.viewholder.FriendViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private Context mContext = null;
    private List<Friend> mFriends = null;
    private ItemClickCallBack itemClickCallBack;

    public FriendAdapter(Context context, @NotNull ItemClickCallBack callBack) {
        this.mContext = context;
        this.mFriends = new ArrayList<>();
        this.itemClickCallBack = callBack;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_type_1, null);
        return new FriendViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = mFriends.get(position);
        holder.onBind(friend, itemClickCallBack);
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
    }

    public void clear() {
        this.mFriends.clear();
    }
}
