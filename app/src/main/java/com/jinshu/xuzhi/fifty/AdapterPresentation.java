package com.jinshu.xuzhi.fifty;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by xuzhi on 2016/9/7.
 */
public class AdapterPresentation extends FragmentStatePagerAdapter {
   /* private final int mSize;

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
    }*/
   private String[] mPresentationUrls;
   public AdapterPresentation(FragmentManager fm, /*int size,Context context,*/String[] presentationUrls) {
       super(fm);
       //mContext = context;
       mPresentationUrls = presentationUrls;
       //mSize = mPresentationUrls.length;
   }

    @Override
    public int getCount() {
        System.out.println("AdapterPresentation:getCount = " + mPresentationUrls.length);
        return mPresentationUrls.length;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("AdapterPresentation:getItem");
        return ImageDetailFragment.newInstance(position,mPresentationUrls[position]);
    }


}