package com.yosta.phuotngay.firebase;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTripAdapter extends FirebaseRecyclerAdapter<FirebaseTrip, FirebaseTripViewHolder> {

    public FirebaseTripAdapter(Query ref) {
        super(FirebaseTrip.class, R.layout.item_trip, FirebaseTripViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(FirebaseTripViewHolder viewHolder, FirebaseTrip model, int position) {
        viewHolder.onBind(model);
    }
}
