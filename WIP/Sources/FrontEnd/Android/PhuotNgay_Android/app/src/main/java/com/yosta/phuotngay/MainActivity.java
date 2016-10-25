package com.yosta.phuotngay;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.dialog.DialogSearch;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapter.ViewPagerAdapter;
import com.yosta.phuotngay.animations.ZoomOutPageTransformer;
import com.yosta.phuotngay.fragment.HomeFragment;
import com.yosta.phuotngay.fragment.SettingFragment;
import com.yosta.phuotngay.fragment.NoConnectionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActivityBehavior {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private DialogSearch searchDialog = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }

        ButterKnife.bind(this);
        searchDialog = new DialogSearch(this);
        // startActivity(new Intent(this, ImageryActivity.class));

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
        adapter.addFrag(new SettingFragment());
    }

    @OnClick(R.id.textView)
    public void onShowSearchDialog() {
        if (!searchDialog.isShowing()) {
            searchDialog.show();
        }
    }
}
