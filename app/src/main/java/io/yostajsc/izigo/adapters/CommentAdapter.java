package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.Comment;
import io.yostajsc.ui.viewholder.CommentViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context mContext = null;
    private List<Comment> mData = null;

    public CommentAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
        return new CommentViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mData.get(position);
        holder.bind(
                comment.getCreatorAvatar(),
                comment.getCreatorName(),
                comment.getContent(),
                DatetimeUtils.getDate(comment.getCreatedTime())
        );
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public void replaceAll(List<Comment> comments) {
        if (this.mData == null)
            return;
        clear();
        this.mData.addAll(comments);
        notifyDataSetChanged();
    }

    public Comment getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mData.get(position);
    }

    public int add(@NonNull Comment comment) {
        this.mData.add(comment);
        int index = this.mData.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mData.clear();
    }
}
