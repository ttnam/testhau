package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.viewholders.ImageGalleryViewHolder;
import com.yosta.phuotngay.view.base.ViewBehavior;
import com.yosta.phuotngay.view.ImageGalleryView;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryViewHolder> {

    private Context mContext;
    private List<ViewBehavior> behaviorList;

    public ImageGalleryAdapter(Context context) {
        this.mContext = context;
        this.behaviorList = new ArrayList<>();
    }

    @Override
    public ImageGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.view_item_simple_drawee_view, parent, false);
        return new ImageGalleryViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ImageGalleryViewHolder holder, final int position) {
        ImageGalleryView galleryView = (ImageGalleryView) behaviorList.get(position);
        if (galleryView != null) {
            holder.onSetEvent(galleryView);
            holder.onSetContent(galleryView);
        }
    }

    @Override
    public int getItemCount() {
        return behaviorList.size();
    }


    public void addViews(List<ImageGalleryView> galleryViews) {
        int positionStart = behaviorList.size() - 1;
        behaviorList.addAll(galleryViews);
        notifyItemRangeChanged(positionStart, galleryViews.size());
    }

    public void addView(ImageGalleryView galleryView) {
        behaviorList.add(galleryView);
        notifyItemChanged(behaviorList.size() - 1);
    }

    public void clear() {
        behaviorList.clear();
        notifyDataSetChanged();
    }

}

