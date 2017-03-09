package io.yostajsc.izigo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.backend.config.APIManager;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.models.user.User;
import io.yostajsc.utils.StorageUtils;
import io.yostajsc.utils.validate.ValidateUtils;
import io.yostajsc.view.CircleImageView;
import io.yostajsc.view.OwnToolBar;

import butterknife.BindView;

/*
@RuntimePermissions*/
public class ProfileActivity extends ActivityBehavior {

    /*@BindView(R.id.text_member_ship)
    TextView tvMembership;*/

    @BindView(R.id.edit_email)
    EditText editEmail;

    @BindView(R.id.text_gender)
    TextView textGender;

    @BindView(R.id.edit_dob)
    EditText editDob;

    @BindView(R.id.edit_first_name)
    EditText editFirstName;

    @BindView(R.id.edit_last_name)
    EditText editLastName;

    @BindView(R.id.image_view)
    CircleImageView imageAvatar;

    @BindView(R.id.layout)
    OwnToolBar ownToolBar;

    private User mUser = null;
    private boolean isFirstTime = false;
/*

    private FirebaseManager mFirebaseUtils = null;
    private List<String> mGender = null;
*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        isFirstTime = intent.getBooleanExtra(AppDefine.FIRST_TIME, false);
    }

    @Override
    public void onApplyData() {
        if (isFirstTime) {
            mUser = StorageUtils.inject(ProfileActivity.this).getUser();
            updateValue();
        } else {
            String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
            if (ValidateUtils.canUse(authorization)) {
                APIManager.connect().getUserInfo(authorization, new CallBackWith<User>() {
                    @Override
                    public void run(User user) {
                        mUser = user;
                        updateValue();
                    }
                }, new CallBack() {
                    @Override
                    public void run() {
                        onExpired();
                    }
                }, new CallBackWith<String>() {
                    @Override
                    public void run(String error) {
                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onApplyData();
    }

    private void updateValue() {
        if (mUser == null) {
            return;
        }
        ownToolBar.setTitle(mUser.getFullName());
        Glide.with(ProfileActivity.this)
                .load(mUser.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);
        editEmail.setText(mUser.getEmail());
        textGender.setText(mUser.getGender());
        editDob.setText(mUser.getBirthday());
        editLastName.setText(mUser.getLastName());
        editFirstName.setText(mUser.getFirstName());
    }

    @Override
    public void onBackPressed() {
        if (isFirstTime) {
            Toast.makeText(this, "Vui lòng xác nhận thông tin tài khoản", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.button)
    public void onConfirm() {

        String dob = editDob.getText().toString();
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String email, gender;

        if (mUser == null) {
            email = editEmail.getText().toString();
            gender = textGender.getText().toString();
        } else {
            email = mUser.getEmail();
            gender = mUser.getGender();
        }

        if (ValidateUtils.canUse(dob, firstName, lastName, email, gender)) {

            Map<String, String> map = new HashMap<>();
            map.put("avatar", mUser.getAvatar());
            map.put("email", email);
            map.put("firstName", firstName);
            map.put("lastName", lastName);
            map.put("gender", gender);
            map.put("dateOfBirth", dob);

            String authorization = StorageUtils.inject(ProfileActivity.this).getString(AppDefine.AUTHORIZATION);
            APIManager.connect().updateProfile(authorization, map, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            }, new CallBack() {
                @Override
                public void run() {
                    if (isFirstTime) {
                        StorageUtils.inject(ProfileActivity.this).save(AppDefine.FIRST_TIME, 0);
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    } else {
                        finish();
                    }

                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.error_message_empty), Toast.LENGTH_SHORT).show();
        }
    }
    /*


    @Override
    public void onApplyRecyclerView() {
        super.onApplyRecyclerView();

        // this.mFirebaseUtils = FirebaseManager.inject();

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
    public void onApplyData() {
        super.onApplyData();
        this.mGender = Arrays.asList(getResources().getStringArray(R.array.arr_gender));
        this.spinnerGender.setItems(this.mGender);

        String uid = "KGSdIvQ1ESWOJfHPJYqkCeX1juf2";
        this.mFirebaseUtils.USER().child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
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
        intent.setType("image*//*");
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

    private void onUpdateUserInfo(User user) {

        Glide.with(ProfileActivity.this).load(user.getAvatar())
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(128, 128).centerCrop()
                .into(imageAvatar);

        tvUserName.setText(user.getUsername());
        txtMembership.setText(user.getMembership());
        tVEmail.setText(user.getEmail());
        spinnerGender.setSelectedIndex((int) user.getGender());
    }*/
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
