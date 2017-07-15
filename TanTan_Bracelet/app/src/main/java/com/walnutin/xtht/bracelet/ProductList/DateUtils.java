package com.walnutin.xtht.bracelet.ProductList;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DateUtils {



    /**
     * 得到某一天对应周的周末
     */
    public static Date getWeekEndDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 7 - weekDay); // 移到一周的开始 日期
        //   System.out.println(sdf.format(cal.getTime()));
        return cal.getTime();

    }


    // 得到 指定月份第一天日期
    public static Date getMonthStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_MONTH) - 1;
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        if (week > 0) {
            cal.add(Calendar.DATE, -week * 7);
        }
        if (weekDay == 1) {
            weekDay = 0;
        } else {
            weekDay = 1 - weekDay;
        }

        cal.add(Calendar.DATE, weekDay);

        return cal.getTime();

    }

    // 得到 指定月份  月尾最后一天日期
    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        if (week < 6) {
            cal.add(Calendar.DATE, (6 - week) * 7);
        }
        if (weekDay == 7) {
            weekDay = 0;
        } else {
            weekDay = 7 - weekDay;
        }

        cal.add(Calendar.DATE, weekDay);

        return cal.getTime();

    }


    // 根据指定日期 移动几天
    public static Date dayToOffsetWeekDate(Date date, int pos) {
        return dateByAddingDate(date, 0, 0, pos * 7, 0, 0, 0);
    }

    // 根据指定日期 移动几天
    public static String dayToOffsetMonthDate(Date date, int pos) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(date);
        localCalendar.add(Calendar.MONTH, pos);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(localCalendar.getTime());
    }


    // 根据指定日期 推迟一周
    public static Date oneWeekNext(Date date) {
        return dateByAddingDate(date, 0, 0, 7, 0, 0, 0);
    }

    public static Date oneWeekPrevious(Date date) {
        return dateByAddingDate(date, 0, 0, -7, 0, 0, 0);
    }

    private static Date dateByAddingDate(Date date, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(date);
        localCalendar.add(Calendar.YEAR, paramInt1);
        localCalendar.add(Calendar.MONTH, paramInt2);
        localCalendar.add(Calendar.DATE, paramInt3);
        localCalendar.add(Calendar.HOUR_OF_DAY, paramInt4);
        localCalendar.add(Calendar.MINUTE, paramInt5);
        localCalendar.add(Calendar.SECOND, paramInt6);
        return localCalendar.getTime();
    }

    public static int getCurrentDayGapLastMonday() { //得到当前时间与上周一的 页数 ，
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay + 7;
    }

    public static String getBeforeDate(Date date, int prev) {      // 往 推 prev天
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, prev);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(cal.getTime());
    }

    public static Date getOffsetDate(int prev) {      // 往 推 prev天
        Calendar cal = Calendar.getInstance();
        //  cal.setTime(date);
        cal.add(Calendar.DATE, prev);
        //   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return cal.getTime();
    }

    // 0为 上周一距离今天的数据位置， 1为三周数据
    public static int getTodayPosition(int type) {
        if (type == 0) {
            return getCurrentDayGapLastMonday();
        } else if (type == 1) {

        }
        return 0;
    }

    public static String formatData(String time) { //将年月日  转成月日 格式
        Date date = new Date();
//	   Calendar cal = Calendar.getInstance();
//	   cal.set(1016, 11, 30);
//	   date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("M/dd");
        return sdf1.format(date);


    }

    public static String formatYearData(String time) { //将年月日  转成年月 格式
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM");
        return sdf1.format(date);
    }

    public static String formatMonthData(String time) { //将年月日 转成 周格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatData = "";
        try {
            Date startDate = stringToDate(time);
            String weekStart;
            String weekEnd;
            weekStart = DateUtils.formatData(time);
            weekEnd = DateUtils.formatData(sdf.format(DateUtils.dayToOffsetWeekDate(startDate, 6)));
            formatData = weekStart + "-" + weekEnd;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return formatData;
    }


    public static int daysOfTwo(Date fDate, Date oDate) { //fDate 开始日期 比较两个日期相差的天数
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;

    }

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date tmp;
        tmp = sdf.parse(date);
        return tmp;
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static List<String> getOneMonthDate(Date date) {  //
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd yyyy"); //设置时间格式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        List<String> dateList = new ArrayList<String>();

        int dateSum = 90;
        calendar.add(Calendar.DATE, -90);
        for (int i = 0; i < dateSum; i++) {
            calendar.add(Calendar.DATE, 1);
            String time = sdf.format(calendar.getTime());
            //  System.out.println(time);
            dateList.add(time);
        }
        return dateList;
    }

    public static List<String> getOneYearDate(Date date) { // 得到近一年的近况
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dateSum = 12;
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < dateSum; i++) {
            calendar.add(Calendar.MONTH, 1);
            String time = sdf.format(calendar.getTime());
            dateList.add(time);
            //System.out.println(time);
        }


        return dateList;

    }

    public static List<String> get12WeekDate(Date date) {  // 构造 12周的周一的日期数据，
        List<String> dateList = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekDay == 1) {
            weekDay = 0;
        } else {
            weekDay = 1 - weekDay;
        }
        calendar.add(Calendar.DATE, weekDay); // 移到一周的开始 日期
        int dateSum = 12;
        calendar.add(Calendar.DATE, -7 * 12);
        for (int i = 0; i < dateSum; i++) {
            calendar.add(Calendar.DATE, 7);
            String time = sdf.format(calendar.getTime());
            dateList.add(time);
            //System.out.println(time);
        }

        //System.out.println("yyy:" + sdf.format(cal.getTime()));
        return dateList;

    }


    public static List<String> getWeekDates(Date date) { // 得到一周的日期  周天-周六
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 0;
        } else {
            weekDay = 1 - weekDay;
        }
        cal.add(Calendar.DATE, weekDay); // 移到一周的开始 日期

        for (int i = 0; i < 7; i++) {
            dateList.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }

        return dateList;
    }


    public static List<String> getWeekDate(Date date) { // 得到一周的日期  周1一  一 周天
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = -6;
        } else {
            weekDay = 2 - weekDay;
        }
        cal.add(Calendar.DATE, weekDay); // 移到一周的开始 日期

        for (int i = 0; i < 7; i++) {
            dateList.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }

        return dateList;
    }
}


