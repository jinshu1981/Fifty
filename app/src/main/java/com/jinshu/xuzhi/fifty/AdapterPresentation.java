package com.jinshu.xuzhi.fifty;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by xuzhi on 2016/9/7.
 */
public class AdapterPresentation extends FragmentStatePagerAdapter {
    private final int mSize;

    public AdapterPresentation(FragmentManager fm, int size) {
        super(fm);
        mSize = size;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailFragment.newInstance(position);
    }


}