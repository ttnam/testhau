package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.realm.RealmList;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.ui.viewholder.ImageryMoreViewHolder;
import io.yostajsc.izigo.ui.viewholder.ImageryNormalViewHolder;
import io.yostajsc.izigo.ui.viewholder.ImageryViewHolder;

public class ImageryAdapter extends RecyclerView.Adapter<ImageryViewHolder> {

    private final static int MAX_ITEM = 6;

    private static boolean isMore = false;
    private static int size = 0;
    private Context mContext;
    // private RealmList<BasePhotoInfo> mUrls;

    public ImageryAdapter(Context context) {
        this.mContext = context;
        // this.mUrls = new RealmList<>();
    }

    @Override
    public ImageryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_imagery, null);
        int width = (int) (DimensionUtil.getScreenWidth(mContext) / 3.0f);
        itemLayoutView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
        if (viewType == Type.MORE) {
            itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_imagery_more, null);
            itemLayoutView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
            return new ImageryMoreViewHolder(itemLayoutView);
        } else
            return new ImageryNormalViewHolder(itemLayoutView);
    }

    @Override
    public int getItemViewType(int position) {
        if (isMore && position == MAX_ITEM - 1) {
            return Type.MORE;
        }
        return Type.NORMAL;
    }

    @Override
    public void onBindViewHolder(final ImageryViewHolder holder, final int position) {
        /*if (isMore && position == MAX_ITEM - 1) {
            ((ImageryMoreViewHolder) holder).bind(mUrls.get(position).getUrl(), size - MAX_ITEM);
        } else {
            ((ImageryNormalViewHolder) holder).bind(mUrls.get(position).getUrl());
        }*/
    }

    @Override
    public int getItemCount() {
        // if (mUrls == null)
            return 0;
        // return mUrls.size();
    }
/*

    public void replaceAll(RealmList<BasePhotoInfo> photos) {
        if (mUrls == null)
            this.mUrls = new RealmList<>();
        clear();
        size = photos.size();
        if (size > MAX_ITEM) {
            isMore = true;
            for (int i = 0; i < MAX_ITEM; i++) {
                this.mUrls.add(photos.get(i));
            }
        } else {
            this.mUrls.addAll(photos);
        }
        notifyDataSetChanged();
    }

    public void add(BasePhotoInfo photoInfo) {
        if (this.mUrls == null)
            this.mUrls = new RealmList<>();
        this.mUrls.add(photoInfo);
        notifyDataSetChanged();
    }
*/

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Type.MORE, Type.NORMAL})
    private @interface Type {
        int NORMAL = 901;
        int MORE = 902;
    }

}

