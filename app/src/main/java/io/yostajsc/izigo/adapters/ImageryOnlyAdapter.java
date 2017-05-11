package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.customview.viewholder.ImageryNormalViewHolder;
import io.yostajsc.izigo.usecase.customview.viewholder.ImageryViewHolder;

public class ImageryOnlyAdapter extends RecyclerView.Adapter<ImageryViewHolder> {

    private Context mContext;
    private List<IgImage> mUrls = null;

    public ImageryOnlyAdapter(Context context) {
        this.mContext = context;
        this.mUrls = new ArrayList<>();
    }

    @Override
    public ImageryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_imagery, null);
        int width = (int) (DimensionUtil.getScreenWidth(mContext) / 3.0f);
        itemLayoutView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
        return new ImageryNormalViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ImageryViewHolder holder, final int position) {
        ((ImageryNormalViewHolder) holder).bind(mUrls.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        if (mUrls == null)
            return 0;
        return mUrls.size();
    }

    public void replaceAll(List<IgImage> album) {
        this.mUrls = album;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mUrls.clear();
    }

    public void add(IgImage image) {
        if (this.mUrls == null)
            this.mUrls = new ArrayList<>();
        this.mUrls.add(image);
        notifyDataSetChanged();
    }

}

