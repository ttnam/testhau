package io.yostajsc.izigo.activities.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.usecase.backend.core.IzigoApiManager;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.user.User;

import butterknife.BindView;

/*
@RuntimePermissions*/
public class ProfileActivity extends OwnCoreActivity {

    @BindView(R.id.text_email)
    TextView textEmail;

    @BindView(R.id.text_gender)
    TextView textGender;

    @BindView(R.id.text_member_ship)
    TextView textMemberShip;

    @BindView(R.id.edit_name)
    EditText editName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;

    private User mUser = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        onApplyData();
    }

    @Override
    public void onApplyData() {
        if (NetworkUtils.isNetworkConnected(this)) {
            IzigoApiManager.connect().getUserInfo(new CallBackWith<User>() {
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

    private void updateValue() {
        if (mUser == null) {
            return;
        }
        editName.setText(mUser.getFullName());
        editName.setSelection(editName.getText().length());

        Glide.with(ProfileActivity.this)
                .load(mUser.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);
        textEmail.setText(mUser.getEmail());
        textGender.setText(mUser.getGender());
        textMemberShip.setText(mUser.getMemberShip());
    }

    @OnClick(R.id.button)
    public void onConfirm() {

        String name = editName.getText().toString();
        String email, gender;

        if (mUser == null) {
            email = textEmail.getText().toString();
            gender = textGender.getText().toString();
        } else {
            email = mUser.getEmail();
            gender = mUser.getGender();
        }

        if (ValidateUtils.canUse(name, email, gender)) {

            Map<String, String> map = new HashMap<>();
            map.put("avatar", mUser.getAvatar());
            map.put("email", email);
            map.put("name", name);
            map.put("gender", gender);
            IzigoApiManager.connect().updateProfile(map, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            }, new CallBack() {
                @Override
                public void run() {
                    /*if (isFirstTime) {
                        StorageUtils.bind(ProfileActivity.this).save(AppConfig.FIRST_TIME, 0);
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    } else {
                        finish();
                    }*/

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

        // this.mFirebaseUtils = FirebaseManager.bind();

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
}
