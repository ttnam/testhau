package com.yosta.phuotngay.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.IconViewPagerAdapter;
import com.yosta.phuotngay.fragments.NotificationsFragment;
import com.yosta.phuotngay.fragments.OwnTripFragment;
import com.yosta.phuotngay.fragments.SearchFragment;
import com.yosta.phuotngay.fragments.TripFragment;
import com.yosta.phuotngay.fragments.SettingFragment;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ActivityBehavior {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onApplyComponents();

    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();

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
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_search));
        }
        if ((tab = this.mTabLayout.getTabAt(2)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_trip));
        }
        if ((tab = this.mTabLayout.getTabAt(3)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_noti));
        }
        if ((tab = this.mTabLayout.getTabAt(4)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_menu));
        }
        this.mTabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        IconViewPagerAdapter adapter = new IconViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TripFragment());
        adapter.addFrag(new SearchFragment());
        adapter.addFrag(new OwnTripFragment());
        adapter.addFrag(new NotificationsFragment());
        adapter.addFrag(new SettingFragment());

        this.mViewPager.setAdapter(adapter);
    }
}
