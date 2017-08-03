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
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Data_run;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.SleepDayPageItem;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.SportDayPageItem;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerSportDaySelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportDaySelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportDaySelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.SportDaySelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CanotSlidingViewpager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SportDaySelectedFragment extends BaseFragment<SportDaySelectedPresenter> implements SportDaySelectedContract.View {

    private String date;

    @BindView(R.id.sport_day_viewpger)
    public CanotSlidingViewpager viewpager;
    private SportDayPageItem[] items = new SportDayPageItem[3];

    private SportDayAdapter viewPagerAdapter;
    private String currentDate;

    private int currentIndexItem = 1001;
    int position_tag = 1001;

    public static SportDaySelectedFragment newInstance() {
        SportDaySelectedFragment fragment = new SportDaySelectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerSportDaySelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .sportDaySelectedModule(new SportDaySelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SportDayPageItem item = null;
        for (int i = 0; i < items.length; i++) {
            item = new SportDayPageItem(this.getActivity());
            items[i] = item;
        }

        return inflater.inflate(R.layout.fragment_sport_day_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c = Calendar.getInstance();
//        currentDate = sdf.format(c.getTime());
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
        viewPagerAdapter = new SportDayAdapter(items);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(1001);
    }
    private void updateUi(int position) {
        try {
            //获取当前显示的HomePageItem
            SportDayPageItem item = items[position % 3];

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(currentDate));
            int day = calendar.get(Calendar.DATE);
            if (position > currentIndexItem) {
                calendar.set(Calendar.DATE, day + 1);
            } else if (position < currentIndexItem) {
                calendar.set(Calendar.DATE, day - 1);
            }

            String updateDate = sdf.format(calendar.getTime());

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

    public void setDate(String date) {
        this.date = date;
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

    private class SportDayAdapter<V> extends PagerAdapter {

        private V[] items;

        public SportDayAdapter(V[] items) {
            super();
            this.items = items;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (((ViewPager) container).getChildCount() == items.length) {
                ((ViewPager) container).removeView(((SportDayPageItem) items[position % items.length]).getView());
            }

            ((ViewPager) container).addView(((SportDayPageItem) items[position % items.length]).getView(), 0);
            return ((SportDayPageItem) items[position % items.length]).getView();
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