package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.FirebaseUtils;
import com.yosta.phuotngay.firebase.adapter.FirebaseActivityAdapter;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.ui.bottomsheet.BottomSheetDialog;
import com.yosta.phuotngay.ui.OwnToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripDetailActivity extends ActivityBehavior {

    @BindView(R.id.text_view)
    AppCompatTextView txtTitle;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView rvActivity;

    @BindView(R.id.image_view)
    AppCompatImageView imageCover;

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.btn_ranking)
    Button btnRanking;

    private FirebaseActivityAdapter activityAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        onApplyComponents();
        onApplyData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.rvActivity.setAdapter(this.activityAdapter);
    }


    @Override
    public void onApplyComponents() {
        super.onApplyComponents();

        // Toobar
        mOwnToolbar.setBinding(
                R.drawable.ic_vector_back_white,
                R.drawable.ic_vector_share_white,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    }
                });


        onApplyWebView();

        onApplyRecyclerView();
    }

    private void onApplyWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDefaultFontSize(14);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setBlockNetworkImage(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setGeolocationEnabled(false);
        settings.setNeedInitialFocus(false);
        settings.setSaveFormData(false);
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        Intent intent = this.getIntent();
        FirebaseTrip trip = (FirebaseTrip) intent.getSerializableExtra(AppUtils.EXTRA_TRIP);
        onUpdateData(trip);
        String tripId = trip.getTripId();
        this.activityAdapter = new FirebaseActivityAdapter(FirebaseUtils.initializeWith(this).ACTIVITYRef(tripId));
    }
    /*
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }*/

    private void onUpdateData(FirebaseTrip trip) {
        if (trip == null) return;

        String prefix = "<html><body><p style=\"text-align: justify\">";
        String postfix = "</p></body></html>";
        String content = trip.getDescription();
        webView.loadData(prefix + content + postfix, "text/html; charset=utf-8", "utf-8");
        txtTitle.setText(trip.getName());
        btnRanking.setText(String.valueOf(trip.getRanking()));
        Glide.with(this).load(trip.getCover()).into(imageCover);
    }

    private void onApplyRecyclerView() {
        this.rvActivity.setHasFixedSize(true);
        this.rvActivity.setItemAnimator(new SlideInUpAnimator());
        this.rvActivity.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvActivity.addItemDecoration(new SpacesItemDecoration(0));
        this.rvActivity.setNestedScrollingEnabled(false);
        this.rvActivity.setHorizontalScrollBarEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.rvActivity.setLayoutManager(layoutManager);
    }
}
