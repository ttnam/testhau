package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.consts.CallBackWith;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = GalleryActivity.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.button_right)
    AppCompatImageView buttonRight;

    private int total = 0;
    private GalleryAdapter galleryAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @OnClick(R.id.button_left)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_right)
    public void done() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("MULTI_IMAGE", galleryAdapter.getSelectedItems());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void onApplyData() {
        GalleryActivityPermissionsDispatcher.loadImageFromGalleryWithCheck(this);
    }

    public void onApplyViews() {
        this.galleryAdapter = new GalleryAdapter(this, new CallBackWith<Integer>() {
            @Override
            public void run(Integer integer) {
                updateUI(integer);
            }
        });
        this.recyclerView.setAdapter(this.galleryAdapter);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        this.recyclerView.setItemAnimator(new FadeInUpAnimator());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GalleryActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void updateUI(int size) {
        buttonRight.setVisibility((size > 0) ? View.VISIBLE : View.INVISIBLE);
        textView.setText(String.format("%d/%d", size, total));
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void loadImageFromGallery() {
        //get all columns of type images
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        //order data by date
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        //get all data in Cursor by sorting in DESC order
        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        total = cursor.getCount();
        updateUI(0);
        // Loop to cursor count
        for (int i = 0; i < total; i++) {
            cursor.moveToPosition(i);
            //get column index
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            // get Imagery from column index
            galleryAdapter.add(cursor.getString(dataColumnIndex));
        }
    }
}
