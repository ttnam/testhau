package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.TimelineAdapter;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.models.view.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripDetailActivity extends ActivityBehavior {

    @BindView(R.id.text_view)
    AppCompatTextView txtTitle;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView rvNote;

    @BindView(R.id.image_view)
    AppCompatImageView imageCover;

    private TimelineAdapter mTimelineAdapter = null;
/*
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }*/

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
        onInitializeData();

        onApplyRecyclerView();
    }
/*
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }*/

    private void onInitializeWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
    }

    private void onInitializeData() {

        Intent intent = this.getIntent();
        FirebaseTrip trip = (FirebaseTrip) intent.getSerializableExtra(AppUtils.EXTRA_TRIP);
        onUpdateData(trip);
        this.mTimelineAdapter = new TimelineAdapter(this);
        this.mTimelineAdapter.clear();
        for (int i = 0; i < 10; i++) {
            this.mTimelineAdapter.add(new TimelineView(""));
        }
    }

    private void onUpdateData(FirebaseTrip trip) {
        if (trip == null) return;

        String prefix = "<html><body><p style=\"text-align: justify\">";
        String postfix = "</p></body></html>";
        String content = trip.getDescription();

        webView.loadData(prefix + content + postfix, "text/html; charset=utf-8", "utf-8");
        txtTitle.setText(trip.getName());

        Glide.with(this).load(trip.getCover()).into(imageCover);
    }

    private void onApplyRecyclerView() {
        this.rvNote.setAdapter(this.mTimelineAdapter);
        this.rvNote.setHasFixedSize(true);
        this.rvNote.setItemAnimator(new SlideInUpAnimator());
        this.rvNote.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvNote.addItemDecoration(new SpacesItemDecoration(0));
        this.rvNote.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvNote.setLayoutManager(layoutManager);
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();
    }
}
