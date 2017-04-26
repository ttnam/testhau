package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.constants.PageType;
import io.yostajsc.core.designs.tabs.IconViewPagerAdapter;
import io.yostajsc.core.designs.viewpager.NonSwipeAbleViewPager;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.AppConfig;
import io.yostajsc.izigo.fragments.NotificationsFragment;
import io.yostajsc.izigo.fragments.OwnTripFragment;
import io.yostajsc.izigo.fragments.SettingsFragment;
import io.yostajsc.izigo.fragments.TripFragment;

public class MainActivity extends OwnCoreActivity {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    NonSwipeAbleViewPager mViewPager;

    private TripFragment tripFragment = new TripFragment();
    private OwnTripFragment ownTripFragment = new OwnTripFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onApplyViewPager();
        onApplyTabLayout();

        if (!NetworkUtils.isNetworkConnected(this)) {
            onInternetDisConnected();
        }
    }

    private void onApplyTabLayout() {

        this.mTabLayout.setupWithViewPager(mViewPager);

        TabLayout.Tab tab = this.mTabLayout.getTabAt(0);
        if (tab != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_home));
        }
        if ((tab = this.mTabLayout.getTabAt(1)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_trip));
        }
        if ((tab = this.mTabLayout.getTabAt(2)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_noti));
        }
        if ((tab = this.mTabLayout.getTabAt(3)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_menu));
        }
        this.mTabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        IconViewPagerAdapter adapter = new IconViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(tripFragment);
        adapter.addFrag(ownTripFragment);
        adapter.addFrag(notificationsFragment);
        adapter.addFrag(settingsFragment);
        this.mViewPager.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int pageType = intent.getIntExtra(AppConfig.PAGE_ID, -1);
        if (pageType == PageType.NOTIFICATION)
            this.mViewPager.setCurrentItem(2, true);
    }

    @Override
    public void onInternetDisConnected() {
        super.onInternetDisConnected();
    }

    @Override
    public void onInternetConnected() {
        super.onInternetConnected();
        if (this.mViewPager.getCurrentItem() == 0) {
            tripFragment.processingLoadPublicTripsFromServer();
        }
    }

    @Override
    public void onExpired() {
        super.onExpired();
    }
}
