package com.yosta.phuotngay.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.models.base.ViewBehavior;
import com.yosta.phuotngay.models.view.ImageryView;
import com.yosta.phuotngay.models.viewholder.ImageryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ImageryAdapter extends RecyclerView.Adapter<ImageryViewHolder> {

    private Context mContext;
    private List<ViewBehavior> behaviorList;

    public ImageryAdapter(Context context) {
        this.mContext = context;
        this.behaviorList = new ArrayList<>();
    }

    @Override
    public ImageryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_simple_drawee_view, null);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels / 2;
        int height = (int) (width / 1.33f);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(width, height));
        return new ImageryViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ImageryViewHolder holder, final int position) {
        ImageryView galleryView = (ImageryView) behaviorList.get(position);
        if (galleryView != null) {
            holder.onSetEvent(galleryView);
            holder.onSetContent(galleryView);
        }
    }

    @Override
    public int getItemCount() {
        return behaviorList.size();
    }


    public void addImages(List<ImageryView> galleryViews) {
        int positionStart = behaviorList.size() - 1;
        behaviorList.addAll(galleryViews);
        notifyItemRangeChanged(positionStart, galleryViews.size());
    }

    public void addImage(ImageryView imageryView) {
        behaviorList.add(imageryView);
        notifyItemChanged(behaviorList.size() - 1);
    }

    public void clear() {
        behaviorList.clear();
        notifyDataSetChanged();
    }

}

