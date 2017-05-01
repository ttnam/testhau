package io.yostajsc.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.core.glide.CropCircleTransformation;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view)
    TextView textName;

    @BindView(R.id.txt_content)
    TextView textContent;

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.image_view)
    AppCompatImageView mImageAvatar;

    private Context mContext;

    public CommentViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(String avatar, String name, String content, String createdTime) {

        Glide.with(mContext)
                .load(avatar)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(mImageAvatar);

        textName.setText(name);
        textContent.setText(content);
        textTime.setText(createdTime);
    }

}
