package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.ui.viewholder.ActiveMemberOnMapsViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class MemberActiveOnMapsAdapter extends RecyclerView.Adapter<ActiveMemberOnMapsViewHolder> {

    private Context mContext = null;
    private List<IgFriend> mList = null;


    public MemberActiveOnMapsAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    @Override
    public ActiveMemberOnMapsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_map_person, null);
        itemLayoutView.setLayoutParams(new CardView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return new ActiveMemberOnMapsViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ActiveMemberOnMapsViewHolder holder, int position) {
        IgFriend friend = mList.get(position);
        holder.bind(friend.getAvatar(), friend.getName(), "5km");
    }

    @Override
    public int getItemCount() {
        if(mList == null)
            return 0;
        return mList.size();
    }

    public IgFriend getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mList.get(position);
    }

    public void add(IgFriend trip) {
        this.mList.add(trip);
        notifyDataSetChanged();
    }

    public void replaceAll(List<IgFriend> friends) {
        if(friends == null)
            return;
        this.mList = friends;
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.mList != null)
            this.mList.clear();
    }
}
