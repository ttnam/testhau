package io.yostajsc.izigo.usecase.notification.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.core.glide.CropCircleTransformation;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class NotificationVH extends RecyclerView.ViewHolder {

    private Context mContext;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.image_cover)
    AppCompatImageView imageCover;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.text_view_more)
    TextView textViewMore;

    public NotificationVH(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(String avatarUrl, String message, String time, String coverUrl) {

        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(mContext)
                    .load(avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.ic_profile_holder)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(this.imageView);
        }
        if (!TextUtils.isEmpty(coverUrl)) {
            Glide.with(mContext)
                    .load(coverUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(this.imageCover);
        }

        textView.setText(Html.fromHtml(message));

        textViewMore.setText(DatetimeUtils.getTimeGap(mContext, Calendar.getInstance().getTimeInMillis() - Long.parseLong(time)));
    }

}
