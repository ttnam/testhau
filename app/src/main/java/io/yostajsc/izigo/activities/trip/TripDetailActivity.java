package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.constants.MessageType;
import io.yostajsc.constants.RoleType;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.dialogs.DialogComment;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.interfaces.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.utils.AppUtils;
import io.yostajsc.utils.DimensionUtil;
import io.yostajsc.utils.NetworkUtils;
import io.yostajsc.utils.StorageUtils;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.utils.validate.ValidateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.view.BottomSheetDialog;
import io.yostajsc.view.CropCircleTransformation;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripDetailActivity extends ActivityBehavior {

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

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

    @BindView(R.id.image_transfer)
    AppCompatImageView imageTransfer;

    @BindView(R.id.image_edit_cover)
    AppCompatImageView imageEditCover;

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.text_number_of_photo)
    TextView textNumberOfPhoto;

    @BindView(R.id.text_activities)
    TextView textNumberOfActivities;

    private String tripId;

    private ImageryAdapter albumAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
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

        // set cover size
       /* layoutCover.setLayoutParams(new FrameLayout.LayoutParams(
                DimensionUtil.getScreenWidth(this),
                DimensionUtil.getScreenWidth(this) * 2 / 3
        ));*/
        this.albumAdapter = new ImageryAdapter(this);
        UiUtils.onApplyWebViewSetting(webView);
        UiUtils.onApplyAlbumRecyclerView(this.rvAlbum, albumAdapter, new SlideInUpAnimator(), new CallBackWith<Integer>() {
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
            RealmManager.findTripById(tripId, new CallBackWith<Trip>() {
                @Override
                public void run(Trip trip) {
                    updateUI(trip);
                }
            });

            if (NetworkUtils.isNetworkConnected(this)) {
                onInternetConnected();
            }
        }
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

                textViews.setText(String.valueOf(trip.getNumberOfView()));

                int nPhotos = trip.getAlbum().size();
                textNumberOfPhoto.setText(getResources().getQuantityString(R.plurals.photos, nPhotos, nPhotos));

                textNumberOfComments.setText(String.valueOf(trip.getNumberOfComments()));
                textNumberOfActivities.setText(String.valueOf(trip.getNumberOfActivities()));

                // Time
                textTime.setText(String.format("%s - %s",
                        AppUtils.builder().getTime(trip.getDepartTime(), AppUtils.DD_MM_YYYY),
                        AppUtils.builder().getTime(trip.getArriveTime(), AppUtils.DD_MM_YYYY)));

                // Update transfer
                UiUtils.showTransfer(trip.getTransfer(), imageTransfer);

                // Update role

                @RoleType int role = trip.getRole();
                switch (role) {
                    case RoleType.ADMIN:
                        turnToAdminMode();
                        break;
                    case RoleType.MEMBER:
                        turnToMemberMode();
                        break;
                    case RoleType.NOT_MEMBER:
                        turnToGuestMode();
                        break;
                }


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

    @OnClick(R.id.layout_activity)
    public void onLoadActivity() {
        Intent intent = new Intent(TripDetailActivity.this, TripTimelineActivity.class);
        intent.putExtra(Trip.TRIP_ID, tripId);
        startActivity(intent);
    }

    @OnClick(R.id.image_edit_cover)
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MessageType.FROM_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TripDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /*
        private void onCommentListener(String tripId) {
     mFirebaseUtils.TRIP().child(tripId).child("comment").addValueEventListener(new ValueEventListener() {
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
                    RealmManager.insertOrUpdate(trip);
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


    private void turnToGuestMode() {
        imageEditCover.setVisibility(View.GONE);
        Toast.makeText(this, "Guest", Toast.LENGTH_SHORT).show();
    }

    private void turnToMemberMode() {
        imageEditCover.setVisibility(View.GONE);
        Toast.makeText(this, "Member", Toast.LENGTH_SHORT).show();
    }

    private void turnToAdminMode() {
        imageEditCover.setVisibility(View.VISIBLE);
        TripDetailActivityPermissionsDispatcher.getImageFromGalleryWithCheck(this);
    }
}
