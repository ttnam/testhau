package io.yostajsc.izigo.usecase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.constants.PageType;
import io.yostajsc.sdk.designs.tabs.IconViewPagerAdapter;
import io.yostajsc.sdk.designs.transformer.NonSwipeAbleViewPager;
import io.yostajsc.sdk.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.usecase.notification.NotificationFragment;
import io.yostajsc.izigo.usecase.trip.fragment.OwnTripFragment;
import io.yostajsc.izigo.usecase.user.SettingsFragment;
import io.yostajsc.izigo.usecase.trip.fragment.TripFragment;

public class MainActivity extends OwnCoreActivity {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    NonSwipeAbleViewPager mViewPager;

    private TripFragment tripFragment = new TripFragment();
    private OwnTripFragment ownTripFragment = new OwnTripFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private NotificationFragment notificationFragment = new NotificationFragment();

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
        adapter.addFrag(this.tripFragment);
        adapter.addFrag(this.ownTripFragment);
        adapter.addFrag(this.notificationFragment);
        adapter.addFrag(this.settingsFragment);
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
            tripFragment.loadTripsFromServer();
        }
    }

    @Override
    public void expired() {
        super.expired();
    }
}
