package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.designs.tabs.IconViewPagerAdapter;
import io.yostajsc.core.designs.viewpager.NonSwipeAbleViewPager;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.izigo.fragments.MapsFragment;
import io.yostajsc.izigo.fragments.TimeLineFragment;
import io.yostajsc.izigo.R;
import io.yostajsc.utils.LocationUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TripTimelineActivity extends OwnCoreActivity {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    NonSwipeAbleViewPager mViewPager;

    private MapsFragment mapsFragment = new MapsFragment();
    private TimeLineFragment timeLineFragment = new TimeLineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_timeline);
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
        this.mTabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        IconViewPagerAdapter adapter = new IconViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(mapsFragment);
        adapter.addFrag(timeLineFragment);
        this.mViewPager.setAdapter(adapter);
    }


    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void askGPS() {
        if (!LocationUtils.Gps.connect().isEnable())
            LocationUtils.Gps.request(this).askGPS();
    }

    @Override
    protected void onGpsOff() {
        super.onGpsOff();
        TripTimelineActivityPermissionsDispatcher.askGPSWithCheck(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TripTimelineActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MessageType.USER_GPS) {
            if (resultCode != RESULT_OK) {
                TripTimelineActivityPermissionsDispatcher.askGPSWithCheck(this);
            }
        }
    }
}
