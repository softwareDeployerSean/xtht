package com.walnutin.xtht.bracelet.mvp.ui.widget.date;

import android.view.View;
import android.view.ViewGroup;

public class CalendarViewAdapter<V> extends PagerAdapter {
    public static final String TAG = "CalendarViewAdapter";
    private V[] items;

    public CalendarViewAdapter(V[] items) {
        super();
        this.items = items;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (((VerticalViewPager) container).getChildCount() == items.length) {
            ((VerticalViewPager) container).removeView((((DatePageImte)items[position % items.length]).getView()));
        }

        ((VerticalViewPager) container).addView(((DatePageImte)items[position % items.length]).getView(), 0);
        return items[position % items.length];
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (((DatePageImte) object).getView());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((VerticalViewPager) container).removeView((View) container);
    }

    public V[] getAllItems() {
        return items;
    }

}