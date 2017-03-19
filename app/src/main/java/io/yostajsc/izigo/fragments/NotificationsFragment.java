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
import butterknife.OnClick;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.interfaces.OnConnectionTimeoutListener;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.models.notification.Notifications;
import io.yostajsc.view.OwnToolBar;

public class NotificationsFragment extends BaseFragment {

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    @BindView(R.id.recycler_view)
    RecyclerView rvNotification;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.own_toolbar)
    OwnToolBar ownToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        onApplyData();
        return rootView;
    }

    private void onApplyViews() {
        ownToolbar.setLeft(R.drawable.ic_vector_back_blue, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHomePage();
            }
        });

    }

    private void onApplyData() {

        String authorization = StorageUtils.inject(mContext).getString(AppDefine.AUTHORIZATION);

        APIManager.connect(new OnConnectionTimeoutListener() {
            @Override
            public void onConnectionTimeout() {

            }
        }).getNotification(authorization, new CallBack() {
            @Override
            public void run() {


            }
        }, new CallBackWith<Notifications>() {
            @Override
            public void run(Notifications notifications) {
                int size = notifications.size();
                if (size > 0) {
                    layoutEmpty.setVisibility(View.INVISIBLE);
                    rvNotification.setVisibility(View.VISIBLE);
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
        });
    }

    @OnClick(R.id.button)
    public void viewHomePage() {
        ((MainActivity) getActivity()).move(0);
    }



}
