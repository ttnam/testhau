package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.interfaces.ItemClick;
import io.yostajsc.izigo.R;
import io.yostajsc.usecase.realm.user.FriendRealm;
import io.yostajsc.usecase.realm.user.FriendsRealm;
import io.yostajsc.izigo.ui.viewholder.MemberViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {

    private Context mContext = null;
    private FriendsRealm mFriends = null;
    private ItemClick<Integer, Integer> mItemClick;
    private CallBackWith<Integer> mKick;

    private boolean mIsClose = false;

    public MemberAdapter(Context context, boolean isClose,
                         @NonNull ItemClick<Integer, Integer> itemClick,
                         CallBackWith<Integer> kick) {
        this.mContext = context;
        this.mFriends = new FriendsRealm();
        this.mItemClick = itemClick;
        this.mIsClose = isClose;
        this.mKick = kick;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_member, null);
        return new MemberViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        FriendRealm friend = mFriends.get(position);
        holder.bind(friend, mIsClose, mItemClick, mKick);
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public FriendRealm getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mFriends.get(position);
    }

    public int add(FriendRealm trip) {
        this.mFriends.add(trip);
        int index = this.mFriends.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void replaceAll(FriendsRealm friends) {
        this.mFriends = friends;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mFriends.clear();
    }
}
