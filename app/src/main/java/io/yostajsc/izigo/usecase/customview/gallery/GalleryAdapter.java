package io.yostajsc.izigo.usecase.customview.gallery;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.izigo.R;

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

        @BindView(R.id.image_view)
        AppCompatImageView imageView;

        @BindView(R.id.layout)
        View layout;

        boolean isSelected = false;

        GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String url) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(itemView.getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        }
    }
}

