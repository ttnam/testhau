package io.yostajsc.izigo.usecase.notification.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.constants.NotificationType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.sdk.model.trip.IgBaseUserInfo;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.trip.BaseTripInfo;
import io.yostajsc.core.glide.CropCircleTransformation;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.text_view_more)
    TextView textViewMore;

    private CallBack yesAction, noAction;

    public NotificationsViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(IgBaseUserInfo userInfo, BaseTripInfo tripInfo, int type,
                     CallBack yes, CallBack no) {

     /*   Glide.with(mContext)
                .load(tripInfo.getCover())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(this.imageView);*/

        Glide.with(mContext)
                .load(userInfo.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(this.imageView);

        String prefix = "<b>";
        String postfix = "</b>";

        String tripName = prefix + tripInfo.getName() + postfix;
        String from = prefix + userInfo.getName() + postfix;

        switch (type) {

            case NotificationType.ACCEPT_JOIN_TRIP:
                textView.setText(from + " thêm bạn vào trip " + tripName);
                break;
            case NotificationType.MEMBER_ADD_TO_TRIP:
                textView.setText(from + " được thêm vào trip " + tripName + " của bạn.");
                break;
            case NotificationType.REQUEST_JOIN_TRIP:
                textView.setText(Html.fromHtml(
                        from + " xin vào trip " + tripName + " của bạn."));
                break;
        }
        this.noAction = no;
        this.yesAction = yes;
    }
/*
    @OnClick(R.id.text_yes)
    public void yes() {
        yesAction.run();
    }

    @OnClick(R.id.text_no)
    public void no() {
        noAction.run();
    }*/

}
