package io.yostajsc.izigo.usecase.notification.viewholder;

import android.view.View;

import butterknife.OnClick;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ActionNotificationVH extends NotificationVH {

    private OnAcceptClickListener acceptClickListener = null;
    private OnDismissClickListener dismissClickListener = null;


    public ActionNotificationVH(View itemView) {
        super(itemView);
    }

    public void bind(String avatarUrl, String message, String time, String coverUrl,
                     OnAcceptClickListener onAcceptClickListener, OnDismissClickListener onDismissClickListener) {
        super.bind(avatarUrl, message, time, coverUrl);
        this.acceptClickListener = onAcceptClickListener;
        this.dismissClickListener = onDismissClickListener;
    }

    @OnClick(R.id.text_view_dismiss)
    public void dismiss() {
        if (dismissClickListener == null)
            return;
        dismissClickListener.onDismiss();
    }

    @OnClick(R.id.text_view_accept)
    public void accept() {
        if (acceptClickListener == null)
            return;
        acceptClickListener.onAccept();
    }

    public interface OnAcceptClickListener {
        void onAccept();
    }

    public interface OnDismissClickListener {
        void onDismiss();
    }
}
