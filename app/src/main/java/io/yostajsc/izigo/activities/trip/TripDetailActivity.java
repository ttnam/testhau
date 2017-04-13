package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
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
import io.yostajsc.izigo.activities.MembersActivity;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.usecase.backend.core.APIManager;
import io.yostajsc.constants.RoleType;
import io.yostajsc.constants.TransferType;
import io.yostajsc.constants.TripTypePermission;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.dialogs.DialogComment;
import io.yostajsc.izigo.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.usecase.realm.RealmManager;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.text_views)
    TextView textViews;

    @BindView(R.id.image_creator_avatar)
    AppCompatImageView imageCreatorAvatar;

    @BindView(R.id.image_transfer)
    AppCompatImageView imageTransfer;

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.text_edit)
    TextView textEdit;

    @BindView(R.id.button_more)
    AppCompatImageView buttonMore;

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
        } else if (v.getId() == R.id.button_more) {
            menu.add(1, v.getId(), 2, "Đổi tên hành trình");
            if (mIsPublic) {
                menu.add(1, v.getId(), 3, "Hiển thị chỉ mình tôi");
            } else {
                menu.add(1, v.getId(), 4, "Hiển thị cho mọi người");
            }
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
                changeTripName();
                break;
            case 3:
                makeTripPublic(false);
                break;
            case 4:
                makeTripPublic(true);
                break;
            /*case 4:
                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
                break;*/
        }
        return super.onContextItemSelected(item);
    }

    @OnClick(R.id.layout_member)
    public void showMembers() {
        Intent intent = new Intent(TripDetailActivity.this, MembersActivity.class);
        intent.putExtra(AppConfig.TRIP_ID, tripId);
        startActivity(intent);
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

    private void changeTripName() {

        View promptsView = LayoutInflater.from(this).inflate(R.layout.view_dialog_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.text_view);
        userInput.setText(textTripName.getText());
        // set dialog message
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String res = userInput.getText().toString();
                                if (ValidateUtils.canUse(res)) {
                                    textTripName.setText(res);
                                    updateTrip(res, TripTypePermission.NAME);
                                }
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onApplyData() {
        try {

            Intent intent = this.getIntent();
            tripId = intent.getStringExtra(AppConfig.TRIP_ID);
            if (ValidateUtils.canUse(tripId)) {
                if (NetworkUtils.isNetworkConnected(this)) {
                    APIManager.connect().trackingViews(tripId, new CallBack() {
                        @Override
                        public void run() {
                        }
                    }, new CallBackWith<String>() {
                        @Override
                        public void run(String error) {
                            AppConfig.showToast(TripDetailActivity.this, error);
                        }
                    }, mOnExpiredCallBack);
                    loadTripFromServer();
                } else {
                    loadTripFromRealm();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTripFromServer() {
        APIManager.connect().getTripDetail(tripId, new CallBackWith<Trip>() {
            @Override
            public void run(Trip trip) {
                updateUI(trip);
                // RealmManager.deleteTripById(tripId);
                RealmManager.insertOrUpdate(trip);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(TripDetailActivity.this, error);
                loadTripFromRealm();
            }
        }, mOnExpiredCallBack);
    }

    private void loadTripFromRealm() {
        RealmManager.findTripById(tripId, new CallBackWith<Trip>() {
            @Override
            public void run(Trip trip) {
                updateUI(trip);
            }
        });
    }

    private void updateUI(final Trip trip) {
        try {

            if (trip == null) return;

            albumAdapter.replaceAll(trip.getAlbum());
            mIsPublic = trip.isPublished();
            mCurrentRoleType = trip.getRole();
            TripDetailActivityView.inject(this)
                    .switchMode(mCurrentRoleType)                // Mode, is publish
                    .setTripCover(trip.getCover())                          // Cover
                    .setTripName(trip.getTripName())                        // Trip name
                    .showTransfer(trip.getTransfer())                       // Transfer
                    .setViews(trip.getNumberOfView())                       // Views
                    .setOwnerName(trip.getCreatorName())                    // Own name
                    .setOwnerAvatar(trip.getCreatorAvatar())                // Avatar
                    .showTripDescription(trip.getDescription())             // Description
                    .setTime(trip.getDepartTime(), trip.getArriveTime());   // Time

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onApplyFirebase(Uri uri) {

        if (NetworkUtils.isNetworkConnected(this)) {

            // Firebase
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

    @OnClick({R.id.button_more, R.id.image_view})
    public void showContextMenu(AppCompatImageView button) {
        if (mCurrentRoleType == RoleType.ADMIN) {
            button.performLongClick();
        }
    }

    @OnClick(R.id.image_transfer)
    public void onTransfer(View view) {
        if (!view.isClickable())
            return;
        if (mCurrentRoleType == RoleType.ADMIN) {
            DialogPickTransfer dialogPickTransfer = new DialogPickTransfer(this);
            dialogPickTransfer.setDialogResult(new CallBackWith<Integer>() {
                @Override
                public void run(@TransferType Integer type) {
                    UiUtils.showTransfer(type, imageTransfer);
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
                            onApplyFirebase(fileUri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onInternetConnected() {

    }

    @Override
    public void onInternetDisConnected() {

    }
}
