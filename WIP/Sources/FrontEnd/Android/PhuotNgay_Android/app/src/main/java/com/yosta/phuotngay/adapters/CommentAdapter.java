package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.models.comments.Comment;
import com.yosta.phuotngay.helpers.viewholders.CommentHolder;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private Context mContext;
    private List<Comment> mViewsList;

    public CommentAdapter(Context context) {
        this.mContext = context;
        this.mViewsList = new ArrayList<>();
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.view_item_comment, parent, false);
        return new CommentHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final CommentHolder holder, final int position) {
        Comment comment = mViewsList.get(position);
        if (comment != null) {
            holder.onSetEvent(comment);
            holder.onSetContent(comment);
        }
    }

    @Override
    public int getItemCount() {
        return mViewsList.size();
    }

    public void addComments(List<Comment> comment) {
        mViewsList.addAll(comment);
        notifyDataSetChanged();
    }

    public int addComment(Comment comment) {
        mViewsList.add(comment);
        int index = mViewsList.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        mViewsList.clear();
    }

}

