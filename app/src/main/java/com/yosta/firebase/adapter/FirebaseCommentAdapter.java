package com.yosta.firebase.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.yosta.phuotngay.R;
import com.yosta.firebase.model.FirebaseComment;
import com.yosta.firebase.viewhd.FirebaseCommentViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class FirebaseCommentAdapter extends FirebaseRecyclerAdapter<FirebaseComment, FirebaseCommentViewHolder> {


    public FirebaseCommentAdapter(Query ref) {
        super(FirebaseComment.class, R.layout.item_comment, FirebaseCommentViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(FirebaseCommentViewHolder viewHolder, FirebaseComment comment, int position) {
        viewHolder.onBind(comment);
    }
}
