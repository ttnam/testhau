package com.yosta.phuotngay.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;
import com.yosta.phuotngay.fragments.TripFragment;
import com.yosta.phuotngay.fragments.NoConnectionFragment;
import com.yosta.phuotngay.fragments.SettingFragment;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ActivityBehavior {


    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onApplyViewPager();
        onApplyTabLayout();

    }

    private void onApplyTabLayout() {

        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_home));
        }
        if ((tab = tabLayout.getTabAt(1)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_search));
        }
        if ((tab = tabLayout.getTabAt(2)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_trip));
        }
        if ((tab = tabLayout.getTabAt(3)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_noti));
        }
        if ((tab = tabLayout.getTabAt(4)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_menu ));
        }
        tabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.addFrag(new TripFragment());
        adapter.addFrag(new NoConnectionFragment());
        adapter.addFrag(new NoConnectionFragment());
        adapter.addFrag(new NoConnectionFragment());
        adapter.addFrag(new SettingFragment());
    }
}
