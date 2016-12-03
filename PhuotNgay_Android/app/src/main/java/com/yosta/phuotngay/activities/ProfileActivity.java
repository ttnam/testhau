package com.yosta.phuotngay.activities;

import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosta.materialspinner.MaterialSpinner;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogChooseImage;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.customview.OwnToolBar;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        this.mGender = Arrays.asList(getResources().getStringArray(R.array.arr_gender));
        this.spinnerGender.setItems(this.mGender);

        Glide.with(this)
                .load(R.drawable.ic_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .error(R.drawable.ic_vector_profile)
                .into(imageAvatar);

/*
        txtGender.setText(this.appConfig.getCurrentUser().getGender());
        textAccountName.setText(this.appConfig.getCurrentUser().getName());

        txtPhotoNumber.setText("1.5K photos");
        txtFriendsNumber.setText("2K friends");*/
    }

    @OnClick(R.id.image_avatar)
    public void onChangeAvatar() {
        DialogChooseImage dialogChooseImage = new DialogChooseImage(this);
        dialogChooseImage.show();
    }
/*
    @OnClick(R.id.image)
    public void onChangeCover() {
        ContextMenuDialog dialog = new ContextMenuDialog(this);
        //dialog.setTitle("Change Cover");
        dialog.show();
    }
/*
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
    };*/
}
