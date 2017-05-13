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
import io.yostajsc.core.designs.decorations.SpacesItemDecoration;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.IgNotification;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogNotification extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Activity mOwnerActivity = null;
    private NotificationAdapter adapter = null;
    private OnItemSelectListener mOnItemSelected = null;

    public DialogNotification(Context context, OnItemSelectListener onItemSelected) {
        super(context, R.style.CoreAppTheme_Dialog);

        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);

        this.mOnItemSelected = onItemSelected;
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
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
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

    public void setData(List<IgNotification> igNoti) {
        if (igNoti == null)
            return;
        adapter.replaceAll(igNoti);
    }

    public interface OnItemSelectListener {
        void selected(String s);
    }
}