package io.yostajsc.izigo.usecase.trip;

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
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.utils.ToastUtils;
import io.yostajsc.izigo.usecase.MembersActivity;
import io.yostajsc.izigo.usecase.trip.adapter.TimelineAdapter;
import io.yostajsc.izigo.usecase.map.MapsActivity;
import io.yostajsc.sdk.model.IgTimeline;
import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogComment;
import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.izigo.constants.TransferType;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.izigo.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.usecase.customview.BottomSheetDialog;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripDetailActivity extends OwnCoreActivity {

    private static final String TAG = TripDetailActivity.class.getSimpleName();

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    @BindView(R.id.recycler_view_time_line)
    RecyclerView rVTimeLine;

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

    @BindView(R.id.text_status)
    TextView textStatus;

    @BindView(R.id.layout_maps)
    FrameLayout layoutMaps;

    @BindView(R.id.layout_history)
    FrameLayout layoutHistory;

    @BindView(R.id.switch_publish)
    Switch switchPublish;

    @BindView(R.id.button_action)
    Button buttonAction;

    private int mCurrentRoleType = RoleType.GUEST;
    private IgTrip mIgTrip = null;
    private ImageryAdapter albumAdapter = null;
    private TimelineAdapter timelineAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        onApplyViews();

        onApplyData();
    }

    @Override
    @OnClick(R.id.button_left)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onApplyViews() {

        UiUtils.onApplyWebViewSetting(webView);

        // Album
        this.albumAdapter = new ImageryAdapter(this);
        this.rvAlbum.setAdapter(this.albumAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.rvAlbum);
        this.rvAlbum.setHasFixedSize(true);
        this.rvAlbum.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvAlbum.setNestedScrollingEnabled(false);
        this.rvAlbum.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        this.rvAlbum.setItemAnimator(new FadeInUpAnimator());
        this.rvAlbum.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlbumActivity();
            }
        }));

        // IgTimeline
        this.timelineAdapter = new TimelineAdapter(this);
        UiUtils.onApplyRecyclerView(this.rVTimeLine, this.timelineAdapter, new SlideInUpAnimator(), null);
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
                TripDetailActivityPermissionsDispatcher.getImageFromGalleryWithCheck(this);
            case 1:
                TripDetailActivityPermissionsDispatcher.onTakePhotoWithCheck(this);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @OnClick(R.id.layout_more)
    public void showMore() {
        PrefsUtils.inject(this).save(IgTrip.TRIP_ID, AppConfig.getInstance().getCurrentTripId());
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void onApplyData() {
        if (NetworkUtils.isNetworkConnected(this)) {

            IzigoSdk.TripExecutor.increaseTripView(AppConfig.getInstance().getCurrentTripId());

            IzigoSdk.TripExecutor.getTripDetail(AppConfig.getInstance().getCurrentTripId(), new IgCallback<IgTrip, String>() {
                @Override
                public void onSuccessful(IgTrip igTrip) {
                    // updateUI(igTrip);
                }

                @Override
                public void onFail(String error) {
                    ToastUtils.showToast(TripDetailActivity.this, error);
                }

                @Override
                public void onExpired() {
                    expired();
                }
            });


            IzigoSdk.TripExecutor.getActivities(AppConfig.getInstance().getCurrentTripId(), new IgCallback<List<IgTimeline>, String>() {
                @Override
                public void onSuccessful(List<IgTimeline> timelines) {
                    updateTimeline(timelines);
                }

                @Override
                public void onFail(String error) {
                    ToastUtils.showToast(TripDetailActivity.this, error);
                }

                @Override
                public void onExpired() {
                    expired();
                }
            });
        } else {
            onInternetDisConnected();
        }
    }

    @Override
    public void onInternetConnected() {
        super.onInternetConnected();
        onApplyData();
        Log.e(TAG, "onInternetConnected");
    }

    private void updateTimeline(List<IgTimeline> timelines) {
        if (timelines != null && timelines.size() > 0) {
            this.timelineAdapter.replaceAll(timelines);
        } else {
            this.timelineAdapter.clear();
        }
    }

    @OnClick(R.id.layout_comment)
    public void loadComment() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
        dialogComment.setTripName(mIgTrip.getName());
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
                    TripActivityView.setVehicle(type);
                }
            });
            dialogPickTransfer.show();
        }
    }

    @OnClick(R.id.switch_publish)
    public void togglePublish() {

        IzigoSdk.TripExecutor.publishTrip(AppConfig.getInstance().getCurrentTripId(), switchPublish.isChecked(), new IgCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                ToastUtils.showToast(TripDetailActivity.this, getString(R.string.str_success));
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(TripDetailActivity.this, error);
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    @OnClick(R.id.layout_show_album)
    public void showAlbumActivity() {
        Intent intent = new Intent(this, TripAlbumActivity.class);
        intent.putExtra(AppConfig.KEY_USER_ROLE, mIgTrip.getRole());
        startActivityForResult(intent, MessageType.PICK_IMAGE);
    }


    @OnClick(R.id.button_action)
    public void makeAction() {
        if (mCurrentRoleType == RoleType.GUEST) {
            IzigoSdk.TripExecutor.join(AppConfig.getInstance().getCurrentTripId(), new IgCallback<Void, String>() {
                @Override
                public void onSuccessful(Void aVoid) {
                    ToastUtils.showToast(TripDetailActivity.this, "Đã gửi request.");
                }

                @Override
                public void onFail(String error) {
                    ToastUtils.showToast(TripDetailActivity.this, error);
                }

                @Override
                public void onExpired() {
                    expired();
                }
            });
        } else {
            startActivity(new Intent(TripDetailActivity.this, MembersActivity.class));
        }
    }

    @OnClick(R.id.layout_maps)
    public void loadMapViews() {
        TripDetailActivityPermissionsDispatcher.loadMapsWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public void loadMaps() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MessageType.FROM_GALLERY);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    public void onTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, MessageType.TAKE_PHOTO);
        }
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
                case MessageType.FROM_GALLERY:
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
                            TripActivityView.changeTripCover(AppConfig.getInstance().getCurrentTripId(), fileUri, new CallBackWith<String>() {
                                @Override
                                public void run(String error) {
                                    ToastUtils.showToast(TripDetailActivity.this, error);
                                }
                            }, new CallBack() {
                                @Override
                                public void run() {
                                    expired();
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.TAKE_PHOTO:

                    break;
                case MessageType.PICK_IMAGE:
                    ArrayList<String> res = data.getStringArrayListExtra("PICK_IMAGE");
                    if (res.size() > 0) {
                        for (String url : res) {
                            albumAdapter.add(new IgImage(url));
                        }
                    }
                    break;
            }
        }
    }
}
