package io.yostajsc.izigo.usecase.trip.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import io.yostajsc.sdk.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.trip.IgTripModel;
import io.yostajsc.izigo.usecase.trip.viewholder.TripViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {

    private Context mContext = null;
    private SparseArray<IgTripModel> mList;

    public TripAdapter(Context context) {
        this.mContext = context;
        this.mList = new SparseArray<>();
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_trip, null));
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        IgTripModel trip = mList.get(position);
        holder.bind(
                trip.getCoverUrl(), // cover
                trip.getName(), // name
                trip.getNumberOfViews(),
                DatetimeUtils.getTimeGap(mContext, trip.getDurationTimeInMillis())
        );
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    public IgTripModel getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mList.get(position);
    }

    public void add(@NonNull IgTripModel newTrip) {
        this.mList.put(this.mList.size(), newTrip);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
    }

}
