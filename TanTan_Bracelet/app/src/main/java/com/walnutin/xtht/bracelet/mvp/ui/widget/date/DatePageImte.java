package com.walnutin.xtht.bracelet.mvp.ui.widget.date;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfo;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.DateSelectActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leiht on 2017/7/8.
 *
 * 第一个日历页面对应一个DatePageImte
 * 此项目里将其放入竖直滑动的ViewPager中，重复创建2个，复用
 *
 */

public class DatePageImte {

    /**
     * Context
     */
    private Context mContext;

    /**
     * View points layout_date_item.xml
     */
    private View view;

    /**
     * Current Calendar
     */
    private CalendarCard currentCalendar;

    /**
     * Next Calendar
     */
    private CalendarCard nextCalendar;

    /**
     * the parent LinearLayout for current month text
     */
    private LinearLayout currentMonthParent;

    /**
     * the parent LinearLayout for next month text
     */
    private LinearLayout nextMonthParent;

    private List<StepInfos> currentMonthStepList;

    private List<StepInfos> nextMonthStepList;

    private Map<Integer, Float> currentMonthRateMap;
    private Map<Integer, Float> nextMonthRateMap;


    public DatePageImte(Context context) {
        this.mContext = context;

        view = LayoutInflater.from(mContext).inflate(R.layout.layout_date_item, null);

        currentCalendar = (CalendarCard) view.findViewById(R.id.current_calendar_card);

        nextCalendar = (CalendarCard) view.findViewById(R.id.next_calendar_card);

        currentMonthParent = (LinearLayout) view.findViewById(R.id.currnet_month_parent_al);

        nextMonthParent = (LinearLayout) view.findViewById(R.id.next_month_parent_al);

//        currentMonthTv = (TextView) view.findViewById(R.id.current__month_tv);
//
//        nextMonthTv = (TextView) view.findViewById(R.id.next_month_tv);

    }

    public void update(int position) {

        for(int i = 0; i < currentMonthParent.getChildCount(); i++) {
            ((TextView)currentMonthParent.getChildAt(i)).setTextColor(Color.WHITE);
        }
        for(int i = 0; i < nextMonthParent.getChildCount(); i++) {
            ((TextView)nextMonthParent.getChildAt(i)).setTextColor(Color.WHITE);
        }

        String firstDayOfMonth = currentCalendar.getShowDate().getYear() + "-" + currentCalendar.getShowDate().getMonth() + "-01";
        int firstDayOfWeek = getWeekByDate(firstDayOfMonth);

        TextView currenTv = ((TextView)currentMonthParent.getChildAt(firstDayOfWeek - 1));
        currenTv.setTextColor(Color.BLACK);
        currenTv.setText(currentCalendar.getShowDate().getMonth() + "月");


        Log.d("TAG", "update() month=" + currentCalendar.getShowDate().getMonth());

        nextCalendar.getShowDate().year = currentCalendar.getShowDate().year;
        nextCalendar.getShowDate().month = currentCalendar.getShowDate().month;
        nextCalendar.getShowDate().day = currentCalendar.getShowDate().day;
        nextCalendar.nextMonth();

        String nextDayOfMonth = nextCalendar.getShowDate().getYear() + "-" + nextCalendar.getShowDate().getMonth() + "-01";
        int nextDayOfWeek = getWeekByDate(nextDayOfMonth);

        TextView nextTv = ((TextView)nextMonthParent.getChildAt(nextDayOfWeek - 1));
        nextTv.setTextColor(Color.BLACK);
        nextTv.setText(nextCalendar.getShowDate().getMonth() + "月");

//        Map<Integer, Float> rateMap;
//
//        rateMap = new HashMap<>();
//        rateMap.put(new Integer(1), (float)0.4);
//        rateMap.put(new Integer(15), (float)0.2);
//        rateMap.put(new Integer(20), (float)0.8);
//        rateMap.put(new Integer(26), (float)0.6);
//        rateMap.put(new Integer(12), (float)0.9);
//        rateMap.put(new Integer(3), (float)0.2);
//        currentCalendar.setRateMap(rateMap);
//
//        nextCalendar.setRateMap(rateMap);

        loadStepDatas();

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            currentMonthStepList = new ArrayList<>();
//            nextMonthStepList = new ArrayList<>();
//            StepInfos dailyInfo1 = new StepInfos();
//            String account = MyApplication.account;
//            dailyInfo1.setAccount(account);
//            dailyInfo1.setStep(6000);
//            dailyInfo1.setStepGoal(7000);
//            dailyInfo1.setCalories(100);
//            dailyInfo1.setDistance(100);
//            dailyInfo1.setUpLoad(100);
//            dailyInfo1.setDates(currentCalendar.getShowDate().getYear() + "-" + currentCalendar.getShowDate().getMonth() + "-08");
//
//            currentMonthStepList.add(dailyInfo1);
//
//            StepInfos dailyInfo2 = new StepInfos();
//            dailyInfo2.setAccount(account);
//            dailyInfo2.setStep(6000);
//            dailyInfo2.setStepGoal(7000);
//            dailyInfo2.setCalories(100);
//            dailyInfo2.setDistance(100);
//            dailyInfo2.setUpLoad(100);
//            dailyInfo2.setDates(currentCalendar.getShowDate().getYear() + "-" + currentCalendar.getShowDate().getMonth() + "-20");
//
//            currentMonthStepList.add(dailyInfo2);
//
//            StepInfos dailyInfo3 = new StepInfos();
//            dailyInfo3.setAccount(account);
//            dailyInfo3.setStep(6000);
//            dailyInfo3.setStepGoal(7000);
//            dailyInfo3.setCalories(100);
//            dailyInfo3.setDistance(100);
//            dailyInfo3.setUpLoad(100);
//            dailyInfo3.setDates(nextCalendar.getShowDate().getYear() + "-" + nextCalendar.getShowDate().getMonth() + "-08");
//
//            nextMonthStepList.add(dailyInfo3);
//
//            StepInfos dailyInfo4 = new StepInfos();
//            dailyInfo4.setAccount(account);
//            dailyInfo4.setStep(6000);
//            dailyInfo4.setStepGoal(7000);
//            dailyInfo4.setCalories(100);
//            dailyInfo4.setDistance(100);
//            dailyInfo4.setUpLoad(100);
//            dailyInfo4.setDates(nextCalendar.getShowDate().getYear() + "-" + nextCalendar.getShowDate().getMonth() + "-20");
//
//            nextMonthStepList.add(dailyInfo4);

            if(currentMonthStepList != null && currentMonthStepList.size() > 0) {
                currentMonthRateMap = new HashMap<>();
                StepInfos stepInfos = null;
                String preDate = "";
                int dayOfStep = 0;
                int day = 0;
                int goal = 0;
                for (int i = 0; i < currentMonthStepList.size(); i++) {
                    stepInfos = currentMonthStepList.get(i);
                    if(i == 0) {
                        preDate = stepInfos.getDates();
                        dayOfStep += stepInfos.getStep();
                        day = getDayByDate(stepInfos.getDates());
                        goal = stepInfos.getStepGoal();
                        if(goal <= 0) {
                            goal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                        }
                        if(currentMonthStepList.size() == 1) {
                            currentMonthRateMap.put(getDayByDate(stepInfos.getDates()), (float)dayOfStep/goal);
                        }
                    }else {
                        if(preDate.equals(stepInfos.getDates())) {
                            preDate = stepInfos.getDates();
                            dayOfStep += stepInfos.getStep();
                            day = getDayByDate(stepInfos.getDates());
                            goal = stepInfos.getStepGoal();
                            if(goal <= 0) {
                                goal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                            }
                            if(i == currentMonthStepList.size() - 1) {
                                currentMonthRateMap.put(day, (float)dayOfStep/goal);
                            }
                        }else {
                            currentMonthRateMap.put(day, (float)dayOfStep / goal);

                            preDate = stepInfos.getDates();
                            day = getDayByDate(stepInfos.getDates());
                            dayOfStep = stepInfos.getStep();
                            goal = stepInfos.getStepGoal();
                            if(goal <= 0) {
                                goal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                            }
                            if(i == currentMonthStepList.size() - 1) {
                                currentMonthRateMap.put(day, (float)dayOfStep/goal);
                            }
                        }
                    }
                }
                currentCalendar.setRateMap(currentMonthRateMap);
            }

            if(nextMonthStepList != null && nextMonthStepList.size() > 0) {
                nextMonthRateMap = new HashMap<>();
                StepInfos stepInfos = null;
                String preDate = "";
                int dayOfStep = 0;
                int day = 0;
                int goal = 0;
                for (int i = 0; i < nextMonthStepList.size(); i++) {
                    stepInfos = nextMonthStepList.get(i);
                    if(i == 0) {
                        preDate = stepInfos.getDates();
                        dayOfStep += stepInfos.getStep();
                        day = getDayByDate(stepInfos.getDates());
                        goal = stepInfos.getStepGoal();
                        if(goal <= 0) {
                            goal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                        }
                        if(nextMonthStepList.size() == 1) {
                            nextMonthRateMap.put(getDayByDate(stepInfos.getDates()), (float)dayOfStep/goal);
                        }
                    }else {
                        if(preDate.equals(stepInfos.getDates())) {
                            preDate = stepInfos.getDates();
                            dayOfStep += stepInfos.getStep();
                            day = getDayByDate(stepInfos.getDates());
                            goal = stepInfos.getStepGoal();
                            if(goal <= 0) {
                                goal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                            }
                            if(i == nextMonthStepList.size() - 1) {
                                nextMonthRateMap.put(day, (float)dayOfStep/goal);
                            }

                        }else {
                            nextMonthRateMap.put(day, (float)dayOfStep / goal);
                            preDate = stepInfos.getDates();
                            day = getDayByDate(stepInfos.getDates());
                            dayOfStep = stepInfos.getStep();
                            goal = stepInfos.getStepGoal();
                            if(goal <= 0) {
                                goal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                            }
                            if(i == nextMonthStepList.size() - 1) {
                                nextMonthRateMap.put(day, (float)dayOfStep/goal);
                            }
                        }
                    }

                }
                nextCalendar.setRateMap(nextMonthRateMap);
            }
        }
    };

    private void formateData() {

    }

    private void loadStepDatas() {
        new Thread() {
            @Override
            public void run() {
                currentMonthStepList = null;
                nextMonthStepList = null;
                String currentMonth = currentCalendar.getShowDate().getYear() + "-" + (currentCalendar.getShowDate().getMonth() < 10 ? "0" + currentCalendar.getShowDate().getMonth() : currentCalendar.getShowDate().getMonth());

                String nextMonth = nextCalendar.getShowDate().getYear() + "-" + (nextCalendar.getShowDate().getMonth() < 10 ? "0" + nextCalendar.getShowDate().getMonth() : nextCalendar.getShowDate().getMonth());


                currentMonthStepList = SqlHelper.instance().getMonthStepListByMonth(MyApplication.account, currentMonth);

                nextMonthStepList = SqlHelper.instance().getMonthStepListByMonth(MyApplication.account, nextMonth);

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private int getDayByDate(String date) {
        int day = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            Date d = sdf.parse(date);
            calendar.setTime(d);

            day = calendar.get(Calendar.DAY_OF_MONTH);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    private int getWeekByDate(String date) {
        int week = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            Date d = sdf.parse(date);

            calendar.setTime(d);

            week = calendar.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week;
    }

    public View getView() {
        return this.view;
    }

    public CalendarCard getCurrentCalendar() {
        return this.currentCalendar;
    }

    public CalendarCard getNextCalendar() {
        return this.nextCalendar;
    }

    public int getContentHeight() {
        return ((DateSelectActivity)mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
    }

    public int getStatusBarHeight() {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    //获取屏幕的宽度
    public int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    //获取屏幕的高度
    public int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public Map<Integer, Float> getCurrentMonthRateMap() {
        return this.currentMonthRateMap;
    }

    public Map<Integer, Float> getNextMonthRateMap() {
        return this.nextMonthRateMap;
    }

}
