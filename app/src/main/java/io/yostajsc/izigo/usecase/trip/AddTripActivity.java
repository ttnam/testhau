
package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.yostajsc.izigo.usecase.map.utils.RouteParserTask;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.utils.FileUtils;
import io.yostajsc.sdk.utils.LogUtils;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.usecase.trip.dialog.DialogPickTransfer;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.usecase.map.utils.Info;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.trip.IgPlace;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.izigo.ui.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddTripActivity extends OwnCoreActivity {

    private static String TAG = AddTripActivity.class.getSimpleName();

    @BindView(R.id.text_view_trip_name)
    TextInputEditText textTripName;

    @BindView(R.id.edit_description)
    TextInputEditText editDescription;

    @BindView(R.id.text_view)
    TextView textViewDes;

    @BindView(R.id.text_arrive)
    TextView textArrive;

    @BindView(R.id.text_depart)
    TextView textDepart;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.text_arrive_time)
    TextView textArriveTime;

    @BindView(R.id.text_arrive_date)
    TextView textArriveDate;

    @BindView(R.id.text_depart_time)
    TextView textDepartTime;

    @BindView(R.id.text_depart_date)
    TextView textDepartDate;

    @BindView(R.id.image_trip_cover)
    AppCompatImageView imageTripCover;

    @BindView(R.id.layout_progress)
    FrameLayout layoutProgress;

    @BindView(R.id.layout)
    FrameLayout layoutCover;

    private int mTransferType = 0;
    private boolean isEdit = false;
    private File mSelectedFile = null;
    private DialogPickTransfer dialogPickTransfer = null;
    private IgPlace from = new IgPlace(), to = new IgPlace();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        AddTripActivityHelper.bind(this);
        dialogPickTransfer = new DialogPickTransfer(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerForContextMenu(imageTripCover);
        IgTrip igTrip = (IgTrip) getIntent().getSerializableExtra(IgTrip.TRIP_ID);
        if (igTrip == null) {
            AddTripActivityHelper.tickTimeUpdate(
                    new AddTripActivityHelper.OnReceiveDate() {
                        @Override
                        public void date(Integer day, Integer month, Integer year, String result) {
                            textArriveDate.setText(result);
                            textDepartDate.setText(result);
                            from.setDate(year, month, day);
                            to.setDate(year, month, day);
                        }
                    }, new AddTripActivityHelper.OnReceiveTime() {
                        @Override
                        public void time(Integer hour, Integer minute, String result) {
                            textArriveTime.setText(result);
                            textDepartTime.setText(result);
                            from.setTime(hour, minute);
                            to.setTime(hour, minute);
                        }
                    });
            enableEditMode(false);
        } else {
            enableEditMode(true);
            textTripName.setText(igTrip.getName());                // Name
            editDescription.setText(igTrip.getDescription());       // Description
            UiUtils.showTransfer(igTrip.getTransfer(), imageView);  // Transfer

            Glide.with(this).clear(imageTripCover);
            Glide.with(this)
                    .load(igTrip.getCoverUrl())
                    .into(imageTripCover);
        }
    }

    void enableEditMode(boolean isEnable) {
        if (isEnable) {
            isEdit = true;
            layoutCover.setVisibility(View.VISIBLE);
        } else {
            isEdit = false;
            layoutCover.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterForContextMenu(imageTripCover);
        try {
            FileUtils.delete(getExternalFilesDir(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.text_arrive, R.id.text_depart})
    public void pickArriveLocation(View view) {
        AddTripActivityPermissionsDispatcher.pickLocationWithCheck(this,
                (view.getId() == R.id.text_arrive) ? MessageType.PICK_LOCATION_TO : MessageType.PICK_LOCATION_FROM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddTripActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void pickLocation(int type) {
        startActivityForResult(new Intent(AddTripActivity.this, PickLocationActivity.class), type);
    }

    @OnClick({R.id.text_depart_time, R.id.text_arrive_time})
    public void pickDepartTime(final View view) {
        AddTripActivityHelper.getInstance().selectTime(new AddTripActivityHelper.OnReceiveTime() {
            @Override
            public void time(Integer hour, Integer minute, String result) {
                if (view.getId() == R.id.text_depart_time) {
                    from.setHour(hour);
                    from.setMinute(minute);
                    textDepartTime.setText(result);
                } else if (view.getId() == R.id.text_arrive_time) {
                    to.setHour(hour);
                    to.setMinute(minute);
                    textArriveTime.setText(result);
                }
            }
        });
    }

    @OnClick({R.id.text_depart_date, R.id.text_arrive_date})
    public void pickDepartDate(final View view) {
        AddTripActivityHelper.getInstance().selectDate(new AddTripActivityHelper.OnReceiveDate() {
            @Override
            public void date(Integer day, Integer month, Integer year, String result) {
                if (view.getId() == R.id.text_depart_date) {
                    from.setDay(day);
                    from.setMonth(month);
                    from.setYear(year);
                    textDepartDate.setText(result);
                } else if (view.getId() == R.id.text_arrive_date) {
                    to.setDay(day);
                    to.setMonth(month);
                    to.setYear(year);
                    textArriveDate.setText(result);
                }
            }
        });
    }

    @OnClick(R.id.image_view)
    public void pickTransfer() {
        dialogPickTransfer.setDialogResult(new CallBackWith<Integer>() {
            @Override
            public void run(@TransferType Integer type) {
                mTransferType = type;
                UiUtils.showTransfer(type, imageView);
            }
        });
        dialogPickTransfer.show();
    }

    @OnClick(R.id.image_trip_cover)
    public void pickImage() {
        imageTripCover.performLongClick();
    }

    @OnClick(R.id.button)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_confirm)
    public void onConfirm() {

        // Trip name
        final String tripName = textTripName.getText().toString();
        if (TextUtils.isEmpty(tripName)) {
            ToastUtils.showToast(this, getString(R.string.str_miss_trip_name));
            return;
        }

        // Destination
        String arriveName = textArrive.getText().toString();
        if (TextUtils.isEmpty(arriveName)) {
            ToastUtils.showToast(this, getString(R.string.str_miss_arrive));
            return;
        }

        // Departure
        String departName = textDepart.getText().toString();
        if (TextUtils.isEmpty(departName)) {
            ToastUtils.showToast(this, getString(R.string.str_miss_depart));
            return;
        }

        // Description
        final String description = editDescription.getText().toString();

        if (from.getTime() < System.currentTimeMillis() ||
                to.getTime() <= from.getTime() ||
                to.getTime() < System.currentTimeMillis()) {
            Toast.makeText(this, getString(R.string.str_invalid_time), Toast.LENGTH_SHORT).show();
            return;
        }

        final Map<String, String> fields = new HashMap<>();
        fields.put("name", tripName);
        fields.put("arrive", to.toString());
        fields.put("depart", from.toString());
        fields.put("description", description);
        fields.put("transfer", String.valueOf(mTransferType));

        showProgress();
        if (isEdit) {
            String tripId = AppConfig.getInstance().getCurrentTripId();

            if (mSelectedFile != null) {
                IzigoSdk.TripExecutor.uploadCover(mSelectedFile, FileUtils.getMimeType(mSelectedFile), tripId, new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {
                        LogUtils.log("onSuccessful cover");
                    }

                    @Override
                    public void onFail(String error) {
                        hideProgress();
                        LogUtils.log(error);
                    }

                    @Override
                    public void onExpired() {
                        expired();
                    }
                });
            }

            IzigoSdk.TripExecutor.updateTrip(tripId, fields, new IgCallback<Void, String>() {
                @Override
                public void onSuccessful(Void aVoid) {
                    hideProgress();
                    ToastUtils.showToast(AddTripActivity.this, R.string.str_success);
                    finish();
                }

                @Override
                public void onFail(String error) {
                    hideProgress();
                    ToastUtils.showToast(AddTripActivity.this, error);
                    LogUtils.log(error);
                }

                @Override
                public void onExpired() {
                    expired();
                }
            });
        } else {

            IzigoSdk.TripExecutor.addTrip(fields, new IgCallback<String, String>() {
                @Override
                public void onSuccessful(String groupId) {
                    hideProgress();
                    onSuccess(groupId);
                }

                @Override
                public void onFail(String error) {
                    hideProgress();
                    ToastUtils.showToast(AddTripActivity.this, error);
                }

                @Override
                public void onExpired() {
                    hideProgress();
                    expired();
                }
            });

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.image_trip_cover) {
            menu.add(0, v.getId(), 0, getString(R.string.str_from_gallery));
            menu.add(0, v.getId(), 1, getString(R.string.str_from_camera));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int order = item.getOrder();
        switch (order) {
            case 0:
                AddTripActivityPermissionsDispatcher.showGalleryWithCheck(this);
                break;
            case 1:
                AddTripActivityPermissionsDispatcher.showCameraWithCheck(this);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.str_select_photo)), MessageType.FROM_GALLERY);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, MessageType.TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MessageType.FROM_GALLERY:
                    try {
                        Uri fileUri = data.getData();
                        if (fileUri == null)
                            return;
                        processFile(fileUri, false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.TAKE_PHOTO:
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri fileUri = FileUtils.getImageUri(AddTripActivity.this, photo);
                        if (fileUri == null)
                            return;
                        processFile(fileUri, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.PICK_LOCATION_FROM:
                    from = (IgPlace) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
                    textDepart.setText(from.getName());

                    MapUtils.Map.direction(
                            new LatLng(from.getLat(), from.getLng()),
                            new LatLng(to.getLat(), to.getLng()),
                            new RouteParserTask.OnDirectionCallBack() {
                                @Override
                                public void onSuccess(Info info, Polyline polyline) {
                                    textViewDes.setText(String.format("Dự tính: %s - %s",
                                            info.strDistance,
                                            info.strDuration));
                                }
                            });
                    break;
                case MessageType.PICK_LOCATION_TO:
                    to = (IgPlace) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
                    textArrive.setText(to.getName());

                    MapUtils.Map.direction(
                            new LatLng(from.getLat(), from.getLng()),
                            new LatLng(to.getLat(), to.getLng()),
                            new RouteParserTask.OnDirectionCallBack() {
                                @Override
                                public void onSuccess(Info info, Polyline polyline) {
                                    textViewDes.setText(String.format("Dự tính: %s - %s",
                                            info.strDistance,
                                            info.strDuration));
                                }
                            });
                    break;
            }
        }
    }

    private void onSuccess(String tripId) {
        Intent intent = new Intent(AddTripActivity.this, TripActivity.class);
        intent.putExtra(IgTrip.TRIP_ID, tripId);
        startActivity(intent);
        finish();
    }

    void hideProgress() {
        layoutProgress.setVisibility(View.GONE);
    }

    void showProgress() {
        layoutProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInternetDisConnected() {
        hideProgress();
    }

    @Override
    public void onInternetConnected() {

    }

    void processFile(Uri fileUri, boolean isTakePhoto) {

        File file = FileUtils.getFile(this, fileUri);
        mSelectedFile = new File(getExternalFilesDir(null), String.format("%s.jpg",
                Calendar.getInstance().getTimeInMillis()));
        try {

            FileUtils.copyFile(file, mSelectedFile);

            if (isTakePhoto) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(this).clear(imageTripCover);
        Glide.with(this)
                .load(mSelectedFile)
                .apply(new RequestOptions()
                        .dontAnimate()
                        .dontTransform()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.ic_style_rect_round_corners_gray_none))
                .into(imageTripCover);
    }
}