package io.yostajsc.izigo.ui.viewholder;

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
import io.yostajsc.constants.MessageType;
import io.yostajsc.izigo.R;
import io.yostajsc.interfaces.ItemClick;
import io.yostajsc.izigo.models.user.Friend;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FriendViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private boolean toggle = false;
    private Resources resources = null;
    private ItemClick<Integer, Integer> mInviteClick = null;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.text_view)
    TextView textName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;

    public FriendViewHolder(View itemView) {
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
        toggle = !toggle;
        if (toggle) {
            button.setBackground(resources.getDrawable(R.drawable.ic_style_rect_round_corners_accent_white));
            button.setTextColor(resources.getColor(R.color.colorAccentDark));
            button.setText(resources.getString(R.string.all_invited));
            mInviteClick.onClick(MessageType.ITEM_CLICK_INVITED, getAdapterPosition());
        } else {
            button.setBackground(resources.getDrawable(R.drawable.ic_style_button_round_corners_accent_reflector));
            button.setTextColor(resources.getColor(android.R.color.white));
            button.setText(resources.getString(R.string.all_invite));
            mInviteClick.onClick(MessageType.ITEM_CLICK_INVITE, getAdapterPosition());
        }
    }

    public void onBind(Friend friend, @NonNull ItemClick<Integer, Integer> inviteClick) {
        this.mInviteClick = inviteClick;
        this.textName.setText(friend.getName());
        Glide.with(mContext)
                .load(friend.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

    }
}