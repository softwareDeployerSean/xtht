package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Leiht on 2017/7/1.
 */

public class HomeViewPagerAdapter extends PagerAdapter {
    @Override
    public void setPrimaryItem(ViewGroup container, int position,
                               Object object) {
    }

    @Override
    public int getCount() {
        return 1000;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return null;
    }
}
