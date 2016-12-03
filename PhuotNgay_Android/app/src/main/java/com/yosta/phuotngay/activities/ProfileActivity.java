package com.yosta.phuotngay.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;
import com.yosta.phuotngay.fragments.ProfileTripFragment;
import com.yosta.phuotngay.fragments.ProfileBaseInfoFragment;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.customview.OwnToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends ActivityBehavior {

    /*@BindView(R.id.image)
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
    AppCompatTextView txtPhotos;*/

    @BindView(R.id.layout)
    OwnToolBar ownToolBar;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        onApplyViewPager();
        onApplyTabLayout();


        ownToolBar.setBinding("Nguyễn Phúc Hậu", R.drawable.ic_vector_add, R.drawable.ic_vector_add,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
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
/*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
/*
        Picasso.with(this).load(this.appConfig.getCurrentUser().getCoverUrl()).into(imageCover);
        Picasso.with(this).load(this.appConfig.getCurrentUser().getAvatarUrl()).into(imageAvatar);

        txtGender.setText(this.appConfig.getCurrentUser().getGender());
        textAccountName.setText(this.appConfig.getCurrentUser().getName());

        txtPhotoNumber.setText("1.5K photos");
        txtFriendsNumber.setText("2K friends");*/
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

    private void onApplyTabLayout() {

        this.mTabLayout.setupWithViewPager(mViewPager);

        TabLayout.Tab tab = this.mTabLayout.getTabAt(0);
        if (tab != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_home));
        }
        if ((tab = this.mTabLayout.getTabAt(1)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_search));
        }
        this.mTabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ProfileBaseInfoFragment());
        adapter.addFrag(new ProfileTripFragment());

        this.mViewPager.setAdapter(adapter);
        this.mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }
}
