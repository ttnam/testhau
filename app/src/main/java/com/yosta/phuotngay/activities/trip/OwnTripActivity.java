package com.yosta.phuotngay.activities.trip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yosta.phuotngay.R;
import com.yosta.utils.AppUtils;
import com.yosta.phuotngay.ui.viewpagercards.CardFragmentPagerAdapter;
import com.yosta.phuotngay.ui.viewpagercards.ShadowTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OwnTripActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_trip);

        ButterKnife.bind(this);

        this.mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));


        this.mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
        this.mFragmentCardShadowTransformer.enableScaling(true);

        onApplyRecyclerView();
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void onApplyRecyclerView() {

        int height = AppUtils.onGetScreenHeight(this) / 2;
        this.mViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height));
        this.mViewPager.setAdapter(mFragmentCardAdapter);
        this.mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        this.mViewPager.setOffscreenPageLimit(5);

    }
}
