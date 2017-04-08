package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.realm.RealmList;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.photo.BasePhotoInfo;
import io.yostajsc.izigo.ui.viewholder.ImageryViewHolder;

public class ImageryAdapter extends RecyclerView.Adapter<ImageryViewHolder> {

    private Context mContext;
    private RealmList<BasePhotoInfo> mUrls;

    public ImageryAdapter(Context context) {
        this.mContext = context;
        this.mUrls = new RealmList<>();
    }

    @Override
    public ImageryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_imagery, null);
        int width = DimensionUtil.getScreenWidth(mContext) / 3;
        itemLayoutView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
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

    public void replaceAll(RealmList<BasePhotoInfo> album, int numberOfItem) {
        if (mUrls == null)
            this.mUrls = new RealmList<>();
        clear();
        int size = album.size();
        if (size > numberOfItem) {
            for (int i = 0; i <= numberOfItem; i++)
                this.mUrls.add(album.get(i));
        } else {
            this.mUrls.addAll(album);
        }
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

