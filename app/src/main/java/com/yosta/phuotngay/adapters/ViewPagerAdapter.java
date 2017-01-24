package com.yosta.phuotngay.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.HashMap;

/**
 * Created by HenryPhuc on 3/6/2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<Fragment> mFragmentList = new SparseArray<>();

    private int index = 0;

    public ViewPagerAdapter(FragmentManager manager) {
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