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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.OnClick;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogComment;
import io.yostajsc.usecase.backend.core.APIManager;
import io.yostajsc.constants.RoleType;
import io.yostajsc.constants.TransferType;
import io.yostajsc.constants.TripTypePermission;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.AppConfig;
import io.yostajsc.usecase.backend.core.ApiCaller;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.utils.PrefsUtil;
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

    @BindView(R.id.switch_publish)
    Switch switchPublish;

    private String tripId;
    private int mCurrentRoleType = RoleType.GUEST;
    private ImageryAdapter albumAdapter = null;
    private boolean mIsPublic = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        onApplyViews();

        // Get data from intent
        tripId = getIntent().getStringExtra(AppConfig.TRIP_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onApplyData();
    }

    @Override
    @OnClick(R.id.button_left)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.switch_publish)
    public void onMakePublish() {
        mIsPublic = switchPublish.isChecked();
        Toast.makeText(this, mIsPublic ? "Yes" : "No", Toast.LENGTH_SHORT).show();
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
            case 2:
                makeTripPublic(false);
                break;
            case 3:
                makeTripPublic(true);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @OnClick(R.id.layout_more)
    public void showMore() {
        PrefsUtil.inject(this).save(Trip.TRIP_ID, tripId);
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void makeTripPublic(boolean isPublic) {
        try {
            String params = "0";
            if (isPublic) {
                params = "1";
            }
            APIManager.connect().updateTripInfo(tripId, params, TripTypePermission.STATUS, new CallBack() {
                @Override
                public void run() {
                    AppConfig.showToast(TripDetailActivity.this, getString(R.string.str_success));
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    AppConfig.showToast(TripDetailActivity.this, error);
                }
            }, mOnExpiredCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApplyData() {
        try {

            if (NetworkUtils.isNetworkConnected(this)) {
                ApiCaller.callApiUpdateTripView(tripId);
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
        APIManager.connect().getTripDetail(tripId, new CallBackWith<Trip>() {
            @Override
            public void run(Trip trip) {
                updateUI(trip);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(TripDetailActivity.this, error);
            }
        }, mOnExpiredCallBack);
    }

    private void updateUI(final Trip trip) {
        if (trip == null) return;
        albumAdapter.replaceAll(trip.getAlbum());
        mIsPublic = trip.isPublished();
        mCurrentRoleType = trip.getRole();
        TripDetailActivityView.inject(this)
                .switchMode(mCurrentRoleType)                           // Mode, is publish
                .setTripCover(trip.getCover())                          // Cover
                .setTripName(trip.getTripName())                        // Trip name
                .setVehicle(trip.getTransfer())                         // Transfer
                .setOwnerName(trip.getCreatorName())                    // Own name
                .setOwnerAvatar(trip.getCreatorAvatar())                // Avatar
                .showTripDescription(trip.getDescription())             // Description
                .setFromTo(trip.getFrom(), trip.getTo())                // From, To
                .setTime(trip.getDepartTime(), trip.getArriveTime());   // Time
    }

    private void onApplyFireBase(Uri uri) {

        if (NetworkUtils.isNetworkConnected(this)) {

            // Fire Base
            StorageReference riversRef = FirebaseStorage.getInstance()
                    .getReference().child("images/covers/" + tripId);

            UploadTask uploadTask = riversRef.putFile(uri);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    updateTrip(task.getResult().getDownloadUrl().toString(), TripTypePermission.COVER);
                }
            });
        }

    }

    private void updateTrip(String data, @TripTypePermission int type) {

        APIManager.connect().updateTripInfo(tripId, data, type, new CallBack() {
            @Override
            public void run() {
                Toast.makeText(TripDetailActivity.this, getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(TripDetailActivity.this, error);
            }
        }, mOnExpiredCallBack);
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
                    TripDetailActivityView.inject(TripDetailActivity.this).setVehicle(type);
                }
            });
            dialogPickTransfer.show();
        }
    }
/*
    @OnClick(R.id.button)
    public void actionLink() {
        if (mCurrentRoleType == RoleType.GUEST) {
            APIManager.connect().apiJoinGroup(tripId, new CallBack() {
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
        PrefsUtil.inject(this).save(Trip.TRIP_ID, tripId);
        Intent intent = new Intent(TripDetailActivity.this, TripTimelineActivity.class);
        intent.putExtra(Trip.TRIP_ID, tripId);
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
                            Glide.with(TripDetailActivity.this)
                                    .load(fileUri)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(imageCover);
                            onApplyFireBase(fileUri);
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
