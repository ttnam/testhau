package io.yostajsc.izigo.usecase.trip.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;

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

    public void bind(String url, String name, String content, String createdTime) {
        if (!TextUtils.isEmpty(url))

            Glide.with(mContext)
                    .load(url)
                    .apply(new RequestOptions()
                            .dontTransform()
                            .priority(Priority.IMMEDIATE)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop()
                            .error(R.drawable.ic_style_rect_round_none_gray_none))
                    .into(mImageAvatar);

        if (!TextUtils.isEmpty(name))
            textName.setText(name);
        if (!TextUtils.isEmpty(content))
            textContent.setText(content);
        if (!TextUtils.isEmpty(createdTime))
            textTime.setText(createdTime);
    }

}
