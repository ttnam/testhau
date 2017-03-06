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
import io.yostajsc.izigo.backend.config.APIManager;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.fragments.NotificationsFragment;
import io.yostajsc.izigo.fragments.OwnTripFragment;
import io.yostajsc.izigo.fragments.SearchFragment;
import io.yostajsc.izigo.fragments.SettingFragment;
import io.yostajsc.izigo.fragments.TripFragment;
import io.yostajsc.izigo.interfaces.CallBackParam;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.base.Locations;
import io.yostajsc.izigo.utils.StorageUtils;

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

    @Override
    protected void onResume() {
        super.onResume();
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().onGetLocation(authorization, new CallBackParam<Locations>() {
            @Override
            public void run(Locations locations) {
                RealmManager.insertOrUpdate(locations);
            }
        }, new CallBackParam<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
