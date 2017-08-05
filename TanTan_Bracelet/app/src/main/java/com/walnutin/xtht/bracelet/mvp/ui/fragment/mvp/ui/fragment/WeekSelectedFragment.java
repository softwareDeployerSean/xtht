package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.SleepDayPageItem;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.SleepWeekPageItem;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerWeekSelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.WeekSelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.WeekSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.WeekSelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CanotSlidingViewpager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class WeekSelectedFragment extends BaseFragment<WeekSelectedPresenter> implements WeekSelectedContract.View {

    private String date;
    private String currentDate;

    private int currentIndexItem = 1001;
    int position_tag = 1001;

    @BindView(R.id.sleep_week_viewpger)
    public CanotSlidingViewpager viewpager;
    private SleepWeekPageItem[] items = new SleepWeekPageItem[3];

    private SleepWeekAdapter viewPagerAdapter;

    public static WeekSelectedFragment newInstance() {
        WeekSelectedFragment fragment = new WeekSelectedFragment();
        return fragment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerWeekSelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .weekSelectedModule(new WeekSelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SleepWeekPageItem item = null;
        for (int i = 0; i < items.length; i++) {
            item = new SleepWeekPageItem(this.getActivity());
            items[i] = item;
        }
        return inflater.inflate(R.layout.fragment_week_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        currentDate = this.date;

        viewpager.setScrollble(false);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                position_tag = position;
                updateUi(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPagerAdapter = new SleepWeekAdapter(items);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(1001);
    }

    private void updateUi(int position) {
        try {
            //获取当前显示的HomePageItem
            SleepWeekPageItem item = items[position % 3];

            String updateDate = "";
            if (position > currentIndexItem) {
                updateDate = getNextWeekToday(currentDate);
            } else if (position < currentIndexItem) {
                updateDate = getLastWeekToday(currentDate);
            }else if(position == currentIndexItem) {
                updateDate = date;
            }

            item.update(updateDate);

            currentIndexItem = position;
            currentDate = item.getDate();

            if (isNow(currentDate)) {
                viewpager.setScrollble(false);
            } else {
                viewpager.setScrollble(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLastWeekToday(String da) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(da);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        long time = calendar.getTimeInMillis();
        System.out.println("time =" + time);
        long time1 = time - 7 * 24 * 60 * 60 * 1000;
        System.out.println("time1=" + time1);
        calendar.setTimeInMillis(time1);

        return format.format(calendar.getTime());
    }

    private String getNextWeekToday(String da) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(da);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        long time = calendar.getTimeInMillis();
        System.out.println("time =" + time);
        long time1 = time + 7 * 24 * 60 * 60 * 1000;
        System.out.println("time1=" + time1);
        calendar.setTimeInMillis(time1);

        return format.format(calendar.getTime());
    }

    private boolean isNow(String date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //获取今天的日期
        String nowDay = sf.format(now);

        LogUtils.debugInfo("nowDay=" + nowDay + ", comDate=" + date);
        LogUtils.debugInfo("day.equals(nowDay)=" + date.equals(nowDay));

        return date.equals(nowDay);

    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    private class SleepWeekAdapter<V> extends PagerAdapter {

        private V[] items;

        public SleepWeekAdapter(V[] items) {
            super();
            this.items = items;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (((ViewPager) container).getChildCount() == items.length) {
                ((ViewPager) container).removeView(((SleepWeekPageItem) items[position % items.length]).getView());
            }

            ((ViewPager) container).addView(((SleepWeekPageItem) items[position % items.length]).getView(), 0);
            return ((SleepWeekPageItem) items[position % items.length]).getView();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) container);
        }
    }

}