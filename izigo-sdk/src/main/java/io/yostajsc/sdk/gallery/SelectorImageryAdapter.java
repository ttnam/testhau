package io.yostajsc.sdk.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SelectorImageryAdapter extends RecyclerView.Adapter<ImageNormalViewHolder> {

    private Context mContext = null;
    private List<ImageryModel> mUrls = null;
    private OnClickListener mOnClickListener = null;

    public SelectorImageryAdapter(Context context, OnClickListener onClickListener) {
        this.mContext = context;
        this.mUrls = new ArrayList<>();
        this.mOnClickListener = onClickListener;
    }

    @Override
    public ImageNormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LayoutSelector.Selector()
                .selectorType(viewType)
                .build(mContext);
    }

    @Override
    public void onBindViewHolder(final ImageNormalViewHolder holder, final int position) {
        holder.bind(mUrls.get(position).getUrl(), mOnClickListener);
    }


    @Override
    @LayoutViewType
    public int getItemViewType(int position) {
        return mUrls.get(position).getType();
    }

    @Override
    public int getItemCount() {
        if (mUrls == null)
            return 0;
        return mUrls.size();
    }

    public void add(ImageryModel image) {
        if (this.mUrls == null)
            this.mUrls = new ArrayList<>();
        this.mUrls.add(image);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mUrls.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ImageryModel> images) {
        if (this.mUrls == null)
            this.mUrls = new ArrayList<>();
        this.mUrls.addAll(images);
        notifyDataSetChanged();
    }

    public void replaceAll(List<ImageryModel> images) {
        this.mUrls = images;
        notifyDataSetChanged();
    }


    private interface ImageryModel {
        String getUrl();

        @LayoutViewType
        int getType();
    }

    public static class Imagery implements ImageryModel {

        private String mUrl;

        private int mType = LayoutViewType.NORMAL;

        public Imagery(String url, @LayoutViewType int type) {
            this.mUrl = url;
            this.mType = type;
        }

        public Imagery(String url) {
            this.mUrl = url;
            this.mType = LayoutViewType.NORMAL;
        }

        public void setType(@LayoutViewType int type) {
            this.mType = type;
        }

        @Override
        public String getUrl() {
            return mUrl;
        }

        @Override
        public int getType() {
            return mType;
        }
    }

}

