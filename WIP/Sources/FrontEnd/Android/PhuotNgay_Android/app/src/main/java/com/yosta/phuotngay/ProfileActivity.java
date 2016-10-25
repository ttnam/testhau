package com.yosta.phuotngay;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.yosta.circleimageview.CircleImageView;
import com.yosta.materialdialog.StandardDialog;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.config.AppConfig;
import com.yosta.phuotngay.globalapp.UIUtils;

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
    AppCompatTextView txtPhotos;

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
        UIUtils.setFont(this, "fonts/Lato-Regular.ttf", txtMembership, txtGender, txtPhotoNumber, txtFriendsNumber);
        UIUtils.setFont(this, UIUtils.FONT_LATO_BLACK, txtFollow, txtExperience, txtOverview, txtPhotos, textAccountName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        Picasso.with(this).load(this.appConfig.getCurrentUser().getCoverUrl()).into(imageCover);
        Picasso.with(this).load(this.appConfig.getCurrentUser().getAvatarUrl()).into(imageAvatar);

        txtGender.setText(this.appConfig.getCurrentUser().getGender());
        textAccountName.setText(this.appConfig.getCurrentUser().getName());

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

    @OnClick(R.id.txt_followers)
    public void onShowFollowers() {
    }

    @OnClick(R.id.txt_photos)
    public void onShowPhotos() {
        startActivity(new Intent(this, ImageryActivity.class));
        onBackPressed();
    }

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
    };
}
