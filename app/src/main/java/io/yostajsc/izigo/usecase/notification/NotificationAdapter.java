package io.yostajsc.izigo.usecase.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.core.utils.ToastUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.constants.NotificationType;
import io.yostajsc.izigo.usecase.notification.viewholder.ActionNotificationVH;
import io.yostajsc.izigo.usecase.notification.viewholder.NotificationVH;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.model.IgNotification;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationVH> {

    private Context mContext = null;
    private List<IgNotification> mData = null;

    public NotificationAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public NotificationVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        NotificationVH viewHolder = null;
        switch (viewType) {
            case NotificationType.ACCEPT_JOIN_TRIP:
            case NotificationType.REQUEST_JOIN_TRIP:
            case NotificationType.MEMBER_ADD_TO_TRIP:
                itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_action_notification, null);
                itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new ActionNotificationVH(itemLayoutView);
                break;
            case NotificationType.ADMIN_TO_USER:
            case NotificationType.SYSTEM:
                itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_notification, null);
                itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder = new NotificationVH(itemLayoutView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationVH holder, final int position) {
        final IgNotification igNotification = mData.get(position);
        String cover = null, avatar = null;
        if (igNotification.getFrom() != null) {
            avatar = igNotification.getFrom().getAvatar();
        }
        if (igNotification.getTrip() != null) {
            cover = igNotification.getTrip().getCover();
        }
        switch (getItemViewType(position)) {
            case NotificationType.REQUEST_JOIN_TRIP: // verify
            case NotificationType.MEMBER_ADD_TO_TRIP: // verify
                ((ActionNotificationVH) holder).bind(avatar, igNotification.getMessage(), igNotification.getId(), cover, new ActionNotificationVH.OnAcceptClickListener() {
                    @Override
                    public void onAccept() {
                        verify(position, igNotification.getTrip().getId(), igNotification.getId(), 1);
                    }
                }, new ActionNotificationVH.OnDismissClickListener() {
                    @Override
                    public void onDismiss() {
                        verify(position, igNotification.getTrip().getId(), igNotification.getId(), 0);
                    }
                });
                break;
            case NotificationType.ACCEPT_JOIN_TRIP: // Accept
                ((ActionNotificationVH) holder).bind(avatar, igNotification.getMessage(), igNotification.getId(), cover, new ActionNotificationVH.OnAcceptClickListener() {
                    @Override
                    public void onAccept() {
                        accept(position, igNotification.getTrip().getId(), igNotification.getId(), 1);
                    }
                }, new ActionNotificationVH.OnDismissClickListener() {
                    @Override
                    public void onDismiss() {
                        accept(position, igNotification.getTrip().getId(), igNotification.getId(), 0);
                    }
                });
                break;
            case NotificationType.ADMIN_TO_USER:
            case NotificationType.SYSTEM:
                holder.bind(avatar, igNotification.getMessage(), igNotification.getId(), cover);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.mData.get(position).getType();
    }

    @Override
    public int getItemCount() {
        if (this.mData == null)
            return 0;
        return this.mData.size();
    }

    public void replaceAll(List<IgNotification> igNotifications) {
        this.mData = igNotifications;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mData.clear();
    }

    private void accept(final int pos, String tripId, String notiId, final int accept) {

        IzigoSdk.UserExecutor.accept(tripId, notiId, accept, new IgCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                ToastUtils.showToast(mContext, (accept == 1) ? "Đã chấp nhận" : "Đã từ chối");
                remove(pos);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(mContext, error);
            }

            @Override
            public void onExpired() {

            }
        });
    }


    private void verify(final int pos, String tripId, String notiId, final int verify) {

        IzigoSdk.UserExecutor.verify(tripId, notiId, verify, new IgCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                ToastUtils.showToast(mContext, (verify == 1) ? "Đã chấp nhận" : "Đã từ chối");
                remove(pos);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(mContext, error);
            }

            @Override
            public void onExpired() {

            }
        });
    }

    private void remove(int pos) {
        this.mData.remove(pos);
        notifyDataSetChanged();
    }

}
