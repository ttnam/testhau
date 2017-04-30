package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.OnClick;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.realm.trip.IgTrip;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogComment;
import io.yostajsc.sdk.api.IzigoApiManager;
import io.yostajsc.constants.RoleType;
import io.yostajsc.constants.TransferType;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.AppConfig;
import io.yostajsc.sdk.IzigoSdk;
import io.yostajsc.sdk.model.IGCallback;
import io.yostajsc.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.view.BottomSheetDialog;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripDetailActivity extends OwnCoreActivity {

    private static final String TAG = TripDetailActivity.class.getSimpleName();

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    @BindView(R.id.image_view)
    AppCompatImageView imageCover;

    @BindView(R.id.text_name)
    TextView textTripName;

    @BindView(R.id.text_creator_name)
    TextView textCreatorName;

    @BindView(R.id.image_creator_avatar)
    AppCompatImageView imageCreatorAvatar;

    @BindView(R.id.image_vehicle)
    AppCompatImageView imageVehicle;

    @BindView(R.id.text_vehicle)
    TextView textVehicle;

    @BindView(R.id.text_time_start)
    TextView textTimeStart;

    @BindView(R.id.text_time_end)
    TextView textTimeEnd;

    @BindView(R.id.text_from)
    TextView textFrom;

    @BindView(R.id.text_to)
    TextView textTo;

    @BindView(R.id.text_edit)
    TextView textEdit;

    @BindView(R.id.text_publish)
    TextView textPublish;

    private String tripId;
    private int mCurrentRoleType = RoleType.GUEST;
    private ImageryAdapter albumAdapter = null;
    private boolean mIsPublic = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        TripDetailActivityView.bind(this);
        onApplyViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        tripId = getIntent().getStringExtra(AppConfig.TRIP_ID);
        onApplyData();
    }

    @Override
    @OnClick(R.id.button_left)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TripDetailActivityView.unbind();
    }

    @OnClick(R.id.text_publish)
    public void onMakePublish() {
        mIsPublic = !mIsPublic;
        TripDetailActivityView.publishTrip(tripId, mIsPublic, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(TripDetailActivity.this, error);
            }
        }, mOnExpiredCallBack);
    }

    @Override
    public void onApplyViews() {

        UiUtils.onApplyWebViewSetting(webView);

        this.albumAdapter = new ImageryAdapter(this);
        this.rvAlbum.setAdapter(this.albumAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.rvAlbum);
        this.rvAlbum.setHasFixedSize(true);
        this.rvAlbum.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvAlbum.setNestedScrollingEnabled(false);
        this.rvAlbum.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
        this.rvAlbum.setItemAnimator(new FadeInUpAnimator());
        this.rvAlbum.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TripDetailActivity.this, "showAlbum " + position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.image_view) {
            menu.add(0, v.getId(), 0, "Chọn từ thư viện");
            menu.add(0, v.getId(), 1, "Chụp từ thiết bị");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int order = item.getOrder();
        switch (order) {
            case 0:
            case 1:
                TripDetailActivityPermissionsDispatcher.getImageFromGalleryWithCheck(this);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @OnClick(R.id.layout_more)
    public void showMore() {
        PrefsUtils.inject(this).save(IgTrip.TRIP_ID, tripId);
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void onApplyData() {
        try {

            if (NetworkUtils.isNetworkConnected(this)) {
                IzigoSdk.TripExecutor.increaseTripView(tripId);
                loadTripFromServer();
            } else {
                onInternetDisConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInternetConnected() {
        super.onInternetConnected();
        onApplyData();
    }

    private void loadTripFromServer() {

        IzigoSdk.TripExecutor.getTripDetail(tripId, new IGCallback<IgTrip, String>() {
            @Override
            public void onSuccessful(IgTrip igTrip) {
                updateUI(igTrip);
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(TripDetailActivity.this, error);
            }

            @Override
            public void onExpired() {
                mOnExpiredCallBack.run();
            }
        });
    }

    private void updateUI(final IgTrip igTrip) {
        if (igTrip == null) return;
        albumAdapter.replaceAll(igTrip.getAlbum());
        mIsPublic = igTrip.isPublished();
        mCurrentRoleType = igTrip.getRole();
        TripDetailActivityView.setPublishMode(mIsPublic);                               // Publish
        TripDetailActivityView.switchMode(mCurrentRoleType);                            // Mode, is publish
        TripDetailActivityView.setTripName(igTrip.getName());                           // IgTrip name
        TripDetailActivityView.setVehicle(igTrip.getTransfer());                        // Transfer
        TripDetailActivityView.setTripCover(igTrip.getCoverUrl());                      // Cover
        TripDetailActivityView.setOwnerName(igTrip.getCreatorName());                   // Own name
        TripDetailActivityView.setOwnerAvatar(igTrip.getCreatorAvatar());               // Avatar
        TripDetailActivityView.setFromTo(igTrip.getFrom(), igTrip.getTo());             // From, To
        TripDetailActivityView.showTripDescription(igTrip.getDescription());            // Description
        TripDetailActivityView.setTime(igTrip.getDepartTime(), igTrip.getArriveTime()); // Time
    }

    @OnClick(R.id.layout_comment)
    public void loadComment() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
        dialogComment.setTripId(tripId);
        dialogComment.setTripName(tripId);
    }

    @OnClick(R.id.image_view)
    public void showContextMenu(AppCompatImageView button) {
        if (mCurrentRoleType == RoleType.ADMIN) {
            button.performLongClick();
        }
    }

    @OnClick(R.id.layout_vehicle)
    public void onTransfer(View view) {
        if (!view.isClickable())
            return;
        if (mCurrentRoleType == RoleType.ADMIN) {
            DialogPickTransfer dialogPickTransfer = new DialogPickTransfer(this);
            dialogPickTransfer.setDialogResult(new CallBackWith<Integer>() {
                @Override
                public void run(@TransferType Integer type) {
                    TripDetailActivityView.setVehicle(type);
                }
            });
            dialogPickTransfer.show();
        }
    }
/*
    @OnClick(R.id.button)
    public void actionLink() {
        if (mCurrentRoleType == RoleType.GUEST) {
            IzigoApiManager.connect().apiJoinGroup(tripId, new CallBack() {
                @Override
                public void run() {
                    Toast.makeText(TripDetailActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    AppConfig.showToast(TripDetailActivity.this, error);
                }
            }, mOnExpiredCallBack);
        } else {
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        }
    }*/

    @OnClick(R.id.layout_activity)
    public void loadActivity() {
        PrefsUtils.inject(this).save(IgTrip.TRIP_ID, tripId);
        Intent intent = new Intent(TripDetailActivity.this, TripTimelineActivity.class);
        intent.putExtra(IgTrip.TRIP_ID, tripId);
        startActivity(intent);
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MessageType.FROM_GALLERY: {
                    try {
                        Uri fileUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                        if (bitmap != null) {

                            // Update UI
                            Glide.with(TripDetailActivity.this)
                                    .load(fileUri)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(imageCover);

                            // Update server
                            TripDetailActivityView.changeTripCover(tripId, fileUri, new CallBackWith<String>() {
                                @Override
                                public void run(String error) {
                                    AppConfig.showToast(TripDetailActivity.this, error);
                                }
                            }, mOnExpiredCallBack);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
