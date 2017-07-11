package com.walnutin.xtht.bracelet.mvp.ui.widget.date;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.DateSelectActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

}
