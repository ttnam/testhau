package io.yostajsc.izigo.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.designs.tabs.IconViewPagerAdapter;
import io.yostajsc.izigo.R;
import io.yostajsc.backend.config.APIManager;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.fragments.NotificationsFragment;
import io.yostajsc.izigo.fragments.OwnTripFragment;
import io.yostajsc.izigo.fragments.TripFragment;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.base.Locations;
import io.yostajsc.utils.StorageUtils;

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
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_trip));
        }
        if ((tab = this.mTabLayout.getTabAt(1)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_home));
        }
        if ((tab = this.mTabLayout.getTabAt(2)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_noti));
        }
        this.mTabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        IconViewPagerAdapter adapter = new IconViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OwnTripFragment());
        adapter.addFrag(new TripFragment());
        adapter.addFrag(new NotificationsFragment());

        this.mViewPager.setAdapter(adapter);
        this.mViewPager.setCurrentItem(1, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().getLocation(authorization, new CallBackWith<Locations>() {
            @Override
            public void run(Locations locations) {
                RealmManager.insertOrUpdate(locations);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onInternetDisconnected() {

    }

    @Override
    protected void onInternetConnected() {

    }
}
