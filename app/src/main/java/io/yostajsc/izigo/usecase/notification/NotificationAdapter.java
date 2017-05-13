package io.yostajsc.izigo.usecase.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.notification.viewholder.NotificationVH;
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
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_notification, null);

        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new NotificationVH(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(NotificationVH holder, final int position) {
        final IgNotification igNotification = mData.get(position);

        int notiType = igNotification.getType();
        // if (notiType == NotificationType.ACCEPT_JOIN_TRIP) {
        String cover = null, avatar = null;
        if (igNotification.getFrom() != null) {
            avatar = igNotification.getFrom().getAvatar();
        }
        if (igNotification.getTrip() != null) {
            cover = igNotification.getTrip().getCover();
        }

        holder.bind(avatar, igNotification.getMessage(), igNotification.getId(), cover);

                    /*new CallBack() {
                        @Override
                        public void run() {
                            // accept(position, igNotification.getTrip().getId(), igNotification.getId(), 1);
                        }
                    }, new CallBack() {
                        @Override
                        public void run() {
                            // accept(position, igNotification.getTrip().getId(), igNotification.getId(), 0);
                        }
                    });*/
        /*} else {
            holder.bind(igNotification.getFrom(), igNotification.getTrip(), igNotification.getType(),
                    new CallBack() {
                        @Override
                        public void run() {
                            // verify(position, igNotification.getTrip().getId(), igNotification.getId(), 1);
                        }
                    }, new CallBack() {
                        @Override
                        public void run() {
                            // verify(position, igNotification.getTrip().getId(), igNotification.getId(), 0);
                        }
                    });
        }*/

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


    /*public IgNotification getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mNotifications.get(position);
    }

    private void remove(int pos) {
        this.mNotifications.remove(pos);
        notifyDataSetChanged();
    }

    public int add(@NonNull IgNotification notification) {
        this.mNotifications.add(notification);
        int index = this.mNotifications.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    private void accept(final int pos, String tripId, String notiId, final int accept) {

        IzigoApiManager.connect().accept(tripId, notiId, accept, new CallBack() {
            @Override
            public void run() {

            }
        }, new CallBack() {
            @Override
            public void run() {
                if (accept == 1)
                    Toast.makeText(mContext, "Đã chấp nhận", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Đã từ chối", Toast.LENGTH_SHORT).show();
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

        IzigoApiManager.connect().verify(tripId, notiId, accept, new CallBack() {
            @Override
            public void run() {

            }
        }, new CallBack() {
            @Override
            public void run() {
                if (accept == 1)
                    Toast.makeText(mContext, "Đã chấp nhận", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Đã từ chối", Toast.LENGTH_SHORT).show();
                remove(pos);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
