package io.yostajsc.izigo.activities.trip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import io.yostajsc.backend.config.APIManager;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.dialogs.DialogComment;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.interfaces.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.utils.AppUtils;
import io.yostajsc.utils.NetworkUtils;
import io.yostajsc.utils.StorageUtils;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.utils.validate.ValidateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.view.BottomSheetDialog;
import io.yostajsc.view.CropCircleTransformation;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripDetailActivity extends ActivityBehavior {

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView rvActivity;

    @BindView(R.id.image_view)
    AppCompatImageView imageCover;

    @BindView(R.id.text_number_of_comment)
    TextView textNumberOfComments;

    @BindView(R.id.text_name)
    TextView textTripName;

    @BindView(R.id.text_creator_name)
    TextView textCreatorName;

    @BindView(R.id.text_views)
    TextView textViews;

    @BindView(R.id.image_creator_avatar)
    AppCompatImageView imageCreatorAvatar;

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.text_number_of_photo)
    TextView textNumberOfPhoto;

    @BindView(R.id.text_activities)
    TextView textNumberOfActivities;

    private String tripId;

    /*

        private FirebaseManager mFirebaseUtils = null;

    */
    /*private FirebaseActivityAdapter mActivityAdapter = null;*/

    private ImageryAdapter albumAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        // this.mFirebaseUtils = FirebaseManager.inject(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    @OnClick({R.id.button_left, R.id.image_view})
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onApplyViews() {
        this.albumAdapter = new ImageryAdapter(this);
        UiUtils.onApplyWebViewSetting(webView);
        UiUtils.onApplyAlbumRecyclerView(this.rvActivity, albumAdapter, new SlideInUpAnimator(), new CallBackWith<Integer>() {
            @Override
            public void run(Integer integer) {

            }
        });
    }

    @Override
    public void onApplyData() {
        Intent intent = this.getIntent();
        tripId = intent.getStringExtra(AppDefine.TRIP_ID);
        if (ValidateUtils.canUse(tripId)) {
            // Read from disk
        /*    RealmManager.findTripById(tripId, new CallBackWith<Trip>() {
                @Override
                public void run(Trip trip) {
                    updateUI(trip);
                }
            });*/

            if (NetworkUtils.isNetworkConnected(this)) {
                onInternetConnected();
            }
        }
/*

        onUpdateData(mCurrTrip);
        String tripId = mCurrTrip.getTripId();
        // this.activityAdapter = new FirebaseActivityAdapter(FirebaseManager.inject(this).ACTIVITYRef(tripId));

        onCommentListener(tripId);
        onRankingListener(tripId);*/

    }

    private void updateUI(Trip trip) {
        new AsyncTask<Trip, Void, Trip>() {

            @Override
            protected Trip doInBackground(Trip... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(Trip trip) {
                super.onPostExecute(trip);

                if (trip == null) return;
                String prefix = "<html><body><p style=\"text-align: justify\">";
                String postfix = "</p></body></html>";
                String content = trip.getDescription();
                if (ValidateUtils.canUse(content)) {
                    webView.loadData(prefix + content + postfix, "text/html; charset=utf-8", "utf-8");
                    webView.setVisibility(View.VISIBLE);
                } else {
                    webView.setVisibility(View.GONE);
                }

                // Cover
                Glide.with(TripDetailActivity.this)
                        .load(trip.getCover())
                        .into(imageCover);

                String tripName = trip.getTripName();
                if (ValidateUtils.canUse(tripName)) {
                    textTripName.setText(tripName);
                    textTripName.setVisibility(View.VISIBLE);
                } else
                    textTripName.setVisibility(View.GONE);

                textCreatorName.setText(trip.getCreatorName());

                // Avatar
                Glide.with(TripDetailActivity.this)
                        .load(trip.getCreatorAvatar())
                        .error(R.drawable.ic_vector_avatar)
                        .bitmapTransform(new CropCircleTransformation(TripDetailActivity.this))
                        .into(imageCreatorAvatar);

                albumAdapter.replaceAll(trip.getAlbum());

                // Photos
                int nPhotos = trip.getAlbum().size();
                textNumberOfPhoto.setText(getResources().getQuantityString(R.plurals.photos, nPhotos, nPhotos));

                int nComments = Integer.parseInt(trip.getNumberOfComments());
                textNumberOfComments.setText(getResources().getQuantityString(R.plurals.comments, nComments, nComments));

                int nViews = Integer.parseInt(trip.getNumberOfView());
                textViews.setText(getResources().getQuantityString(R.plurals.views, nViews, nViews));

                int nActivities = Integer.parseInt(trip.getNumberOfActivities());
                textNumberOfActivities.setText(getResources().getQuantityString(R.plurals.activities, nActivities, nActivities));

                // Time
                textTime.setText(String.format("%s - %s",
                        AppUtils.builder().getTime(trip.getDepartTime(), AppUtils.DD_MM_YYYY),
                        AppUtils.builder().getTime(trip.getArriveTime(), AppUtils.DD_MM_YYYY)));


            }
        }.execute(trip);
    }


    @OnClick(R.id.button_share)
    public void share() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @OnClick(R.id.layout_comment)
    public void onLoadComment() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
        dialogComment.setTripId(tripId);
    }

    /*
        @OnClick(R.id.layout_rating)
        public void onOpenRating() {
            startActivity(new Intent(this, RatingActivity.class));
        }



    @OnClick(R.id.btn_ranking)
    public void onRequestRanking() {
        long ranking = Long.parseLong(btnRanking.getText().toString());
        ranking = ranking + 1;
        final String result = String.valueOf(ranking);

        final long finalRanking = ranking;
       *//* new StandardDialog(this)
                .setButtonsColor(getResources().getColor(R.color.PureRed))
                .setCancelable(false)
                .setTopColorRes(android.R.color.white)
                .setTopColor(getResources().getColor(android.R.color.white))
                .setTopColorRes(R.color.PureRed)
                .setNegativeButton(R.string.message_later, null)
                .setMessage("If you like this trip please rate for us. Thank you!!")
                .setPositiveButton("Rate Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFirebaseUtils.onChangeRanking(mCurrTrip.getTripId(), finalRanking,
                                new CallBack() {
                                    @Override
                                    public void run() {
                                        btnRanking.setText(String.valueOf(result));
                                    }
                                });
                    }
                })
                .show();*//*
    }*/
/*
/*

    // Ranking listener
    private void onRankingListener(String tripId) {
        */
/*mFirebaseUtils.TRIP().child(tripId).child("ranking").addValueEventListener(new ValueEventListener() {
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
        });*//*

    }

    private void onCommentListener(String tripId) {
       */
/* mFirebaseUtils.TRIP().child(tripId).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtComment.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*//*

    }
*/
    @Override
    protected void onInternetConnected() {
        super.onInternetConnected();
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
        if (ValidateUtils.canUse(authorization)) {
            APIManager.connect().getTripDetail(authorization, tripId, new CallBackWith<Trip>() {
                @Override
                public void run(Trip trip) {
                    // RealmManager.insertOrUpdate(trip);
                    updateUI(trip);
                }
            }, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(TripDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            onExpired();
        }
    }

    @Override
    protected void onInternetDisconnected() {

    }

}
