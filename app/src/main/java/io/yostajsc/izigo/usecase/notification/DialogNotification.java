package io.yostajsc.izigo.usecase.notification;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.sdk.designs.decorations.SpacesItemDecoration;
import io.yostajsc.sdk.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.IgNotification;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogNotification extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Activity mOwnerActivity = null;
    private NotificationAdapter adapter = null;

    public DialogNotification(Context context) {
        super(context, R.style.CoreAppTheme_Dialog);

        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mOwnerActivity = getOwnerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_notification);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.CoreAppTheme_AnimDialog_Grow;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        onApplyViews();
    }

    private void onApplyViews() {
        this.adapter = new NotificationAdapter(mOwnerActivity);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(mOwnerActivity));
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mOwnerActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
    }

    public void setData(List<IgNotification> igNotifications) {
        if (igNotifications == null)
            return;
        adapter.replaceAll(igNotifications);
    }
}