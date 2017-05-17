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
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.izigo.dialogs.DialogComment;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.usecase.map.MapsActivity;
import io.yostajsc.izigo.usecase.trip.adapter.TimelineAdapter;
import io.yostajsc.izigo.utils.UiUtils;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.model.IgTimeline;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.sdk.model.trip.IgTripStatus;
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

    @BindView(R.id.text_vehicle)
    TextView textVehicle;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    @BindView(R.id.recycler_view_time_line)
    RecyclerView rVTimeLine;

    private ImageryAdapter albumAdapter = null;
    private TimelineAdapter timelineAdapter = null;

    private IgTrip mIgTrip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);
        TripActivityView.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    protected void onDestroy() {
        TripActivityView.unbind();
        super.onDestroy();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
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

            }
        }));

        // IgTimeline
        this.timelineAdapter = new TimelineAdapter(this);
        UiUtils.onApplyRecyclerView(this.rVTimeLine, this.timelineAdapter, new SlideInUpAnimator(), null);

    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        IzigoSdk.TripExecutor.getTripDetail(AppConfig.getInstance().getCurrentTripId(), new IgCallback<IgTrip, String>() {
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
        AppConfig.igImages = igTrip.getAlbum();
        this.albumAdapter.replaceAll(igTrip.getAlbum());

        TripActivityView.setTripName(igTrip.getName());
        TripActivityView.setVehicle(igTrip.getTransfer());                        // Transfer
        TripActivityView.setTripStatus(igTrip.getStatus());                       // Status
        TripActivityView.setTripCover(igTrip.getCoverUrl());
        TripActivityView.setOwnerAvatar(igTrip.getCreatorAvatar());
        TripActivityView.setFromTo(igTrip.getFrom(), igTrip.getTo());             // From, To
        TripActivityView.showTripDescription(igTrip.getDescription());            // Description
        TripActivityView.setTime(
                igTrip.getDepartTime(),
                igTrip.getArriveTime()); // Time
        TripActivityView.isPublish(igTrip.isPublished());                         // is publish
        TripActivityView.switchMode(igTrip.getRole());                            // Mode, is publish
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
        /*Intent intent = new Intent(this, TripAlbumActivity.class);
        intent.putExtra(AppConfig.KEY_USER_ROLE, mIgTrip.getRole());
        startActivityForResult(intent, MessageType.PICK_IMAGE);*/
    }

    @OnClick(R.id.layout_maps)
    public void loadMapViews() {
        closeMenu();
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

        } else
            ToastUtils.showToast(this, "Yêu cầu không được chấp nhận, vui lòng liên hệ admin!");
    }

    @OnClick(R.id.layout_delete)
    public void delete() {
        closeMenu();
        if (mIgTrip.getRole() == RoleType.ADMIN) {

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

    @OnClick(R.id.layout_save)
    public void save() {
        closeMenu();
    }

    @OnClick(R.id.layout_share)
    public void share() {
        closeMenu();
    }
}
