package io.yostajsc.designs.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

/**
 * Created by HenryPhuc on 3/6/2016.
 */

public class IconViewPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<Fragment> mFragmentList = new SparseArray<>();

    private int index = 0;

    public IconViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.put((index++), fragment);
        notifyDataSetChanged();
    }
}