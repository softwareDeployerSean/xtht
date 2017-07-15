package com.walnutin.xtht.bracelet.ProductList;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    private static SimpleDateFormat mMessageFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", new DateFormatSymbols(Locale.US));
    private static DateFormat mMessageFormatTZ = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", new DateFormatSymbols(Locale.US));

    private static DateFormat sWeekFormat = new SimpleDateFormat(
            "MM-dd HH:mm", new DateFormatSymbols(Locale.US));
    private static DateFormat sDayFormat = new SimpleDateFormat(
            "HH:mm", new DateFormatSymbols(Locale.US));

    private static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
    private static String sDayAgo = "";
    private static String sHourAgo = "";
    private static String sMinAgo = "";
//	static {
//		mMessageFormatTZ.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//
//		sDayAgo = SApp.getInstance().getString(R.string.time_day_ago);
//		sHourAgo = SApp.getInstance().getString(R.string.time_hour_ago);
//		sMinAgo = SApp.getInstance().getString(R.string.time_min_ago);
//	}

    private static SimpleDateFormat mBirthdayFormat = new SimpleDateFormat(
            "yyyy-MM-dd", new DateFormatSymbols(Locale.US));

    private static SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd HH:mm");
    ;

    public synchronized static String formatTime(Context context, String tv) {
        String s = tv;
        try {
            Date date = mMessageFormat.parse(tv);
            long second = (System.currentTimeMillis() - date.getTime()) / 1000;
            if (second > 3600 && second <= 86400) {
                s = second / 3600 + sHourAgo;
            } else if (second > 59 && second <= 3600) {
                s = second / 60 + sMinAgo;
            } else if (second <= 59) {
                s = 1 + sMinAgo;
            } else {
                s = mDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static String formatTime(Context context, long ts) {
        String s = "";

        long second = (System.currentTimeMillis() - ts) / 1000;
        if (second > 86400) {
            s = second / 86400 + sDayAgo;
        } else if (second > 3600 && second <= 86400) {
            s = second / 3600 + sHourAgo;
        } else if (second > 59 && second <= 3600) {
            s = second / 60 + sMinAgo;
        } else if (second <= 59) {
            s = 1 + sMinAgo;
        } else {
            s = "";
        }
        return s;
    }

    public static long dayMills(long millsTime) {
        final long dayMillis = 1000 * 3600 * 24;
        long day = millsTime / dayMillis;
        day = day * dayMillis;
        return day;
    }

    public static boolean isToday(long millsTime) {
        final long dayMillis = 1000 * 3600 * 24;
        long day = millsTime / dayMillis;
        day = day * dayMillis;
        return day == TimeUtil.dayMills(System.currentTimeMillis());
    }


    public synchronized static String formatRealTime(Context context, long ts) {
        long currentTime = System.currentTimeMillis();
        long diffTime = (currentTime - todayTS()) / 1000;
        //Get the distance zero time difference today
        String s = "";
        Date date = new Date(ts);
        long second = (currentTime - ts) / 1000;
        if (second > 31536000) {
            s = mMessageFormat.format(date);
        } else if (second >= diffTime && second <= 31536000) {
            s = sWeekFormat.format(date);
        } else if (second < diffTime) {
            s = sDayFormat.format(date);
        }
        return s;
    }

    public synchronized static int diffTimeSec(String prev, String now) {
        try {
            Date d1 = mMessageFormat.parse(prev);
            Date d2 = mMessageFormat.parse(now);

            return (int) ((d2.getTime() - d1.getTime()) / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0xffffff;
    }

    public synchronized static String now() {
        Date d = new Date();
        return mMessageFormat.format(d);
    }

    @SuppressWarnings("deprecation")
    public static int nowYear() {
        Date d = new Date();
        return d.getYear();
    }

    @SuppressWarnings("deprecation")
    public static int nowHour() {
        Date d = new Date();
        return d.getHours();
    }

    public static String nowDate() {
        Date d = new Date();
        return day.format(d);
    }

    // We only got one SimpleDateFormat instance, if multi-entered, OOPS.
    // For more, see SimpleDateFormat.parse(), ChatInfo. FIXME
    public synchronized static long toTS(String s) {
        try {
            Date date = mMessageFormatTZ.parse(s);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    // We only got one SimpleDateFormat instance, if multi-entered, OOPS.
    // For more, see SimpleDateFormat.parse(), ChatInfo. FIXME
    public synchronized static String toStr(long ts) {
        Date d = new Date(ts);
        return mMessageFormat.format(d);
    }

    public static String toStr(Calendar c) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis();
        long time = (curTime - timeStamp) / 1000;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }


    @SuppressWarnings("deprecation")
    public static int toYear(String s) {
        try {
            Date date = mBirthdayFormat.parse(s);
            return date.getYear();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    public static int toYearRaw(String s) {
        try {
            Date date = mBirthdayFormat.parse(s);
            return date.getYear() + 1900;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentDate() {
        return mBirthdayFormat.format(new Date());
    }

    public static String getformatData(Date date) {
        return mBirthdayFormat.format(date);

    }

    public static long todayTS() {
        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);

        return current.getTimeInMillis();
    }

    public static long tomorrowTS() {
        return (todayTS() + 86400000);
    }

    public static long nextDayTS(long ts) {
        if (ts <= 0)
            return 86400000;
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(ts);
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);

        return (current.getTimeInMillis() + 86400000);
    }

    @SuppressWarnings("deprecation")
    public static int toAge(String s) {
        if (s == null || s.length() <= 0 || "0000-00-00".equals(s))
            return 0;
        try {
            Date date = mBirthdayFormat.parse(s);
            Date now = new Date();

            int age = now.getYear() - date.getYear();
            if (age < 0)
                age = 0;
            return age;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String timeInterval(long tv) {
        long hour = tv / 3600;
        tv %= 3600;

        long min = (tv + 59) / 60;

        if (hour == 0 && min == 0 && tv > 0)
            min = 1;
        String s = "";
        if (hour > 0)
            s += hour + "小时";

        s += min + "分钟";
        return s;
    }

    public static String minutes(int n) {
        return n + "分钟";
    }

    public static String clockInDay(int hour) {
        if (hour <= 5) {
            return ("凌晨" + hour + "点");
        } else if (hour <= 11) {
            return ("上午" + hour + "点");
        } else if (hour == 12) {
            return ("中午" + (hour - 12) + "点");
        } else if (hour <= 18) {
            return ("下午" + (hour - 12) + "点");
        } else
            return ("晚上" + (hour - 12) + "点");
    }

    public static String duration(long tv) {
        long hour = tv / 3600;
        tv %= 3600;
        long min = tv / 60;
        tv %= 60;
        long sec = tv;

        String s = "";
        if (hour > 0)
            s += hour + "时";

        if (hour > 0 || min > 0)
            s += min + "分";

        s += sec + "秒";
        return s;
    }

    public static String toMoodTime(String s) {
        long today = todayTS();
        long yesterday = today - 86400000;

        long ts = toTS(s);
        if (ts > today)
            return TODAY + s.substring(s.length() - 8, s.length() - 3);
        else if (ts > yesterday)
            return YESTERDAY + s.substring(s.length() - 9, s.length() - 3);
        else
            return ((today - ts) / 86400000 + 1) + BEFORE_DAYS;
    }

    public static String getDateFromTime(String time) {
        try {
            Date date = mBirthdayFormat.parse(time);
            return mBirthdayFormat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static long getTSFromDate(String time) {
        try {
            Date date = mBirthdayFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    private static final String TODAY = "今天";
    private static final String YESTERDAY = "昨天";
    private static final String BEFORE_DAYS = "天前";

    public static String timeStamp2FullDate(long seconds) {  //时间戳转换为时间 包含 年月日，时分秒
        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    public static String timeStamp2_Hour_MinitueDate(long seconds) {  //时间戳转换为时间只包含时分
        String format = "HH:mm";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    public static String timeStamp2YMDDate(long seconds) {  //时间戳（秒）转换为时间 只包含 年月日
        String format = "yyyy-MM-dd";
//		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
//			return "";
//		}
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    public static long timeStamp() {  // 精确到秒
        long time = System.currentTimeMillis();

        return time / 1000;
    }


    public static String formatYMD(Date time) {
        return mBirthdayFormat.format(time);
    }


    public static int getWeekIndexByDate(String startTime) { // 根据日期 得出星期 如2016-10-10 等于2
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(startTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(startTime.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, Integer.valueOf(startTime.split("-")[2]));
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    private static SimpleDateFormat mNormalFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    private static SimpleDateFormat mDetailTimeFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String ConvertNormalTimeFromDate(String time) {
        try {
            Date date = mBirthdayFormat.parse(time);
            return mNormalFormat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String ConvertDetailTime2NormalTime(String time) {

        return time.substring(0, time.indexOf(" "));
    }


    public static String detailTimeToHmFormat(String detailTime) { //yyyy-MM-dd HH:mm:ss to HH:mm
        String times = detailTime.split(" ")[1];
        times = times.substring(0, times.lastIndexOf(":"));
        //  System.out.println(" time: " + times);
        return times;
    }


    public static int getDaysOfMonth(String mDates) { // mDates = 2016-10-13
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(mDates.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(mDates.split("-")[1]) - 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public static String MinitueToDetailTime(int index, int min) { //index为需要移动多少天 600 转换为 04:00：00
        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, index);

        // calendar.setTime(date);
        int hour = min / 60;
        int minitue = min % 60;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minitue, 0);
        return sdf.format(calendar.getTime());
    }

    public static String MinitueToDetailTimeByDate(String date, int min) { //date 如2017-01-01格式 600 转换为 04:00：00
        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(date.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(date.split("-")[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.split("-")[2]));
        int hour = min / 60;
        int minitue = min % 60;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minitue, 0);
        return sdf.format(calendar.getTime());
    }


    public static String formatTwoString(int time) {
        String tm = String.valueOf(time);
        if (tm.length() < 2) {
            tm = "0" + tm;
        }
        return tm;
    }


    public static String MinutiToTime(int time) {

        String suffix = "";
        String prefix = "";
        time = time % 1440;
        int tmp = time / 60;
        suffix = String.valueOf(time % 60);
        prefix = String.valueOf(tmp);
        if (suffix.length() < 2) {
            suffix = "0" + suffix;
        }
        if (prefix.length() < 2) {
            prefix = "0" + prefix;
        }

        return String.valueOf(prefix + ":" + suffix);
    }

    public static String MinitueToPrefix(int time) {
        int tmp = time / 60;
        String prefix = String.valueOf(tmp);
        if (prefix.length() < 2) {
            prefix = "0" + prefix;
        }
        return prefix;
    }

    public static String MinitueToSuffix(int time) {
        time = time % 1440;
        String suffix = String.valueOf(time % 60);
        if (suffix.length() < 2) {
            suffix = "0" + suffix;
        }
        return suffix;
    }

    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(true, true, tz.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }
}
