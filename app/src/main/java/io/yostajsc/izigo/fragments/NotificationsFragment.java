package io.yostajsc.izigo.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.sdk.api.IzigoApiManager;
import io.yostajsc.core.designs.decorations.SpacesItemDecoration;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.NotificationAdapter;
import io.yostajsc.utils.UiUtils;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class NotificationsFragment extends CoreFragment {

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    @BindView(R.id.recycler_view)
    RecyclerView rvNotification;

    @BindView(R.id.button)
    Button button;

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
        button.setVisibility(View.GONE);
        this.adapter = new NotificationAdapter(mContext);
        UiUtils.onApplyRecyclerView(rvNotification, this.adapter, new SlideInRightAnimator(), null);
        rvNotification.addItemDecoration(new SpacesItemDecoration(3));
    }


    private void onApplyData() {

        /*IzigoApiManager.connect().getNotification(new CallBack() {
            @Override
            public void run() {
                // TODO:
            }
        }, new CallBackWith<Notifications>() {
            @Override
            public void run(Notifications notifications) {
                int size = notifications.size();
                if (size > 0) {
                    layoutEmpty.setVisibility(View.INVISIBLE);
                    rvNotification.setVisibility(View.VISIBLE);
                    adapter.replaceAll(notifications);
                } else {
                    layoutEmpty.setVisibility(View.VISIBLE);
                    rvNotification.setVisibility(View.INVISIBLE);
                }
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}
