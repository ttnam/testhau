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
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.fragments.NotificationsFragment;
import io.yostajsc.izigo.fragments.OwnTripFragment;
import io.yostajsc.izigo.fragments.TripFragment;

public class MainActivity extends ActivityCoreBehavior {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    NonSwipeAbleViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onApplyViews();

    }

    @Override
    public void onApplyViews() {

        onApplyViewPager();
        onApplyTabLayout();

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
        this.mTabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        IconViewPagerAdapter adapter = new IconViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TripFragment());
        adapter.addFrag(new OwnTripFragment());
        adapter.addFrag(new NotificationsFragment());

        this.mViewPager.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int pageType = intent.getIntExtra(AppDefine.PAGE_ID, -1);
        if (pageType == PageType.NOTIFICATION)
            this.mViewPager.setCurrentItem(2, true);
    }

    @Override
    public void onInternetDisConnected() {

    }

    @Override
    public void onInternetConnected() {

    }
}
