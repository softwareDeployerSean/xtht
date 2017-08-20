package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerDateSelectComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.DateSelectModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.DateSelectContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.DateSelectPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.CalendarCard;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.CalendarViewAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.CustomDate;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.DatePageImte;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.DateUtil;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.CanotSlidingVerticalViewpager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DateSelectActivity extends BaseActivity<DateSelectPresenter> implements DateSelectContract.View, View.OnClickListener, CalendarCard.OnCellClickListener {

    private CanotSlidingVerticalViewpager mViewPager;
    private int mCurrentIndex = 498;
    private DatePageImte[] mShowViews;
    private CalendarViewAdapter adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    private String date;
    private String monthDate;

    DatePageImte[] items = new DatePageImte[3];
    DatePageImte currentItem = null;

    @BindView(R.id.toolbar_title)
    public TextView toolBarTitle;

    private SqlHelper sqlHelper;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerDateSelectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .dateSelectModule(new DateSelectModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_date_select; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mViewPager = (CanotSlidingVerticalViewpager) this.findViewById(R.id.vp_calendar);

        for (int i = 0; i < 3; i++) {
            DatePageImte item = new DatePageImte(this);
            items[i] = item;
        }
        adapter = new CalendarViewAdapter<>(items);
        setViewPager();

        mViewPager.setCurrentItem(498);

        updateNearPage(498);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        mViewPager.setScrollbleButton(false);

        sqlHelper = SqlHelper.instance();
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d("TAG", "--------------onPageSelected-------------------");
                updateNearPage(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private boolean isCurrentMonth(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        LogUtils.debugInfo("now=" + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH));

        LogUtils.debugInfo("calendar=" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH));

        return  calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR);
    }

    private void updateNearPage(int position) {
        measureDirection(position);
        updateCalendarView(position);

        currentItem = items[position % items.length];
        CustomDate currentShowDate = currentItem.getCurrentCalendar().getShowDate();

        Log.d("TAG", "currentShowDate.day=" + currentShowDate.day);

        currentItem.update(position);

        //更新下一个页面
        DatePageImte nextPage = items[(position + 1) % items.length];
        nextPage.getCurrentCalendar().getShowDate().year = currentShowDate.year;
        nextPage.getCurrentCalendar().getShowDate().month = currentShowDate.month;
        nextPage.getCurrentCalendar().getShowDate().day = currentShowDate.day;

        //更新上一个页面
        DatePageImte prePage = items[(position - 1) % items.length];
        prePage.getCurrentCalendar().getShowDate().year = currentShowDate.year;
        prePage.getCurrentCalendar().getShowDate().month = currentShowDate.month;
        prePage.getCurrentCalendar().getShowDate().day = currentShowDate.day;

        Log.d("TAG", "---------after currentShowDate month=" + currentShowDate.getMonth());

        CalendarCard currentCalendarCard = currentItem.getCurrentCalendar();
        CalendarCard nextCalendarCard = currentItem.getNextCalendar();

        String firstShowDate = currentCalendarCard.getShowDate().getYear() + "-" + currentCalendarCard.getShowDate().getMonth() + "-01";
        String secondShowDate = nextCalendarCard.getShowDate().getYear() + "-" + nextCalendarCard.getShowDate().getMonth() + "-01";

        if (isCurrentMonth(firstShowDate) || isCurrentMonth(secondShowDate)) {
            mViewPager.setScrollbleButton(false);
        } else {
            mViewPager.setScrollbleButton(true);
        }

        toolBarTitle.setText(String.valueOf(currentItem.getCurrentCalendar().getShowDate().getYear()));

        String queryDate = currentCalendarCard.getShowDate().getYear() + "-" + (currentCalendarCard.getShowDate().getMonth() < 10 ? "0" + currentCalendarCard.getShowDate().getMonth() : currentCalendarCard.getShowDate().getMonth()) + "-01";
        boolean isScroll = SqlHelper.instance().exitsStepsByDate(MyApplication.account, queryDate);
        if(isScroll) {
            mViewPager.setCanScrollTop(true);
        }else {
            mViewPager.setCanScrollTop(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


    private boolean isNow(String dateTime) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //获取今天的日期
        String nowDay = sf.format(now);
        Date d = null;
        try {
            d = sf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String comDate = sf.format(d);
        //对比的时间
        String day = sf.format(d);

        LogUtils.debugInfo("nowDay=" + nowDay + ", comDate=" + comDate);
        LogUtils.debugInfo("day.equals(nowDay)=" + day.equals(nowDay));

        return comDate.equals(nowDay);

    }

    @Override
    public void clickDate(CustomDate date) {
        boolean canClick = false;

        Map<Integer, Float> currentMonthRateMap = currentItem.getCurrentMonthRateMap();
        Map<Integer, Float> nextMonthRateMap = currentItem.getNextMonthRateMap();

        CalendarCard currentCalendarCard = currentItem.getCurrentCalendar();
        CalendarCard nextCalendarCard = currentItem.getNextCalendar();

        if(date.getYear() == currentCalendarCard.getShowDate().getYear()
                && date.getMonth() == currentCalendarCard.getShowDate().getMonth()) {
            if(currentMonthRateMap != null && currentMonthRateMap.containsKey(date.getDay()) && currentMonthRateMap.get(date.day) > 0) {
                canClick = true;
            }

            if(isNow(currentCalendarCard.getShowDate().toString())) {
                canClick = true;
            }

        }else if(date.getYear() == nextCalendarCard.getShowDate().getYear()
                && date.getMonth() == nextCalendarCard.getShowDate().getMonth()) {
            if(nextMonthRateMap != null && nextMonthRateMap.containsKey(date.day) && nextMonthRateMap.get(date.day) > 0) {
                canClick = true;
            }
            if(isNow(nextCalendarCard.getShowDate().toString())) {
                canClick = true;
            }
        }

        if(canClick) {
            Intent intent = new Intent();
            intent.putExtra("selectedDate", date.toString());
            setResult(1001, intent);
            finish();
        }else {
            LogUtils.debugInfo("No data can't be click.");
        }
    }

    @Override
    public void changeDate(CalendarCard calendarCard, CustomDate date) {
//        monthText.setText(date.month + "月");
    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = (DatePageImte[]) adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].getCurrentCalendar().rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {

            mShowViews[arg0 % mShowViews.length].getCurrentCalendar().leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
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
        finish();
    }


}