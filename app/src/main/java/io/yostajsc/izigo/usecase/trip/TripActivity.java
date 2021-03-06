package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.gallery.ImageNormalViewHolder;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.gallery.SelectorImageryAdapter;
import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.izigo.usecase.trip.dialog.DialogComment;
import io.yostajsc.izigo.usecase.trip.dialog.DialogTripStatus;
import io.yostajsc.izigo.usecase.MembersActivity;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.usecase.map.MapsActivity;
import io.yostajsc.izigo.usecase.trip.adapter.TimelineAdapter;
import io.yostajsc.izigo.ui.UiUtils;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.IgTimeline;
import io.yostajsc.sdk.api.model.trip.IgImage;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.sdk.api.model.trip.IgTripStatus;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripActivity extends OwnCoreActivity {

    private final static String TAG = TripActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.image_creator_avatar)
    AppCompatImageView imageCreatorAvatar;

    @BindView(R.id.image_trip_cover)
    AppCompatImageView imageTripCover;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.text_name)
    TextView textTripName;

    @BindView(R.id.text_status)
    TextView textStatus;

    @BindView(R.id.text_time_start)
    TextView textTimeStart;

    @BindView(R.id.text_time_end)
    TextView textTimeEnd;

    @BindView(R.id.text_from)
    TextView textFrom;

    @BindView(R.id.text_to)
    TextView textTo;

    @BindView(R.id.image_vehicle)
    AppCompatImageView imageVehicle;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    @BindView(R.id.recycler_view_time_line)
    RecyclerView rVTimeLine;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.button_publish)
    ToggleButton buttonPublish;

    private SelectorImageryAdapter albumAdapter = null;
    private TimelineAdapter timelineAdapter = null;

    private IgTrip mIgTrip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);
        TripActivityHelper.bind(this);
        onApplyViews();
    }

    @Override
    protected void onDestroy() {
        TripActivityHelper.unbind();
        super.onDestroy();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        UiUtils.onApplyWebViewSetting(webView);

        // Album
        this.albumAdapter = new SelectorImageryAdapter(this, new ImageNormalViewHolder.OnClick() {
            @Override
            public void onClick(int position) {
                showAlbumActivity();
            }
        });
        this.rvAlbum.setAdapter(this.albumAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.rvAlbum);
        this.rvAlbum.setHasFixedSize(true);
        this.rvAlbum.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvAlbum.setNestedScrollingEnabled(false);
        this.rvAlbum.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        this.rvAlbum.setItemAnimator(new FadeInUpAnimator());
        this.timelineAdapter = new TimelineAdapter(this);
        UiUtils.onApplyRecyclerView(this.rVTimeLine, this.timelineAdapter, new SlideInUpAnimator(), null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        onApplyData();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        updateTripDetail();
        IzigoSdk.TripExecutor.getActivities(AppConfig.getInstance().getCurrentTripId(), new IgCallback<List<IgTimeline>, String>() {
            @Override
            public void onSuccessful(List<IgTimeline> timelines) {
                updateTimeline(timelines);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(TripActivity.this, error);
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    void updateTripDetail() {
        IzigoSdk.TripExecutor.getTripDetail(AppConfig.getInstance().getCurrentTripId(),
                new IgCallback<IgTrip, String>() {
                    @Override
                    public void onSuccessful(IgTrip igTrip) {
                        updateUI(igTrip);
                    }

                    @Override
                    public void onFail(String error) {
                        ToastUtils.showToast(TripActivity.this, error);
                    }

                    @Override
                    public void onExpired() {
                        expired();
                    }
                });
    }

    private void updateTimeline(List<IgTimeline> timelines) {
        if (timelines != null && timelines.size() > 0) {
            this.timelineAdapter.replaceAll(timelines);
        } else {
            this.timelineAdapter.clear();
        }
    }

    private void updateUI(final IgTrip igTrip) {

        if (igTrip == null) return;

        mIgTrip = igTrip;
        if (igTrip.getAlbum() != null && igTrip.getAlbum().size() > 0) {
            this.albumAdapter.clear();
            for (IgImage igImage : igTrip.getAlbum()) {
                this.albumAdapter.add(
                        new SelectorImageryAdapter.Imagery(igImage.getUrl()));
            }
        }

        TripActivityHelper.setTripName(igTrip.getName());
        TripActivityHelper.setVehicle(igTrip.getTransfer());                        // Transfer
        TripActivityHelper.setTripStatus(igTrip.getStatus());                       // Status
        TripActivityHelper.setTripCover(igTrip.getCoverUrl());
        TripActivityHelper.setOwnerAvatar(igTrip.getCreatorAvatar());
        TripActivityHelper.setFromTo(igTrip.getFrom(), igTrip.getTo());             // From, To
        TripActivityHelper.showTripDescription(igTrip.getDescription());            // Description
        TripActivityHelper.setTime(
                igTrip.getDepartTime(),
                igTrip.getArriveTime()); // Time
        TripActivityHelper.isPublish(igTrip.isPublished());                         // is publish
        TripActivityHelper.switchMode(igTrip.getRole());                            // Mode, is publish
    }

    @OnClick(R.id.button_open_menu)
    public void openMenu() {
        drawerLayout.openDrawer(Gravity.END);
    }

    private void closeMenu() {
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.layout_show_album)
    public void showAlbumActivity() {
        Intent intent = new Intent(this, TripAlbumActivity.class);
        intent.putExtra(AppConfig.KEY_USER_ROLE, mIgTrip.getRole());
        startActivity(intent);
    }

    @OnClick(R.id.layout_maps)
    public void loadMapViews() {
        closeMenu();
        if (mIgTrip == null)
            return;
        if (mIgTrip.getRole() == RoleType.GUEST) {
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
            return;
        }
        if (mIgTrip.getStatus() == IgTripStatus.ONGOING)
            TripActivityPermissionsDispatcher.loadMapsWithCheck(this);
        else
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public void loadMaps() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TripActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick(R.id.layout_comment)
    public void loadComment() {
        closeMenu();

        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
        dialogComment.setTripName(mIgTrip.getName());
    }

    @OnClick(R.id.layout_history)
    public void showMemories() {
        closeMenu();
        if (mIgTrip.getStatus() == IgTripStatus.FINISHED) {

        } else {
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
        }
    }

    @OnClick(R.id.layout_status)
    public void status() {
        closeMenu();
        if (mIgTrip.getRole() == RoleType.ADMIN) {
            DialogTripStatus dialogTripStatus = new DialogTripStatus(this, new DialogTripStatus.OnSelectListener() {
                @Override
                public void select(int type) {
                    IzigoSdk.TripExecutor.updateTripStatus(AppConfig.getInstance().getCurrentTripId(), String.valueOf(type),
                            new IgCallback<Void, String>() {
                                @Override
                                public void onSuccessful(Void aVoid) {
                                    ToastUtils.showToast(TripActivity.this, getString(R.string.str_success));
                                    updateTripDetail();
                                }

                                @Override
                                public void onFail(String error) {
                                    ToastUtils.showToast(TripActivity.this, error);
                                }

                                @Override
                                public void onExpired() {
                                    expired();
                                }
                            });

                }
            });
            dialogTripStatus.show();
            dialogTripStatus.setCurrentStatus(mIgTrip.getStatus());
        } else
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
    }

    @OnClick(R.id.layout_edit)
    public void edit() {
        closeMenu();
        if (mIgTrip.getRole() == RoleType.ADMIN) {
            Intent intent = new Intent(this, AddTripActivity.class);
            intent.putExtra(IgTrip.TRIP_ID, mIgTrip);
            startActivityForResult(intent, MessageType.EDIT_TRIP);
        } else
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
    }

    @OnClick(R.id.layout_members)
    public void members() {
        closeMenu();
        if (mIgTrip.getRole() == RoleType.GUEST) {
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
            return;
        }
        startActivity(new Intent(this, MembersActivity.class));
    }

    @OnClick(R.id.layout_save)
    public void save() {
        closeMenu();
    }

    @OnClick(R.id.layout_share)
    public void share() {
        closeMenu();

        String body = "Tham gia " + mIgTrip.getName() + " cùng tôi nha!";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.str_send_invite)));
    }

    @OnClick(R.id.button)
    public void extraFunction() {
        if (mIgTrip.getRole() == RoleType.GUEST) {
            IzigoSdk.TripExecutor.join(AppConfig.getInstance().getCurrentTripId(), new IgCallback<Void, String>() {
                @Override
                public void onSuccessful(Void aVoid) {
                    ToastUtils.showToast(TripActivity.this, "Đã gửi yêu cầu. Vui lòng chờ admin xác nhận.");
                }

                @Override
                public void onFail(String error) {
                    ToastUtils.showToast(TripActivity.this, error);
                }

                @Override
                public void onExpired() {
                    expired();
                }
            });
        }
    }

    @OnClick(R.id.button_publish)
    public void togglePublish() {
        IzigoSdk.TripExecutor.updateTripPublishStatus(AppConfig.getInstance().getCurrentTripId(),
                buttonPublish.isChecked(),
                new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {
                        ToastUtils.showToast(TripActivity.this, getString(R.string.str_success));
                    }

                    @Override
                    public void onFail(String error) {
                        ToastUtils.showToast(TripActivity.this, error);
                    }

                    @Override
                    public void onExpired() {
                        expired();
                    }
                });
    }
}
