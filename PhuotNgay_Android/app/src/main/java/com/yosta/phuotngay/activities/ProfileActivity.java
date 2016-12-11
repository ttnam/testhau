package com.yosta.phuotngay.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yosta.materialspinner.MaterialSpinner;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogChooseImage;
import com.yosta.phuotngay.firebase.FirebaseUtils;
import com.yosta.phuotngay.firebase.model.FirebaseUser;
import com.yosta.phuotngay.helpers.app.DatetimeUtils;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;
import com.yosta.phuotngay.ui.customview.OwnToolBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ProfileActivity extends ActivityBehavior {

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tV_member_ship)
    TextView txtMembership;

    @BindView(R.id.tV_email)
    TextView tVEmail;

    @BindView(R.id.spinner_gender)
    MaterialSpinner spinnerGender;

    @BindView(R.id.image_avatar)
    CircleImageView imageAvatar;

    @BindView(R.id.layout)
    OwnToolBar ownToolBar;

    private FirebaseUtils mFirebaseUtils = null;
    private List<String> mGender = null;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        this.mFirebaseUtils = FirebaseUtils.initializeWith(this);

        this.ownToolBar.setBinding(
                "Nguyễn Phúc Hậu",
                Integer.MIN_VALUE,
                R.drawable.ic_vector_menu,
                null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();

    }

    @Override
    public void onApplyFont() {
        super.onApplyFont();
        /*UIUtils.setFont(this, "fonts/Lato-Regular.ttf", txtMembership, txtGender, txtPhotoNumber, txtFriendsNumber);
        UIUtils.setFont(this, UIUtils.FONT_LATO_BLACK, txtFollow, txtExperience, txtOverview, txtPhotos, textAccountName);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        this.mGender = Arrays.asList(getResources().getStringArray(R.array.arr_gender));
        this.spinnerGender.setItems(this.mGender);

        String uid = "KGSdIvQ1ESWOJfHPJYqkCeX1juf2";
        this.mFirebaseUtils.USER().child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FirebaseUser user = dataSnapshot.getValue(FirebaseUser.class);
                    if (user != null) {
                        onUpdateUserInfo(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.image_avatar)
    public void onChangeAvatar() {
        DialogChooseImage dialogChooseImage = new DialogChooseImage(this);
        dialogChooseImage.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageInfo messageInfo) {
        if (messageInfo.getMessage() == MessageType.TAKE_PHOTO) {
            ProfileActivityPermissionsDispatcher.showCameraWithCheck(this);
        }
        if (messageInfo.getMessage() == MessageType.FROM_GALLERY) {
            ProfileActivityPermissionsDispatcher.showGalleryWithCheck(this);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MessageType.TAKE_PHOTO);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MessageType.FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MessageType.FROM_GALLERY:
                    Uri uri = data.getData();
                    try {
                        Glide.with(ProfileActivity.this).load(uri)
                                .error(R.drawable.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(128, 128).centerCrop()
                                .into(imageAvatar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.TAKE_PHOTO: {
                    try {
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        File destination = new File(Environment.getExternalStorageDirectory(),
                                System.currentTimeMillis() + ".jpg");
                        FileOutputStream fo;
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Glide.with(ProfileActivity.this).load(destination)
                                .error(R.drawable.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(128, 128).centerCrop()
                                .into(imageAvatar);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void onUpdateUserInfo(FirebaseUser user) {

        Glide.with(ProfileActivity.this).load(user.getAvatar())
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(128, 128).centerCrop()
                .into(imageAvatar);

        tvUserName.setText(user.getUsername());
        txtMembership.setText(user.getMembership());
        tVEmail.setText(user.getEmail());
        spinnerGender.setSelectedIndex((int) user.getGender());
    }
    /*

    @OnClick(R.id.layout_logout)
    public void onShowLogout() {
        new StandardDialog(this)
                .setButtonsColor(getResources().getColor(R.color.Red))
                .setCancelable(false)
                .setTopColorRes(android.R.color.white)
                .setTopColor(getResources().getColor(android.R.color.white))
                .setTopColorRes(R.color.BlueTitle)
                .setIcon(R.drawable.ic_vector_logout)
                .setMessage("If you like or even dislike this app, Please rate for us. Thank you!!")
                .setNegativeButton("No, thanks!", null)
                .setPositiveButton("Logout", onLogout)
                .show();
    }

    private View.OnClickListener onLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppConfig appConfig = (AppConfig) getApplication();
            appConfig.userLogout();
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            finish();
        }
    };*/
}
