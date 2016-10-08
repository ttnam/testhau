package com.yosta.phuotngay.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.interfaces.DialogBehavior;
import com.yosta.phuotngay.adapters.SearchItemAdapter;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.sliders.SlideInLeftAnimator;
import com.yosta.phuotngay.animations.sliders.SlideInRightAnimator;
import com.yosta.phuotngay.helpers.listeners.ListenerHelpers;
import com.yosta.phuotngay.models.views.SearchItemView;
import com.yosta.phuotngay.helpers.viewholders.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class SearchDialog extends Dialog implements DialogBehavior {

    @BindView(R.id.image)
    AppCompatImageView imgBack;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.container)
    RelativeLayout dismissContainer;

    private Activity owner = null;
    private SearchItemAdapter searchItemAdapter = null;

    public SearchDialog(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        onAttachedWindow(context);
        searchItemAdapter = new SearchItemAdapter(context);
    }

    @Override
    public void onAttachedWindow(Context context) {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }
        owner = (context instanceof Activity) ? (Activity) context : null;
        if (owner != null)
            setOwnerActivity(owner);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        owner = getOwnerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_search);
        ButterKnife.bind(this);

        onApplyComponents();
        onApplyData();
        onApplyEvents();
    }

    @Override
    @OnClick(R.id.image)
    public void onClose() {
        dismiss();
    }

    private void onInitializeRecyclerView() {
        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration());
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(getOwnerActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(searchItemAdapter);
    }

    @Override
    public void onApplyComponents() {

        YoYo.with(new SlideInLeftAnimator()).duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(imgBack);

        YoYo.with(new SlideInRightAnimator()).duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(editText);

        onInitializeRecyclerView();

    }

    @Override
    public void onApplyEvents() {
        editText.setOnEditorActionListener(ListenerHelpers.onEditorSearchChange);
    }

    @Override
    public void onApplyData() {
        for (int i = 0; i < 10; i++) {
            searchItemAdapter.addView(new SearchItemView());
        }
    }
}