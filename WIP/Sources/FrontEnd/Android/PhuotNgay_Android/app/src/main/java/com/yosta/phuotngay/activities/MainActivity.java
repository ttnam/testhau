package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.SearchDialog;
import com.yosta.phuotngay.activities.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapters.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;
import com.yosta.phuotngay.fragments.HomeFragment;
import com.yosta.phuotngay.fragments.MenuSettingFragment;
import com.yosta.phuotngay.fragments.NoConnectionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActivityBehavior {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        startActivity(new Intent(this, FrescoActivity.class));


        /*onApplyViewPager();
        onApplyTabLayout();*/

    }

    private void onApplyTabLayout() {

        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_home));
        }

        if ((tab = tabLayout.getTabAt(1)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_group));
        }

        if ((tab = tabLayout.getTabAt(2)) != null) {
            tab.setIcon(getResources().getDrawable(R.drawable.ic_style_tab_menu));
        }

        tabLayout.setSmoothScrollingEnabled(true);
    }

    private void onApplyViewPager() {
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.addFrag(new HomeFragment());
        adapter.addFrag(new NoConnectionFragment());
        adapter.addFrag(new MenuSettingFragment());
    }

    @OnClick(R.id.textView)
    public void onShowSearchDialog() {
        SearchDialog searchDialog = new SearchDialog(this);
        searchDialog.show();
    }
}
