package io.yostajsc.izigo.usecase.trip.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.sdk.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.IgComment;
import io.yostajsc.izigo.usecase.view.viewholder.CommentViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context mContext = null;
    private List<IgComment> mData = null;

    public CommentAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
        itemLayoutView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CommentViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        IgComment comment = mData.get(position);
        holder.bind(
                comment.getCreatorAvatar(),
                comment.getCreatorName(),
                comment.getContent(),
                DatetimeUtils.getDate(Long.valueOf(comment.getCreatedTime()))
        );
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public void replaceAll(List<IgComment> comments) {
        if (this.mData == null)
            return;
        clear();
        this.mData.addAll(comments);
        notifyDataSetChanged();
    }

    public IgComment getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mData.get(position);
    }

    public void add(@NonNull IgComment comment) {
        this.mData.add(comment);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mData.clear();
    }
}
