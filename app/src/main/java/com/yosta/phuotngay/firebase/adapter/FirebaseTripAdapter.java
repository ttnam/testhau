package com.yosta.phuotngay.firebase.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.ui.viewholder.TripViewHolder;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTripAdapter extends FirebaseRecyclerAdapter<FirebaseTrip, TripViewHolder> {

    public FirebaseTripAdapter(Query ref) {
        super(FirebaseTrip.class, R.layout.item_trip, TripViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(TripViewHolder viewHolder, FirebaseTrip trip, int position) {
        // viewHolder.onBind(trip);
    }

}
