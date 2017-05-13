package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.CoreActivity;
import io.yostajsc.core.utils.FileUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.ImageryOnlyAdapter;
import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.izigo.usecase.customview.gallery.GalleryActivity;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripAlbumActivity extends CoreActivity {

    @BindView(R.id.button_right)
    AppCompatImageView btnRight;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    private ImageryOnlyAdapter albumAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        this.albumAdapter = new ImageryOnlyAdapter(this);
        this.rvAlbum.setAdapter(this.albumAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.rvAlbum);
        this.rvAlbum.setHasFixedSize(true);
        this.rvAlbum.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvAlbum.setNestedScrollingEnabled(false);
        this.rvAlbum.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        this.rvAlbum.setItemAnimator(new FadeInUpAnimator());
        this.rvAlbum.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


            }
        }));
        registerForContextMenu(btnRight);
    }

    @OnClick({R.id.button_left, R.id.button})
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("PICK_IMAGE", albumAdapter.getAllInListString());
        setResult(RESULT_OK, intent);
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
        if (AppConfig.igImages.size() > 0) {
            this.albumAdapter.replaceAll(AppConfig.igImages);
        }

        int role = getIntent().getIntExtra(AppConfig.KEY_USER_ROLE, -1);
        btnRight.setVisibility(role == RoleType.GUEST ? View.INVISIBLE : View.VISIBLE);
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
        TripAlbumActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MessageType.FROM_MULTI_GALLERY:
                    ArrayList<String> res = data.getStringArrayListExtra("MULTI_IMAGE");
                    if (res.size() > 0) {
                        for (String url : res) {
                            albumAdapter.add(new IgImage(url));
                        }
                    }
                    break;
                case MessageType.TAKE_PHOTO: {
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri tempUri = FileUtils.getImageUri(TripAlbumActivity.this, photo);
                        albumAdapter.add(new IgImage(tempUri.toString()));
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
                startActivityForResult(
                        new Intent(TripAlbumActivity.this, GalleryActivity.class),
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
        menu.add(0, v.getId(), 0, "Chọn từ thư viện");
        menu.add(0, v.getId(), 1, "Chụp từ thiết bị");
    }
}
