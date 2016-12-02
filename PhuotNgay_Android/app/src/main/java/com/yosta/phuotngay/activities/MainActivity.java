package com.yosta.phuotngay.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;
import com.yosta.phuotngay.fragments.OwnTripFragment;
import com.yosta.phuotngay.fragments.SearchFragment;
import com.yosta.phuotngay.fragments.TripFragment;
import com.yosta.phuotngay.fragments.NoConnectionFragment;
import com.yosta.phuotngay.fragments.SettingFragment;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ActivityBehavior {


    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.text_view)
    TextView mTvTitle;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TripFragment());
        adapter.addFrag(new SearchFragment());
        adapter.addFrag(new OwnTripFragment());
        adapter.addFrag(new NoConnectionFragment());
        adapter.addFrag(new SettingFragment());

        this.mViewPager.setAdapter(adapter);
        this.mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        mTvTitle.setText("Trang chủ");
                        break;
                    case 1:
                        mTvTitle.setText("Tìm kiếm");
                        break;
                    case 2:
                        mTvTitle.setText("Hành trình của bạn");
                        break;
                    case 3:
                        mTvTitle.setText("Thông báo");
                        break;
                    case 4:
                        mTvTitle.setText("Cài đặt");
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
