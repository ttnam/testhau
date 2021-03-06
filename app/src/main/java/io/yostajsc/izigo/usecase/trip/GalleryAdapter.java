package io.yostajsc.izigo.usecase.trip;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.sdk.R;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.utils.DimensionUtil;
import io.yostajsc.sdk.utils.GlideUtils;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Context mContext;
    private List<String> mUrls = null;
    private SparseArray<String> mSelectedItems = null;
    private CallBackWith<Integer> mOnClick = null;

    public GalleryAdapter(Context context, CallBackWith<Integer> onClick) {
        this.mContext = context;
        this.mUrls = new ArrayList<>();
        this.mSelectedItems = new SparseArray<>();
        this.mOnClick = onClick;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_imagery_select, null);
        int width = (int) (DimensionUtil.getScreenWidth(mContext) / 3.0f);
        itemLayoutView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
        return new GalleryViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, int position) {

        final String url = mUrls.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.isSelected = !holder.isSelected;
                if (holder.isSelected) {
                    mSelectedItems.put(holder.getAdapterPosition(), url);
                    holder.layout.setVisibility(View.VISIBLE);
                } else {
                    mSelectedItems.remove(holder.getAdapterPosition());
                    holder.layout.setVisibility(View.GONE);
                }
                mOnClick.run(mSelectedItems.size());
            }
        });
        holder.bind(url);
    }

    @Override
    public int getItemCount() {
        if (mUrls == null)
            return 0;
        return mUrls.size();
    }

    public void replaceAll(List<String> urls) {
        this.mUrls = urls;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mUrls.clear();
    }

    public void add(String url) {
        if (this.mUrls == null)
            this.mUrls = new ArrayList<>();
        this.mUrls.add(url);
        notifyDataSetChanged();
    }

    public String get(int pos) {
        if (pos < 0 || pos > mUrls.size())
            return null;
        return this.mUrls.get(pos);
    }

    public ArrayList<String> getSelectedItems() {

        int size = this.mSelectedItems.size();
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int key = mSelectedItems.keyAt(i);
            res.add(mSelectedItems.get(key));
        }

        return res;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        View layout;
        AppCompatImageView imageView;

        boolean isSelected = false;

        GalleryViewHolder(View itemView) {
            super(itemView);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
            layout = itemView.findViewById(R.id.layout);
        }

        void bind(String url) {
            if (!TextUtils.isEmpty(url)) {
                GlideUtils.showImage(url, imageView);
            }
        }
    }
}

