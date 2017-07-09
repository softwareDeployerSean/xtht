package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerDateSelectComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.DateSelectModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.DateSelectContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.DateSelectPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.CalendarCard;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.CalendarViewAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.CustomDate;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.DatePageImte;
import com.walnutin.xtht.bracelet.mvp.ui.widget.date.VerticalViewPager;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DateSelectActivity extends BaseActivity<DateSelectPresenter> implements DateSelectContract.View, View.OnClickListener, CalendarCard.OnCellClickListener {

    private VerticalViewPager mViewPager;
    private int mCurrentIndex = 498;
    private DatePageImte[] mShowViews;
    private CalendarViewAdapter adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    DatePageImte[] items = new DatePageImte[3];

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
        mViewPager = (VerticalViewPager) this.findViewById(R.id.vp_calendar);

        for (int i = 0; i < 3; i++) {
            DatePageImte item = new DatePageImte(this);
            items[i] = item;
        }
        adapter = new CalendarViewAdapter<>(items);
        setViewPager();

        mViewPager.setCurrentItem(498);

        updateNearPage(498);


    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {

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

    private void updateNearPage(int position) {
        measureDirection(position);
        updateCalendarView(position);

        DatePageImte item = items[position % items.length];
        CustomDate currentShowDate = item.getCurrentCalendar().getShowDate();

        Log.d("TAG", "currentShowDate.day=" + currentShowDate.day);

        item.update(position);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void clickDate(CustomDate date) {
        Intent intent = new Intent();
        intent.putExtra("selectedDate", date.toString());
        setResult(1001, intent);
        finish();
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