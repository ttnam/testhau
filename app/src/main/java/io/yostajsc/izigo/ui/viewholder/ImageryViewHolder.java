package io.yostajsc.izigo.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.utils.validate.ValidateUtils;
import io.yostajsc.view.RoundedCornersTransformation;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ImageryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    private Context mContext;

    public ImageryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void bind(String url) {
        if (ValidateUtils.canUse(url)) {
            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    //.bitmapTransform(new RoundedCornersTransformation(mContext, 24, 0))
                    .into(imageView);
        }
    }
}
