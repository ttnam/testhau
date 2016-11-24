package com.yosta.phuotngay.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OwnTripFragment extends Fragment {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);

        onApplyViewPager();
        onApplyTabLayout();

        return rootView;
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
        tabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.addFrag(new NoConnectionFragment());
        adapter.addFrag(new NoConnectionFragment());
    }
}
