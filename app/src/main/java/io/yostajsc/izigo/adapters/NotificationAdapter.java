package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.yostajsc.backend.core.APIManager;
import io.yostajsc.constants.NotificationType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.models.notification.Notification;
import io.yostajsc.izigo.models.notification.Notifications;
import io.yostajsc.izigo.ui.viewholder.NotificationsViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {

    private Context mContext = null;
    private Notifications mNotifications = null;
    private CallBack yesAction, noAction;

    public NotificationAdapter(Context context) {
        this.mContext = context;
        this.mNotifications = new Notifications();
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_notification, null);

        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new NotificationsViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, final int position) {
        final Notification notification = mNotifications.get(position);

        int notiType = notification.getType();
        if (notiType == NotificationType.ACCEPT_JOIN_TRIP) {

            holder.bind(notification.getFrom(), notification.getTrip(), notification.getType(),
                    new CallBack() {
                        @Override
                        public void run() {
                            accept(position, notification.getTrip().getId(), notification.getId(), 1);
                        }
                    }, new CallBack() {
                        @Override
                        public void run() {
                            accept(position, notification.getTrip().getId(), notification.getId(), 0);
                        }
                    });
        } else {
            holder.bind(notification.getFrom(), notification.getTrip(), notification.getType(),
                    new CallBack() {
                        @Override
                        public void run() {
                            verify(position, notification.getTrip().getId(), notification.getId(), 1);
                        }
                    }, new CallBack() {
                        @Override
                        public void run() {
                            verify(position, notification.getTrip().getId(), notification.getId(), 0);
                        }
                    });
        }

    }

    @Override
    public int getItemCount() {
        if (mNotifications == null)
            return 0;
        return mNotifications.size();
    }

    public void replaceAll(Notifications notifications) {
        if (this.mNotifications == null)
            return;
        clear();
        this.mNotifications.addAll(notifications);
        notifyDataSetChanged();
    }

    public Notification getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mNotifications.get(position);
    }

    private void remove(int pos) {
        this.mNotifications.remove(pos);
        notifyDataSetChanged();
    }

    public int add(@NonNull Notification notification) {
        this.mNotifications.add(notification);
        int index = this.mNotifications.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mNotifications.clear();
    }

    private void accept(final int pos, String tripId, String notiId, final int accept) {

        String authorization = StorageUtils.inject(mContext).getString(AppDefine.AUTHORIZATION);

        APIManager.connect().accept(authorization, tripId, notiId, accept, new CallBack() {
            @Override
            public void run() {

            }
        }, new CallBack() {
            @Override
            public void run() {
                if (accept == 1)
                    Toast.makeText(mContext, "Đã chấp nhận", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Không chấp nhận", Toast.LENGTH_SHORT).show();
                remove(pos);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verify(final int pos, String tripId, String notiId, final int accept) {

        String authorization = StorageUtils.inject(mContext).getString(AppDefine.AUTHORIZATION);

        APIManager.connect().verify(authorization, tripId, notiId, accept, new CallBack() {
            @Override
            public void run() {

            }
        }, new CallBack() {
            @Override
            public void run() {
                if (accept == 1)
                    Toast.makeText(mContext, "Đã chấp nhận", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Không chấp nhận", Toast.LENGTH_SHORT).show();
                remove(pos);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
