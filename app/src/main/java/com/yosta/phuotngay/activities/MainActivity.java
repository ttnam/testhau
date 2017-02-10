package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.dialogs.DialogFilter;
import com.yosta.phuotngay.adapters.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;
import com.yosta.phuotngay.fragments.NotificationsFragment;
import com.yosta.phuotngay.fragments.OwnTripFragment;
import com.yosta.phuotngay.fragments.SearchFragment;
import com.yosta.phuotngay.fragments.TripFragment;
import com.yosta.phuotngay.fragments.NoConnectionFragment;
import com.yosta.phuotngay.fragments.SettingFragment;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.OwnToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ActivityBehavior {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

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

    @Override
    protected void onResume() {
        super.onResume();
        this.mViewPager.setCurrentItem(0, true);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TripFragment());
        adapter.addFrag(new SearchFragment());
        adapter.addFrag(new OwnTripFragment());
        adapter.addFrag(new NotificationsFragment());
        adapter.addFrag(new SettingFragment());

        this.mViewPager.setAdapter(adapter);
        this.mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        mOwnToolbar.setBinding(
                                "Trang chủ",
                                R.drawable.ic_vector_add,
                                R.drawable.ic_vector_filter,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(MainActivity.this, CreateTripActivity.class));
                                    }
                                },
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DialogFilter dialog = new DialogFilter(MainActivity.this);
                                        dialog.show();
                                    }
                                }
                        );
                        break;
                    case 1:
                        mOwnToolbar.setBinding(getString(R.string.all_search), Integer.MIN_VALUE, Integer.MIN_VALUE);
                        break;
                    case 2:
                        mOwnToolbar.setBinding(getString(R.string.all_your_trip), Integer.MIN_VALUE, Integer.MIN_VALUE);
                        break;
                    case 3:
                        mOwnToolbar.setBinding(getString(R.string.all_noti), Integer.MIN_VALUE, Integer.MIN_VALUE);
                        break;
                    case 4:
                        mOwnToolbar.setBinding(getString(R.string.setting), Integer.MIN_VALUE, Integer.MIN_VALUE);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
