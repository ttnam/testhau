package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.viewholders.ProfileAvatarHolder;
import com.yosta.phuotngay.models.views.ProfileAvatarView;

import java.util.ArrayList;
import java.util.List;

public class ProfileAvatarAdapter extends RecyclerView.Adapter<ProfileAvatarHolder> {

    private Context mContext;
    private List<ProfileAvatarView> mViewsList;

    public ProfileAvatarAdapter(Context context) {
        this.mContext = context;
        this.mViewsList = new ArrayList<>();
    }

    @Override
    public ProfileAvatarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.view_item_avatar, parent, false);
        return new ProfileAvatarHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ProfileAvatarHolder holder, final int position) {
        ProfileAvatarView avatarView = mViewsList.get(position);
        if (avatarView != null) {
            holder.onSetEvent(avatarView);
            holder.onSetContent(avatarView);
        }
    }

    public void addViews(List<ProfileAvatarView> avatarViews) {
        mViewsList.addAll(avatarViews);
        notifyDataSetChanged();
    }

    public void addView(ProfileAvatarView avatarView) {
        mViewsList.add(avatarView);
        notifyDataSetChanged();
    }

    public void clear() {
        mViewsList.clear();
    }

    @Override
    public int getItemCount() {
        return mViewsList.size();
    }
}

