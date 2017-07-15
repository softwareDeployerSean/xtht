package com.walnutin.xtht.bracelet.ProductList.imp;

import android.content.Context;


import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.HeartRateModel;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：MrJiang
 */
public class HeartRateStatisticManage {

    private Context context;
    List<HeartRateModel> heartRateModelList;
    List<Integer> dayHourKey;
    List<Integer> dayHourValue;
    List<Integer> monthDayKey;
    List<Integer> monthDayValue;
    List<Integer> weekDayKey;
    List<Integer> weekDayValue;
    private static HeartRateStatisticManage instance;

    Map<String, List> mapDayHeartRateInfoList = new HashMap<>();
    Map<String, List> mapMonthHeartRateInfoList = new HashMap<>();
    Map<String, List> mapWeekHeartRateInfoList = new HashMap<>();

    public HeartRateStatisticManage(Context mcontext) {
        this.context = mcontext;

    }

    public static HeartRateStatisticManage getInstance(Context context) {
        if (instance == null) {
            instance = new HeartRateStatisticManage(context);
        }
        return instance;
    }

/*
*
* 统计心率天 模式
*
*
 *  */

    private static SimpleDateFormat mBirthdayFormat = new SimpleDateFormat(
            "M/dd yyyy");
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
        try {
            Date date = mDetailTimeFormat.parse(time);
            return mNormalFormat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static String toConvert(String date) { //"9/20 2016"转变成 2016-9-26格式
        String year = date.split(" ")[1].trim();
        String month = date.split(" ")[0].split("/")[0].trim();
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = date.split(" ")[0].split("/")[1].trim();
        date = year + "-" + month + "-" + day;
        return date;


    }

    public void setHeartRateModelList(List<HeartRateModel> heartRateModelList) {
        this.heartRateModelList = heartRateModelList;
    }


    public void getDayModeHeartRateListByDate(String startDate, String endDate) {

        startDate = ConvertNormalTimeFromDate(startDate);
        endDate = ConvertNormalTimeFromDate(endDate);
        List<HeartRateModel> heartRateModelList = SqlHelper.instance().getHeartRateListByTime(MyApplication.account, startDate, endDate);

//        for (HeartRateModel heartRateModel : heartRateModelList) {
//            mapDayHeartRateInfoList.put(heartRateModel.getTestMomentTime(), heartRateModel);
//        }

    }


    public List<HeartRateModel> getDayModeHeartRateListByDate(String startDate) {  //startData格式为9/11 2-16 得到指定某一天心率集合

        startDate = ConvertNormalTimeFromDate(startDate); // 详细时间转换为 yyyy-MM-dd 格式
        if (mapDayHeartRateInfoList.containsKey(startDate)) {
            return mapDayHeartRateInfoList.get(startDate);
        }

        heartRateModelList = SqlHelper.instance().getOneDayHeartRateInfo(MyApplication.account, startDate);

        mapDayHeartRateInfoList.put(startDate, heartRateModelList);

        return heartRateModelList;

    }


    public List<HeartRateModel> getDayModeHeartRateListByNormalDate(String normalDate) {  //得到指定某一天心率集合

        if (!normalDate.equals(TimeUtil.getCurrentDate())) {
            if (mapDayHeartRateInfoList.containsKey(normalDate)) {
                return mapDayHeartRateInfoList.get(normalDate);
            }
        }

        heartRateModelList = SqlHelper.instance().getOneDayHeartRateInfo(MyApplication.account, normalDate);

        mapDayHeartRateInfoList.put(normalDate, heartRateModelList);

        return heartRateModelList;

    }

    public int calcLowHeartRateValue(List<HeartRateModel> heartRateModelList) {
        int lowRate = 10000;
        for (HeartRateModel heartRateMode : heartRateModelList) {
            if (heartRateMode.currentRate < lowRate) {
                lowRate = heartRateMode.currentRate; // 计算出最低心率值
            }
        }
        if (lowRate == 10000) {
            lowRate = 0;
        }
        return lowRate;
    }


    public int calcCenterHeartRateValue(List<HeartRateModel> heartRateModelList) {
        int centerRate = 0;
        int sum = 0;
        int itemCount = 0;
        for (HeartRateModel heartRateMode : heartRateModelList) {
            sum += heartRateMode.currentRate;
            itemCount++;
        }

        if (itemCount != 0) {
            centerRate = sum / itemCount;
        }
        return centerRate;
    }


    public int calcHighHeartRateValue(List<HeartRateModel> heartRateModelList) {
        int highRate = 0;
        for (HeartRateModel heartRateMode : heartRateModelList) {
            if (heartRateMode.currentRate > highRate) {
                highRate = heartRateMode.currentRate; // 计算出最大心率值
            }
        }
        if (highRate == 0) {
            highRate = 0;
        }
        return highRate;

    }


    public void calcPerHourCenterHeartRate(List<HeartRateModel> heartRateModeList) { // 计算一天 每个小时的 平均值映射
        dayHourKey = new ArrayList<>();
        dayHourValue = new ArrayList<>();

        int len = 0;
        if (heartRateModeList != null) {
            len = heartRateModeList.size();
        } else {
            return;
        }

        for (int i = 0; i < 24; i++) {
            List<HeartRateModel> hourRateList = getHourHeartRate(i, heartRateModeList);
            if (hourRateList.size() > 0) {
                dayHourKey.add(i);
                dayHourValue.add(calcCenterHeartRateValue(hourRateList)); // 得到这个小时的平均值
            }

        }


    }


    public List getHourHeartRate(int hour, List<HeartRateModel> heartRateModeList) {   // 得到指定几小时的心率集合

        List<HeartRateModel> heartRateModeHourList = new ArrayList<>();

        for (HeartRateModel heartRateMode : heartRateModeList) {
            if (heartRateMode.getCurrentRate() > 0) {
                String time = TimeUtil.detailTimeToHmFormat(heartRateMode.getTestMomentTime()); // 得到 时和分
                int hourValue = Integer.parseInt(time.split(":")[0]);
                if (hour == hourValue) {
                    heartRateModeHourList.add(heartRateMode);         // 一个小时内的测得所有心率数据
                }
            }
        }
        return heartRateModeHourList;

    }


    public List<Integer> getOneDayHeartRateKeyDetails() {

        return dayHourKey;
    }

    public List<Integer> getOneDayHeartRateValueDetails() {
        return dayHourValue;

    }


/*
*
* 统计步数 周 模式
*
*
 *  */


    public List getWeekModeStepListByDate(String startTime, int position) { //startTime ="2016-08-08"

        if (mapWeekHeartRateInfoList.containsKey(startTime)) {
            return mapWeekHeartRateInfoList.get(startTime);
        }

        String endTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(startTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(startTime.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, Integer.valueOf(startTime.split("-")[2]));
        calendar.add(Calendar.DATE, 6);
        endTime = TimeUtil.formatYMD(calendar.getTime());


        List heartRateModelList = SqlHelper.instance().getHeartRateListByTime(MyApplication.account, startTime, endTime);
        if (!mapWeekHeartRateInfoList.containsKey(startTime)) {
            mapWeekHeartRateInfoList.put(startTime, heartRateModelList);
        }
        return heartRateModelList;
    }


    public void calcPerDayCenterHeartRate(List<HeartRateModel> heartRateModeList) { // 计算每一天 的心率平均值映射
        weekDayKey = new ArrayList<>();
        weekDayValue = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            List<HeartRateModel> hourRateList = getDayHeartRate(i, heartRateModeList);
            if (hourRateList.size() > 0) {
                weekDayKey.add(i);
                weekDayValue.add(calcCenterHeartRateValue(hourRateList)); // 得到这个小时的平均值
            }

        }


    }


    public List getDayHeartRate(int week, List<HeartRateModel> heartRateModeList) {   // 得到指定一天的心率集合

        List<HeartRateModel> heartRateModeHourList = new ArrayList<>();

        for (HeartRateModel heartRateMode : heartRateModeList) {
            String time = TimeUtil.ConvertDetailTime2NormalTime(heartRateMode.getTestMomentTime()); // 得到 yyyy-MM-dd
            int weekOfDay = TimeUtil.getWeekIndexByDate(time) - 1;

            if (week == weekOfDay) {
                heartRateModeHourList.add(heartRateMode);         // 一天内测得的所有心率数据
            }
        }
        return heartRateModeHourList;

    }


    public List<Integer> getWeekDayKey() {
        return weekDayKey;
    }

    public List<Integer> getWeekDayValue() {
        return weekDayValue;
    }

/*
*
* 统计步数 月 模式
*
*
 *  */

    public List getMonthModeHeartRateListByDate(String month, int position) { //month ="2016-08-08"

        month = month.substring(0, month.lastIndexOf("-"));

        if (mapMonthHeartRateInfoList.containsKey(month)) {
            return mapMonthHeartRateInfoList.get(month);
        }
        List heartRateModelList = SqlHelper.instance().getMonthHeartRateListByTime(MyApplication.account, month);
        if (!mapMonthHeartRateInfoList.containsKey(month)) {
            mapMonthHeartRateInfoList.put(month, heartRateModelList);
        }
        return heartRateModelList;
    }


    public void calcMonthPerDayCenterHeartRate(List<HeartRateModel> heartRateModeList, String date) { // 计算每一天 的心率平均值映射

        monthDayKey = new ArrayList<>();
        monthDayValue = new ArrayList<>();

        int mondayNum = TimeUtil.getDaysOfMonth(date);

        for (int i = 0; i < mondayNum; i++) {
            List<HeartRateModel> hourRateList = getMonthDayHeartRate(i, heartRateModeList);
            if (hourRateList.size() > 0) {
                monthDayKey.add(i);
                monthDayValue.add(calcCenterHeartRateValue(hourRateList)); // 得到这个小时的平均值
            }

        }


    }


    public List getMonthDayHeartRate(int dayOfMonth, List<HeartRateModel> heartRateModeList) {   // 得到指定一天的


        List<HeartRateModel> heartRateModeHourList = new ArrayList<>();

        for (HeartRateModel heartRateMode : heartRateModeList) {
            String time = TimeUtil.ConvertDetailTime2NormalTime(heartRateMode.getTestMomentTime()); // 得到 yyyy-MM-dd
            int monthOfDay = Integer.valueOf(time.split("-")[2]) - 1;
            if (dayOfMonth == monthOfDay) {
                heartRateModeHourList.add(heartRateMode);         // 一天内测得的所有心率数据
            }
        }
        return heartRateModeHourList;

    }


    public List<Integer> getMonthDayKey() {
        return monthDayKey;
    }

    public List<Integer> getMonthDayValue() {
        return monthDayValue;
    }
}
