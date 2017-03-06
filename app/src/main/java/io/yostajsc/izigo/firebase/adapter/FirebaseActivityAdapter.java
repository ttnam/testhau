package io.yostajsc.izigo.firebase.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.firebase.model.FirebaseActivity;
import io.yostajsc.izigo.firebase.viewhd.FirebaseActivityViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 12/11/2016.
 */

public class FirebaseActivityAdapter extends FirebaseRecyclerAdapter<FirebaseActivity, FirebaseActivityViewHolder> {

    public FirebaseActivityAdapter(Query ref) {
        super(FirebaseActivity.class, R.layout.item_timeline, FirebaseActivityViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(FirebaseActivityViewHolder viewHolder, FirebaseActivity model, int position) {
        viewHolder.onBind(model);
    }
}
