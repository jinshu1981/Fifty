package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by xuzhi on 2016/10/7.
 */
public class AdapterDetailPresentation extends FragmentStatePagerAdapter {
    //private final int mSize;
    private String[] mPresentationUrls;
    public AdapterDetailPresentation(FragmentManager fm, int size,Context context,String[] presentationUrls) {
        super(fm);
        //mContext = context;
        mPresentationUrls = presentationUrls;
        //mSize = mPresentationUrls.length;
    }

    @Override
    public int getCount() {
        return mPresentationUrls.length;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailFragment.newInstance(position,mPresentationUrls[position]);
    }


}
