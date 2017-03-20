package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.notification.Notification;
import io.yostajsc.izigo.models.notification.Notifications;
import io.yostajsc.izigo.ui.viewholder.NotificationsViewHolder;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {

    private Context mContext = null;
    private Notifications mNotifications = null;

    public NotificationAdapter(Context context) {
        this.mContext = context;
        this.mNotifications = new Notifications();
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_notification, null);

        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT ));

        return new NotificationsViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        holder.bind(notification.getFrom(), notification.getTrip(), notification.getType());
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

    public int add(@NonNull Notification notification) {
        this.mNotifications.add(notification);
        int index = this.mNotifications.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        this.mNotifications.clear();
    }
}
