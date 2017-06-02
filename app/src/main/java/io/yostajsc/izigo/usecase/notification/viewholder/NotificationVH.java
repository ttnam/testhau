package io.yostajsc.izigo.usecase.notification.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.utils.DatetimeUtils;
import io.yostajsc.sdk.utils.GlideUtils;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class NotificationVH extends RecyclerView.ViewHolder {

    protected Context mContext;

    @BindView(R.id.image_view)
    AppCompatImageView imageViewAvatar;

    @BindView(R.id.image_cover)
    AppCompatImageView imageViewCover;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.text_view_more)
    TextView textViewMore;

    public NotificationVH(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(String url, String message, String time, String coverUrl) {

        if (!TextUtils.isEmpty(url)) {
            GlideUtils.showAvatar(url, imageViewAvatar);
        }
        if (!TextUtils.isEmpty(coverUrl)) {
            GlideUtils.showImage(coverUrl, imageViewCover);
        }

        textView.setText(Html.fromHtml(message));
        textViewMore.setText(DatetimeUtils.getTimeGap(mContext, Calendar.getInstance().getTimeInMillis() - Long.parseLong(time)));
    }

}
