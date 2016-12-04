package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.yosta.materialspinner.MaterialSpinner;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogChooseImage;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;
import com.yosta.phuotngay.ui.customview.OwnToolBar;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ProfileActivity extends ActivityBehavior {

    /*@BindView(R.id.image)
    AppCompatImageView imageCover;

    @BindView(R.id.txt_overview)
    AppCompatTextView txtOverview;

    @BindView(R.id.txt_experience)
    AppCompatTextView txtExperience;

    @BindView(R.id.txt_followers)
    AppCompatTextView txtFollow;

    @BindView(R.id.txt_membership)
    AppCompatTextView txtMembership;

    @BindView(R.id.txt_photo_number)
    AppCompatTextView txtPhotoNumber;

    @BindView(R.id.txt_friends_number)
    AppCompatTextView txtFriendsNumber;

    @BindView(R.id.textView)
    AppCompatTextView textAccountName;

    @BindView(R.id.txt_gender)
    AppCompatTextView txtGender;

    @BindView(R.id.txt_photos)
    AppCompatTextView txtPhotos;*/

    @BindView(R.id.spinner_gender)
    MaterialSpinner spinnerGender;

    @BindView(R.id.image_avatar)
    CircleImageView imageAvatar;

    @BindView(R.id.layout)
    OwnToolBar ownToolBar;

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

        ownToolBar.setBinding("Nguyễn Phúc Hậu", Integer.MIN_VALUE, R.drawable.ic_vector_menu,
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

        Glide.with(this)
                .load(R.drawable.ic_avatar).diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate().error(R.drawable.ic_vector_profile)
                .into(imageAvatar);
    }

    @OnClick(R.id.image_avatar)
    public void onChangeAvatar() {
        DialogChooseImage dialogChooseImage = new DialogChooseImage(this);
        dialogChooseImage.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageInfo messageInfo) {
        if (messageInfo.getMessage() == MessageType.TAKE_PHOTO) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // fileUri = FileUtlis.getOutputMediaFile(MEDIA_TYPE_IMAGE, IMAGE_DIRECTORY_NAME);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, MessageType.TAKE_PHOTO);
        }
        if (messageInfo.getMessage() == MessageType.FROM_GALLERY) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, MessageType.FROM_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MessageType.FROM_GALLERY:
                    Uri selectedImage = data.getData();
                    String tmp = selectedImage.getPath();
                    String id = tmp.substring((tmp.contains(":")) ?
                            (tmp.lastIndexOf(":") + 1) :
                            (tmp.lastIndexOf("/") + 1));

                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);
                    assert cursor != null;
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    String filename;
                    if (cursor.moveToFirst()) {
                        filename = cursor.getString(columnIndex);
                        Toast.makeText(ProfileActivity.this, filename, Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
                    Glide.with(this)
                            .load(selectedImage).error(R.drawable.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(128, 128).centerCrop()
                            .into(imageAvatar);
                    break;
                case MessageType.TAKE_PHOTO:
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date());
                    String file_path = Environment
                            .getExternalStorageDirectory().getAbsolutePath() +
                            "/PhuotNgay";
                    File dir = new File(file_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    File file = new File(dir, "image_" + timeStamp + ".jpg");
                    try {
                        FileOutputStream fOut = new FileOutputStream(file);

                        fOut.flush();
                        fOut.close();

                        MediaStore.Images.Media.insertImage(getContentResolver(),
                                file.getAbsolutePath(), file.getName(), file.getName());

                        filename = file.toString();

                        Glide.with(this).load(file)
                                .error(R.drawable.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(imageAvatar);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
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
