package io.yostajsc.izigo.usecase.trip.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.map.model.Person;
import io.yostajsc.izigo.usecase.customview.viewholder.ActiveMemberOnMapsViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class MemberActiveOnMapsAdapter extends RecyclerView.Adapter<ActiveMemberOnMapsViewHolder> {

    private Context mContext = null;
    private List<Person> mList = null;

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
        Person person = mList.get(position);
        holder.bind(person.getAvatar(), person.getName(), person.getDistance(), person.getTime(), person.isVisible());
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    public Person getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mList.get(position);
    }

    public void add(Person person) {
        this.mList.add(person);
        notifyDataSetChanged();
    }

    public void replaceAll(Person[] person) {
        if (person == null)
            return;
        if (this.mList == null)
            this.mList = new ArrayList<>();
        this.mList.clear();
        Collections.addAll(mList, person);
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.mList != null)
            this.mList.clear();
    }
}
