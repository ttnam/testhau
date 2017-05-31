package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.dialog.DialogProgress;
import io.yostajsc.sdk.gallery.ImageNormalViewHolder;
import io.yostajsc.sdk.utils.LogUtils;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.gallery.SelectorImageryAdapter;
import io.yostajsc.sdk.gallery.LayoutViewType;
import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.sdk.utils.FileUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.cache.IgCache;
import io.yostajsc.sdk.api.model.trip.IgImage;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripAlbumActivity extends OwnCoreActivity {

    @BindView(R.id.button_right)
    AppCompatImageView btnRight;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    private int role;
    private DialogProgress dialogProgress = null;
    private SelectorImageryAdapter albumAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        this.dialogProgress = new DialogProgress(this);
        this.albumAdapter = new SelectorImageryAdapter(this, new ImageNormalViewHolder.OnClick() {
            @Override
            public void onClick(int position) {
                deleteImage(position);
            }
        });
        this.rvAlbum.setAdapter(this.albumAdapter);
        this.rvAlbum.setHasFixedSize(true);
        this.rvAlbum.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvAlbum.setNestedScrollingEnabled(false);
        this.rvAlbum.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        this.rvAlbum.setItemAnimator(new FadeInUpAnimator());
        registerForContextMenu(btnRight);
    }

    @OnClick(R.id.button_left)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_right)
    public void addImage() {
        btnRight.performLongClick();
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(btnRight);
        super.onDestroy();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        role = getIntent().getIntExtra(AppConfig.KEY_USER_ROLE, -1);

        btnRight.setVisibility(role == RoleType.GUEST ? View.INVISIBLE : View.VISIBLE);
        List<IgImage> imageList = IgCache.TripCache.getAlbum();
        if (imageList != null && imageList.size() > 0) {
            for (IgImage igImage : imageList) {
                this.albumAdapter.add(
                        new SelectorImageryAdapter.Imagery(
                                igImage.getId(), igImage.getUrl(),
                                role == RoleType.ADMIN ?
                                        LayoutViewType.DELETE :
                                        LayoutViewType.NORMAL));
            }
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TripAlbumActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MessageType.FROM_MULTI_GALLERY:
                    ArrayList<String> urls = data.getStringArrayListExtra("MULTI_IMAGE");
                    if (urls != null && urls.size() > 0) {
                        confirmUpload(urls);
                    }
                    break;
                case MessageType.TAKE_PHOTO: {
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri tempUri = FileUtils.getImageUri(TripAlbumActivity.this, photo);
                        //albumAdapter.add(new IgImage(tempUri.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int order = item.getOrder();
        switch (order) {
            case 0:
                startActivityForResult(new Intent(TripAlbumActivity.this, GalleryActivity.class),
                        MessageType.FROM_MULTI_GALLERY);
                break;
            case 1:
                TripAlbumActivityPermissionsDispatcher.onTakePhotoWithCheck(this);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getString(R.string.str_from_gallery));
        menu.add(0, v.getId(), 1, getString(R.string.str_from_camera));
    }

    /**
     * Delete image from album with imageId
     */
    void deleteImage(final int position) {
        if (role != RoleType.ADMIN)
            return;

        String imageId = albumAdapter.getId(position);
        if (TextUtils.isEmpty(AppConfig.getInstance().getCurrentTripId()))
            return;

        IzigoSdk.TripExecutor.deleteImage(AppConfig.getInstance().getCurrentTripId(),
                imageId, new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {
                        albumAdapter.remove(position);
                        ToastUtils.showToast(TripAlbumActivity.this, "Bạn đã xoá ảnh thành công.");
                    }

                    @Override
                    public void onFail(String error) {
                        LogUtils.log(error);
                    }

                    @Override
                    public void onExpired() {
                        expired();
                    }
                });
    }

    void confirmUpload(final ArrayList<String> urls) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.str_ask_upload_photo))
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                upload(urls);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public void onInternetDisConnected() {
        super.onInternetDisConnected();
        this.dialogProgress.dismiss();
    }

    void upload(ArrayList<String> urls) {

        if (!dialogProgress.isShowing())
            dialogProgress.show("Uploading ...");

        List<File> files = new ArrayList<>();
        for (String url : urls) {
            files.add(new File(url));
        }

        IzigoSdk.TripExecutor.uploadAlbum(AppConfig.getInstance().getCurrentTripId(),
                files, new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {
                        dialogProgress.dismiss();
                        ToastUtils.showToast(TripAlbumActivity.this, R.string.str_success);
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        ToastUtils.showToast(TripAlbumActivity.this, error);
                        dialogProgress.dismiss();
                    }

                    @Override
                    public void onExpired() {
                        dialogProgress.dismiss();
                        expired();
                    }
                });
    }

}
