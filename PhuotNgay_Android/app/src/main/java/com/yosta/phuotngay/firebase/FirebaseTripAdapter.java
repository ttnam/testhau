package com.yosta.phuotngay.firebase;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.models.trip.FirebaseTrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTripAdapter extends FirebaseRecyclerAdapter<FirebaseTrip, FirebaseTripViewHolder> {

    private Context mContext;

    public FirebaseTripAdapter(Context context, Query ref) {
        super(FirebaseTrip.class, R.layout.item_trip, FirebaseTripViewHolder.class, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(FirebaseTripViewHolder viewHolder, FirebaseTrip model, int position) {
        viewHolder.onBind(model);
    }
}
