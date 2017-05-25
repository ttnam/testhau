package io.yostajsc.izigo.usecase.view.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.user.IgFriend;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.consts.ItemClick;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.glide.CropCircleTransformation;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class MemberViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private Resources resources = null;
    private ItemClick<Integer, Integer> mInviteClick = null;
    private CallBackWith<Integer> mKick;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.text_view)
    TextView textName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;


    @BindView(R.id.image_close)
    AppCompatImageView imageClose;

    public MemberViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = itemView.getContext();
        resources = mContext.getResources();

        itemView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @OnClick(R.id.button)
    public void onInvite() {
        button.setBackground(resources.getDrawable(R.drawable.ic_style_rect_round_corners_accent_white));
        button.setTextColor(resources.getColor(R.color.colorAccentDark));
        button.setText(resources.getString(R.string.all_invited));
        mInviteClick.onClick(MessageType.ITEM_CLICK_INVITED, getAdapterPosition());
    }

    public void bind(IgFriend friend, boolean isClose, @NonNull ItemClick<Integer, Integer> inviteClick,
                     CallBackWith<Integer> kick) {

        this.mInviteClick = inviteClick;
        this.mKick = kick;

        this.textName.setText(friend.getName());
        Glide.with(mContext)
                .load(friend.getAvatar())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);
        if (isClose) {
            if (getAdapterPosition() == 0) {
                imageClose.setVisibility(View.INVISIBLE);
            } else {
                imageClose.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mKick.run(getAdapterPosition());
                    }
                });
            }
        } else {
            imageClose.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }
}