package io.yostajsc.izigo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.photo.BasePhotoInfo;
import io.yostajsc.izigo.ui.viewholder.ImageryViewHolder;
import io.yostajsc.utils.validate.ValidateUtils;

public class ImageryAdapter extends RecyclerView.Adapter<ImageryViewHolder> {

    private Context mContext;
    private RealmList<BasePhotoInfo> mUrls;

    public ImageryAdapter(Context context) {
        this.mContext = context;
        this.mUrls = new RealmList<>();
    }

    @Override
    public ImageryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_imagery, null);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels / 3;
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ImageryViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ImageryViewHolder holder, final int position) {
        String url = mUrls.get(position).getUrl();
        if (ValidateUtils.canUse(url)) {
            holder.bind(url);
        }
    }

    @Override
    public int getItemCount() {
        if (mUrls == null)
            return 0;
        return mUrls.size();
    }

    public void replaceAll(RealmList<BasePhotoInfo> photoInfos) {
        if (mUrls == null)
            this.mUrls = new RealmList<>();
        clear();
        this.mUrls.addAll(photoInfos);
        notifyDataSetChanged();
    }

    public void add(BasePhotoInfo photoInfo) {
        if (this.mUrls == null)
            this.mUrls = new RealmList<>();
        this.mUrls.add(photoInfo);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mUrls != null)
            mUrls.clear();
        notifyDataSetChanged();
    }

}

