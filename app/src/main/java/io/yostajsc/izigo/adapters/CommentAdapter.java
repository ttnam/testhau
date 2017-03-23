package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.yostajsc.core.utils.AppUtils;
import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.comment.Comment;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.izigo.ui.viewholder.CommentViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context mContext = null;
    private Comments mComments = null;

    public CommentAdapter(Context context) {
        this.mContext = context;
        this.mComments = new Comments();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
        return new CommentViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bind(
                comment.getCreatorAvatar(),
                comment.getCreatorName(),
                comment.getContent(),
                DatetimeUtils.getDate(comment.getCreatedTime())
        );
    }

    @Override
    public int getItemCount() {
        if (mComments == null)
            return 0;
        return mComments.size();
    }

    public void replaceAll(Comments comments) {
        if (this.mComments == null)
            return;
        clear();
        this.mComments.addAll(comments);
        notifyDataSetChanged();
    }

    public Comment getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mComments.get(position);
    }

    public int add(@NonNull Comment comment) {
        this.mComments.add(comment);
        int index = this.mComments.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mComments.clear();
    }
}
