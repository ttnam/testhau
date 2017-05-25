
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Calendar;

import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.utils.FileUtils;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.constants.TransferType;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.usecase.trip.dialog.DialogPickTransfer;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.usecase.map.model.Info;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.trip.IgPlace;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.izigo.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddTripActivity extends OwnCoreActivity {

    private static String TAG = AddTripActivity.class.getSimpleName();

    @BindView(R.id.text_view_trip_name)
    TextInputEditText textGroupName;

    @BindView(R.id.edit_description)
    TextInputEditText editDescription;

    @BindView(R.id.text_arrive)
    TextView textArrive;

    @BindView(R.id.text_depart)
    TextView textDepart;

    @BindView(R.id.text_view)
    TextView textViewDes;

    @BindView(R.id.text_arrive_time)
    TextView textArriveTime;

    @BindView(R.id.text_arrive_date)
    TextView textArriveDate;

    @BindView(R.id.text_depart_time)
    TextView textDepartTime;

    @BindView(R.id.text_depart_date)
    TextView textDepartDate;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.image_trip_cover)
    AppCompatImageView imageTripCover;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private int mTransferType = 0;
    private IgPlace from = new IgPlace(), to = new IgPlace();
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        AddTripActivityHelper.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerForContextMenu(imageTripCover);
        IgTrip igTrip = (IgTrip) getIntent().getSerializableExtra(IgTrip.TRIP_ID);
        if (igTrip == null) {
            AddTripActivityHelper.tickTimeUpdate(new AddTripActivityHelper.OnReceiveDate() {
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
            isEdit = false;
        } else {
            isEdit = true;
            textGroupName.setText(igTrip.getName());                // Name
            editDescription.setText(igTrip.getDescription());       // Description
            UiUtils.showTransfer(igTrip.getTransfer(), imageView);  // Transfer

            Glide.with(this).load(igTrip.getCoverUrl())
                    .priority(Priority.IMMEDIATE)
                    .animate(R.anim.anim_fade_in)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageTripCover);
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
        DialogPickTransfer dialogPickTransfer = new DialogPickTransfer(this);
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

        // Group name
        String groupName = textGroupName.getText().toString();
        String arriveName = textArrive.getText().toString();
        String departName = textDepart.getText().toString();
        String description = editDescription.getText().toString();

        if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(arriveName) ||
                TextUtils.isEmpty(departName) || TextUtils.isEmpty(description)) {
            ToastUtils.showToast(this, getString(R.string.error_message_empty));
            return;
        }

        if (from.getTime() < System.currentTimeMillis() || to.getTime() <= from.getTime() || to.getTime() < System.currentTimeMillis()) {
            Toast.makeText(this, getString(R.string.str_invalid_time), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress();
        if (isEdit) {
            finish();
        } else {
            IzigoSdk.TripExecutor.addTrip(groupName, to.toString(), from.toString(), description, mTransferType, new IgCallback<String, String>() {
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
            menu.add(0, v.getId(), 0, "Chọn từ thư viện");
            menu.add(0, v.getId(), 1, "Chụp từ thiết bị");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int order = item.getOrder();
        switch (order) {
            case 0:
                AddTripActivityPermissionsDispatcher.getImageFromGalleryWithCheck(this);
            case 1:
                AddTripActivityPermissionsDispatcher.onTakePhotoWithCheck(this);
                break;
        }
        return super.onContextItemSelected(item);
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
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onTakePhoto() {
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
                        onUriReceiverListener(fileUri, false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.TAKE_PHOTO:
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri tempUri = FileUtils.getImageUri(AddTripActivity.this, photo);
                        onUriReceiverListener(tempUri, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.PICK_LOCATION_FROM:
                    from = (IgPlace) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
                    textDepart.setText(from.getName());

                    MapUtils.Map.direction(null,
                            new LatLng(from.getLat(), from.getLng()),
                            new LatLng(to.getLat(), to.getLng()), false, new CallBackWith<Info>() {
                                @Override
                                public void run(Info info) {
                                    textViewDes.setText(String.format("Dự tính: %s - %s",
                                            info.strDistance,
                                            info.strDuration));
                                }
                            }, null);
                    break;
                case MessageType.PICK_LOCATION_TO:
                    to = (IgPlace) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
                    textArrive.setText(to.getName());

                    MapUtils.Map.direction(null,
                            new LatLng(from.getLat(), from.getLng()),
                            new LatLng(to.getLat(), to.getLng()), false, new CallBackWith<Info>() {
                                @Override
                                public void run(Info info) {
                                    textViewDes.setText(String.format("Dự tính: %s - %s",
                                            info.strDistance,
                                            info.strDuration));
                                }
                            }, null);
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

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInternetDisConnected() {
        hideProgress();
    }

    @Override
    public void onInternetConnected() {

    }

    private void onUriReceiverListener(Uri fileUri, boolean isTakePhoto) {

        File file = FileUtils.getFile(this, fileUri);
        File mSelectedFile = new File(getExternalFilesDir(null), String.format("%s.jpg",
                Calendar.getInstance().getTimeInMillis()));
        try {

            FileUtils.copyFile(file, mSelectedFile);

            if (isTakePhoto) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(this).load(mSelectedFile)
                .priority(Priority.IMMEDIATE)
                .animate(R.anim.anim_fade_in)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageTripCover);
    }

}