package io.yostajsc.izigo.usecase.notification;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.utils.ToastUtils;
import io.yostajsc.core.designs.decorations.SpacesItemDecoration;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.base.BaseFragment;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.model.IgNotification;
import io.yostajsc.izigo.utils.UiUtils;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class NotificationsFragment extends BaseFragment {

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    @BindView(R.id.recycler_view)
    RecyclerView rvNotification;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.text_view_title)
    TextView textView;

    private NotificationAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        onApplyData();
        return rootView;
    }

    private void onApplyViews() {
        this.adapter = new NotificationAdapter(mContext);
        UiUtils.onApplyRecyclerView(rvNotification, this.adapter, new SlideInRightAnimator(), null);
        rvNotification.addItemDecoration(new SpacesItemDecoration(3));
    }


    private void onApplyData() {

        IzigoSdk.UserExecutor.getNotifications(new IgCallback<List<IgNotification>, String>() {
            @Override
            public void onSuccessful(List<IgNotification> igNotifications) {
                int size = igNotifications.size();
                if (size > 0) {
                    toggleUI(false);
                    adapter.replaceAll(igNotifications);
                } else {
                    toggleUI(true);
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(mContext, error);
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    private void toggleUI(boolean isEmpty) {
        if (isEmpty) {
            button.setVisibility(View.GONE);
            textView.setText("You got no notifications");
            layoutEmpty.setVisibility(View.VISIBLE);
            rvNotification.setVisibility(View.INVISIBLE);
        } else {
            rvNotification.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }
}
