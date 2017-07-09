package com.walnutin.xtht.bracelet.mvp.ui.widget.date;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.DateSelectActivity;

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

    private TextView currentMonthTv;

    /**
     * Next Calendar
     */
    private CalendarCard nextCalendar;

    private TextView nextMonthTv;


    public DatePageImte(Context context) {
        this.mContext = context;

        view = LayoutInflater.from(mContext).inflate(R.layout.layout_date_item, null);

        currentCalendar = (CalendarCard) view.findViewById(R.id.current_calendar_card);

        nextCalendar = (CalendarCard) view.findViewById(R.id.next_calendar_card);

        currentMonthTv = (TextView) view.findViewById(R.id.current__month_tv);

        nextMonthTv = (TextView) view.findViewById(R.id.next_month_tv);

    }

    public void update(int position) {
        currentMonthTv.setText(currentCalendar.getShowDate().getMonth() + "月");

        Log.d("TAG", "update() month=" + currentCalendar.getShowDate().getMonth());

        nextCalendar.getShowDate().year = currentCalendar.getShowDate().year;
        nextCalendar.getShowDate().month = currentCalendar.getShowDate().month;
        nextCalendar.getShowDate().day = currentCalendar.getShowDate().day;
        nextCalendar.nextMonth();
        nextMonthTv.setText(nextCalendar.getShowDate().getMonth() + "月");

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
