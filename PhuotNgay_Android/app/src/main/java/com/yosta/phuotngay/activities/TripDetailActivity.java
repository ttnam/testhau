package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogComment;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.helpers.listeners.ItemClickSupport;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.models.trip.FirebaseTrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripDetailActivity extends ActivityBehavior {

    @BindView(R.id.text_view)
    AppCompatTextView txtTitle;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.rv_note)
    RecyclerView rvNote;

    @BindView(R.id.layout)
    SwipeRefreshLayout swipeRefreshLayout;


    @BindView(R.id.image_view)
    AppCompatImageView imageCover;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideLeftRight;
        }
        onInitializeWebView();
        onInitializeRecyclerView();
        onInitializeData();
    }

    private void onInitializeWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
    }

    private void onInitializeData() {

        Intent intent = this.getIntent();
        FirebaseTrip trip = (FirebaseTrip) intent.getSerializableExtra(AppUtils.EXTRA_TRIP);

        String prefix = "<html><body><p style=\"text-align: justify\">";
        String postfix = "</p></body></html>";
        String content = trip.getDescription();

        webView.loadData(prefix + content + postfix, "text/html; charset=utf-8", "utf-8");
        txtTitle.setText(trip.getName());

        Glide.with(this).load(trip.getCover()).into(imageCover);
    }

    private void onInitializeRecyclerView() {
        rvNote.setHasFixedSize(true);
        rvNote.setItemAnimator(new DefaultItemAnimator());
        rvNote.addItemDecoration(new SpacesItemDecoration(2));
        rvNote.setNestedScrollingEnabled(false);
        rvNote.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        rvNote.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvNote);
        ItemClickSupport.addTo(rvNote).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // do it
                    }
                }
        );

    }

    @OnClick(R.id.layout_comment)
    public void onShowCommentDialog() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
