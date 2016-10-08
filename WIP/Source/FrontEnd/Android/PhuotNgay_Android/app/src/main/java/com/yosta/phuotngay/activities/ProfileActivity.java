package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yosta.circleimageview.CircleImageView;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.interfaces.ActivityBehavior;
import com.yosta.phuotngay.config.AppConfig;
import com.yosta.phuotngay.helpers.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends ActivityBehavior {

    @BindView(R.id.image)
    AppCompatImageView imageCover;

    @BindView(R.id.image_avatar)
    CircleImageView imageAvatar;

    @BindView(R.id.txt_overview)
    AppCompatTextView txtOverview;

    @BindView(R.id.txt_experience)
    AppCompatTextView txtExperience;

    @BindView(R.id.txt_follow)
    AppCompatTextView txtFollow;

    @BindView(R.id.txt_membership)
    AppCompatTextView txtMembership;

    @BindView(R.id.txt_photo_number)
    AppCompatTextView txtPhotoNumber;

    @BindView(R.id.txt_friends_number)
    AppCompatTextView txtFriendsNumber;

    @BindView(R.id.textView)
    AppCompatTextView textAccountName;

    @BindView(R.id.txt_toolbar_title)
    AppCompatTextView txtToolbarTitle;

    @BindView(R.id.txt_gender)
    AppCompatTextView txtGender;

    @BindView(R.id.txt_photos)
    AppCompatTextView txtPhotos;

    @BindView(R.id.txt_friends)
    AppCompatTextView txtFriends;

    private AppConfig appConfig = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        this.appConfig = (AppConfig) getApplication();
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();
    }

    @Override
    public void onApplyFont() {
        super.onApplyFont();
        AppUtils.setFont(this, "fonts/Lato-Regular.ttf", txtMembership, txtGender, txtPhotoNumber, txtFriendsNumber);
        AppUtils.setFont(this, "fonts/Lato-Black.ttf", txtFollow, txtExperience, txtOverview, txtPhotos,
                txtFriends, textAccountName, txtToolbarTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_logout:
                return true;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        Picasso.with(this).load(this.appConfig.getCurrentUser().getCoverUrl()).into(imageCover);
        Picasso.with(this).load(this.appConfig.getCurrentUser().getAvatarUrl()).into(imageAvatar);

        txtGender.setText(this.appConfig.getCurrentUser().getGender());
        txtToolbarTitle.setText(this.appConfig.getCurrentUser().getUserName());
        textAccountName.setText(this.appConfig.getCurrentUser().getUserName());

        txtPhotoNumber.setText("1.5K photos");
        txtFriendsNumber.setText("2K friends");
    }

/*
    @OnClick(R.id.image)
    public void onChangeCover() {
        ContextMenuDialog dialog = new ContextMenuDialog(this);
        //dialog.setTitle("Change Cover");
        dialog.show();
    }

    @OnClick(R.id.image_avatar)
    public void onChangeAvatar() {
        ContextMenuDialog dialog = new ContextMenuDialog(this);
        dialog.show();
    }*/

    @OnClick(R.id.layout_follow)
    public void onShowFollowers() {
    }

    @OnClick(R.id.txt_photos)
    public void onShowPhotos() {
        startActivity(new Intent(this, FrescoActivity.class));
        onClose();
    }

    @Override
    @OnClick(R.id.imageBack)
    public void onClose() {
        super.onClose();
    }

    @OnClick(R.id.img_edit)
    public void onEdit() {
        Toast.makeText(ProfileActivity.this, "onEdit", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.img_logout)
    public void onLogout() {
        Toast.makeText(ProfileActivity.this, "onLogout", Toast.LENGTH_SHORT).show();
    }
/*
    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                break;
            }
            case REQUEST_SELECT_IMAGE_IN_ALBUM: {

                if (resultCode == RESULT_OK) {

                }
            }
        }
    }*/
}
