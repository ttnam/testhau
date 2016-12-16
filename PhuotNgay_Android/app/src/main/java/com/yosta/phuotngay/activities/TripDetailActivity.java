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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yosta.materialdialog.StandardDialog;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.dialogs.DialogComment;
import com.yosta.phuotngay.firebase.FirebaseUtils;
import com.yosta.phuotngay.firebase.adapter.FirebaseActivityAdapter;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.interfaces.CallBackListener;
import com.yosta.phuotngay.ui.bottomsheet.BottomSheetDialog;
import com.yosta.phuotngay.ui.OwnToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.tV_comment)
    TextView txtComment;

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.btn_ranking)
    Button btnRanking;

    private FirebaseTrip mCurrTrip = null;
    private FirebaseUtils mFirebaseUtils = null;
    private FirebaseActivityAdapter activityAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        this.mFirebaseUtils = FirebaseUtils.initializeWith(this);


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
        mCurrTrip = (FirebaseTrip) intent.getSerializableExtra(AppUtils.EXTRA_TRIP);
        onUpdateData(mCurrTrip);
        String tripId = mCurrTrip.getTripId();
        this.activityAdapter = new FirebaseActivityAdapter(FirebaseUtils.initializeWith(this).ACTIVITYRef(tripId));

        onCommentListener(tripId);
        onRankingListener(tripId);

    }
    /*
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }*/

    @OnClick(R.id.layout_comment)
    public void onLoadComment() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
    }

    @OnClick(R.id.btn_ranking)
    public void onRequestRanking() {
        long ranking = Long.parseLong(btnRanking.getText().toString());
        ranking = ranking + 1;
        final String result = String.valueOf(ranking);

        final long finalRanking = ranking;
        new StandardDialog(this)
                .setButtonsColor(getResources().getColor(R.color.PureRed))
                .setCancelable(false)
                .setTopColorRes(android.R.color.white)
                .setTopColor(getResources().getColor(android.R.color.white))
                .setTopColorRes(R.color.PureRed)
                .setPositiveButton("Later", null)
                .setMessage("If you like this trip please rate for us. Thank you!!")
                .setNegativeButton("Rate Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFirebaseUtils.onChangeRanking(mCurrTrip.getTripId(), finalRanking,
                                new CallBackListener() {
                                    @Override
                                    public void run() {
                                        btnRanking.setText(String.valueOf(result));
                                    }
                                });
                    }
                })
                .show();
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

    // Ranking listener
    private void onRankingListener(String tripId) {
        mFirebaseUtils.TRIP().child(tripId).child("ranking").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long ranking = (long) dataSnapshot.getValue();
                    btnRanking.setText(String.valueOf(ranking));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onCommentListener(String tripId) {
        mFirebaseUtils.TRIP().child(tripId).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtComment.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onApplyRecyclerView() {
        this.rvActivity.setHasFixedSize(true);
        this.rvActivity.setItemAnimator(new SlideInUpAnimator());
        this.rvActivity.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvActivity.addItemDecoration(new SpacesItemDecoration(0));
        this.rvActivity.setNestedScrollingEnabled(false);
        this.rvActivity.setHorizontalScrollBarEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        this.rvActivity.setLayoutManager(layoutManager);
    }
}
