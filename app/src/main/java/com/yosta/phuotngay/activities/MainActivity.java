package com.yosta.phuotngay.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.IconViewPagerAdapter;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.fragments.NotificationsFragment;
import com.yosta.phuotngay.fragments.OwnTripFragment;
import com.yosta.phuotngay.fragments.SearchFragment;
import com.yosta.phuotngay.fragments.TripFragment;
import com.yosta.phuotngay.fragments.SettingFragment;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.interfaces.CallBackLocationsParam;
import com.yosta.phuotngay.interfaces.CallBackStringParam;
import com.yosta.phuotngay.interfaces.CallBackTripsParam;
import com.yosta.phuotngay.managers.RealmManager;
import com.yosta.phuotngay.models.base.Locations;
import com.yosta.phuotngay.models.trip.BaseTrip;
import com.yosta.phuotngay.services.api.APIManager;

import java.util.List;

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

    @Override
    protected void onResume() {
        super.onResume();

        String authorization = StorageHelper.inject(this).getString(User.AUTHORIZATION);
        APIManager.connect().onGetLocation(authorization, new CallBackLocationsParam() {
            @Override
            public void run(Locations locations) {
                RealmManager.insertOrUpdate(locations);
            }
        }, new CallBackStringParam() {
            @Override
            public void run(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        APIManager.connect().onGetTrips(authorization, new CallBackTripsParam() {
            @Override
            public void run(List<BaseTrip> trips) {

            }
        }, new CallBackStringParam() {
            @Override
            public void run(String res) {

            }
        });
    }
}
