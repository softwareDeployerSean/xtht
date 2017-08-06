package com.walnutin.xtht.bracelet.app.utils;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suns on 2017-06-17.
 */

public class ConmonUtils {
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /*
        * 验证邮箱
        * @param email
        * @return
                */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 利用MD5进行加密
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException     没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5(String str) {

        //确定计算方法
        MessageDigest md5 = null;
        String newstr = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            //加密后的字符串
            newstr = Base64.encodeToString(md5.digest(str.getBytes("utf-8")), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return newstr;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String getYingCun(int a) {
        int yingchi = (int) (a / 12);
        if (a % 12 == 0) {
            return yingchi + "'";
        } else {
            int yingcun = a - yingchi * 12;
            return yingchi + "'" + yingcun + "”";
        }

    }

    public static boolean hasNetwork(Context context) {
        if (DeviceUtils.getNetworkType(MyApplication.getAppContext()) == 0) {//没有网络
            ToastUtils.showToast(context.getString(R.string.no_net), context);
            return false;
        } else {
            return true;
        }
    }

    public static void saveArray(Context context, List<LatLng> list) {
        if (list.size() <= 0) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("Status_size", list.size());
        for (int i = 0; i < list.size(); i++) {
            String json = new Gson().toJson(list.get(i));
            editor.putString("Status_" + i, json);
        }
        editor.commit();

    }

    public static void deleteArray(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    public static List loadArray(Context context) {
        List<LatLng> list = new ArrayList<>();
        list.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            LatLng userinfo = null;
            String gson = sharedPreferences.getString("Status_" + i, null);
            if (!TextUtils.isEmpty(gson)) {
                userinfo = new Gson().fromJson(gson, LatLng.class);
            }
            list.add(userinfo);
        }

        return list;
    }

    /**
     * 这个方法挺简单的。
     * DecimalFormat is a concrete subclass of NumberFormat that formats decimal numbers.
     *
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();


        // 保留两位小数
        nf.setMaximumFractionDigits(2);


        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);


        return nf.format(d);
    }

    public static int updateGpsStatus(int event, GpsStatus status) {
        int count = 0;
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                if (s.getSnr() != 0)//只有信躁比不为0的时候才算搜到了星
                {
                    count++;
                }
//Toast.makeText(mContext, "当前星颗数="+count, Toast.LENGTH_SHORT).show();

            }
        }
        return count;
    }

    public static Boolean initGPS(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

            return false;
        } else {
            return true;
            // 弹出Toast
//          Toast.makeText(TrainDetailsActivity.this, "GPS is ready",
//                  Toast.LENGTH_LONG).show();
//          // 弹出对话框
//          new AlertDialog.Builder(this).setMessage("GPS is ready")
//                  .setPositiveButton("OK", null).show();
        }
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "'" + unitFormat(second) + "''";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "'" + unitFormat(minute) + "''" + unitFormat(second) + "'''";
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String shuzi(Double d) {
        DecimalFormat decimalFormat = new DecimalFormat("##############0.0");
        String tmp = decimalFormat.format(d);
        return tmp;
    }


    public static String getweek_day(String time) {
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        String begin = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String end = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        //所在周开始日期
        LogUtils.debugInfo("开始日期" + begin + "借宿日志" + end);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return begin + "," + end;

    }

    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    // 获取某年的第几周的开始日期
    public static String getFirstDayOfWeek(int year, int week) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    // 获取某年的第几周的结束日期
    public static String getLastDayOfWeek(int year, int week) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计算两天之间的天数
     *
     * @param startStr
     * @param endStr
     * @return
     */
    public static int daysBetween(String startStr, String endStr) {
        int daysBetween = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = sdf.parse(startStr);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date1);

            Date date2 = sdf.parse(endStr);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(date2);

            Calendar date = (Calendar) startDate.clone();

            while (date.before(endDate)) {
                date.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return daysBetween;
    }

    public static String getbeforeday(String date) {
        String before_day = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        try {
            date1 = sdf.parse(date);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date1);

            startDate.add(Calendar.DATE, -1); //得到前一天
            Date date_tmp = startDate.getTime();
            before_day = sdf.format(date_tmp);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return before_day;
    }

    public static String getnextday(String date) {
        String before_day = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        try {
            date1 = sdf.parse(date);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date1);

            startDate.add(Calendar.DATE, 1); //得到前一天
            Date date_tmp = startDate.getTime();
            before_day = sdf.format(date_tmp);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return before_day;
    }

    public static Date getTimesWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周一0点时间
    public static String getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(cal.getTime());
        return time;
    }

    // 获得本周日24点时间
    public static String getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekDate());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(cal.getTime());
        return time;
    }

    /**
     * 根据日期获得所在周的日期
     *
     * @param mdate
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<String> dateToWeek(Date mdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int b = mdate.getDay();
        Date fdate;
        List<String> list = new ArrayList<String>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a - 1, sdf.format(fdate));
        }
        return list;
    }
}
