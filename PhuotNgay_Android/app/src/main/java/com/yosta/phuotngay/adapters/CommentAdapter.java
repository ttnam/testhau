package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.bindings.CommentBinding;
import com.yosta.phuotngay.databinding.ItemCommentBinding;
import com.yosta.phuotngay.models.comment.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.BindingHolder> {

    private Context mContext;
    private List<Comment> mComments;

    public CommentAdapter(Context context) {
        this.mContext = context;
        this.mComments = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemCommentBinding commentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_comment, parent, false);
        return new BindingHolder(commentBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemCommentBinding commentBinding = (ItemCommentBinding) holder.binding;
        commentBinding.setComment(new CommentBinding(mContext, mComments.get(position)));
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void addComments(List<Comment> comment) {
        mComments.addAll(comment);
        notifyDataSetChanged();
    }

    public int addComment(Comment comment) {
        mComments.add(comment);
        int index = mComments.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        mComments.clear();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public BindingHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
