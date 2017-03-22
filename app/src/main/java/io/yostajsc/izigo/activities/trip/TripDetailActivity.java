package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
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
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.constants.RoleType;
import io.yostajsc.constants.TransferType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.interfaces.OnConnectionTimeoutListener;
import io.yostajsc.core.utils.AppUtils;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.ActivityManagerActivity;
import io.yostajsc.izigo.activities.dialogs.DialogComment;
import io.yostajsc.izigo.activities.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.adapters.ImageryAdapter;
import io.yostajsc.izigo.activities.ActivityCoreBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.view.CropCircleTransformation;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripDetailActivity extends ActivityCoreBehavior {

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

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.text_number_of_photo)
    TextView textNumberOfPhoto;

    @BindView(R.id.text_activities)
    TextView textNumberOfActivities;

    @BindView(R.id.text_members)
    TextView textNumberOfMembers;

    @BindView(R.id.button_more)
    AppCompatImageView buttonMore;

    @BindView(R.id.button)
    FloatingActionButton button;

    private String tripId;
    private int roleType = RoleType.GUEST;
    private ImageryAdapter albumAdapter = null;
    private final String TAG = TripDetailActivity.class.getSimpleName();

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
        this.albumAdapter = new ImageryAdapter(this);
        UiUtils.onApplyWebViewSetting(webView);
        UiUtils.onApplyAlbumRecyclerView(this.rvAlbum, albumAdapter, new SlideInUpAnimator(), new CallBackWith<Integer>() {
            @Override
            public void run(Integer integer) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.str_option));
        menu.add(0, v.getId(), 0, "Công khai");
        menu.add(1, v.getId(), 1, "Đổi ảnh đại diện");
        menu.add(1, v.getId(), 2, "Chỉnh sửa mô tả");
        menu.add(1, v.getId(), 3, "Đổi tên hành trình");
        menu.add(2, v.getId(), 4, "Chia sẻ");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int order = item.getOrder();
        switch (order) {
            case 0:

                break;
            case 1:
                TripDetailActivityPermissionsDispatcher.getImageFromGalleryWithCheck(this);
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return super.onContextItemSelected(item);
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

    private void updateUI(final Trip trip) {

        if (trip == null) return;
        roleType = trip.getRole();

        switch (roleType) {
            case RoleType.GUEST:
                button.setImageResource(R.drawable.ic_add_user);
                break;
            case RoleType.MEMBER:
            case RoleType.ADMIN:
                button.setImageResource(R.drawable.ic_marker);
                break;

        }

        // Avatar
        Glide.with(TripDetailActivity.this)
                .load(trip.getCreatorAvatar())
                .error(R.drawable.ic_vector_avatar)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(TripDetailActivity.this))
                .into(imageCreatorAvatar);

        // Cover
        Glide.with(TripDetailActivity.this)
                .load(trip.getCover())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageCover);

        // Update transfer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UiUtils.showTransfer(trip.getTransfer(), imageTransfer);
            }
        }, 10);

        new AsyncTask<Trip, Void, Trip>() {

            @Override
            protected Trip doInBackground(Trip... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(Trip trip) {
                super.onPostExecute(trip);

                int nPhotos = trip.getAlbum().size();
                textNumberOfPhoto.setText(getResources().getQuantityString(R.plurals.photos, nPhotos, nPhotos));
                albumAdapter.replaceAll(trip.getAlbum());

                UiUtils.showTextCenterInWebView(webView, trip.getDescription());

                textTripName.setText(trip.getTripName());
                textCreatorName.setText(trip.getCreatorName());

                int nViews = trip.getNumberOfView();
                textViews.setText(getResources().getQuantityString(R.plurals.views, nViews, nViews));

                int nComments = trip.getNumberOfComments();
                textNumberOfComments.setText(getResources().getQuantityString(R.plurals.comments, nComments, nComments));

                int nActivities = trip.getNumberOfActivities();
                textNumberOfActivities.setText(getResources().getQuantityString(R.plurals.activities, nActivities, nActivities));

                int nMembers = trip.getNumberOfMembers();
                textNumberOfMembers.setText(getResources().getQuantityString(R.plurals.members, nMembers, nMembers));

                textTime.setText(String.format("%s - %s",
                        AppUtils.builder().getTime(trip.getDepartTime(), AppUtils.DD_MM_YYYY),
                        AppUtils.builder().getTime(trip.getArriveTime(), AppUtils.DD_MM_YYYY)));
            }
        }.execute(trip);
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
                    updateCover(task.getResult().getDownloadUrl().toString());
                }
            });
        }

    }

    private void updateCover(String url) {

        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);

        APIManager.connect(new OnConnectionTimeoutListener() {
            @Override
            public void onConnectionTimeout() {
                // TODO
            }
        }).updateCover(authorization, tripId, url, new CallBack() {
            @Override
            public void run() {
                onExpired();
            }
        }, new CallBack() {
            @Override
            public void run() {
                Toast.makeText(TripDetailActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                // TODO:
                Log.e(TAG, error);
                Toast.makeText(TripDetailActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.button_more)
    public void more() {
        if (roleType == RoleType.ADMIN) {
            registerForContextMenu(buttonMore);
            buttonMore.performLongClick();
        }
        /*BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());*/
    }

    @OnClick(R.id.text_number_of_comment)
    public void onLoadComment() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
        dialogComment.setTripId(tripId);
    }


    @OnClick(R.id.image_transfer)
    public void onTransfer(View view) {
        if (!view.isClickable())
            return;
        if (roleType == RoleType.ADMIN ||
                roleType == RoleType.MEMBER) {
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

    @OnClick(R.id.button)
    public void actionLink() {
        if (roleType == RoleType.GUEST) {
            String authorization = StorageUtils.inject(this)
                    .getString(AppDefine.AUTHORIZATION);
            APIManager.connect(new OnConnectionTimeoutListener() {
                @Override
                public void onConnectionTimeout() {
                    // TODO
                }
            }).join(authorization, tripId, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            }, new CallBack() {
                @Override
                public void run() {
                    Toast.makeText(TripDetailActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(TripDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            startActivity(new Intent(this, ActivityManagerActivity.class));
            finish();
        }
    }

    @OnClick(R.id.text_activities)
    public void onLoadActivity() {
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
        super.onInternetConnected();
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
        if (ValidateUtils.canUse(authorization)) {
            APIManager.connect(new OnConnectionTimeoutListener() {
                @Override
                public void onConnectionTimeout() {
                    // TODO
                }
            }).getTripDetail(authorization, tripId, new CallBackWith<Trip>() {
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
    public void onInternetDisConnected() {

    }
}
