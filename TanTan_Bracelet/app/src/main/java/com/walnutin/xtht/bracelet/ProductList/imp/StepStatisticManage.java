package com.walnutin.xtht.bracelet.ProductList.imp;

import android.content.Context;


import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：MrJiang
 */
public class StepStatisticManage {

    private Context context;
    StepInfos stepInfos;
    //List<StepInfos> stepInfosList;
    List<Integer> dayHourKey;
    List<Integer> dayHourValue;
    List<Integer> monthDayKey;
    List<Integer> monthDayValue;
    List<Integer> weekDayKey;
    List<Integer> weekDayValue;
    private static StepStatisticManage instance;

    Map<String, StepInfos> mapDayStepInfoList = new HashMap<>();
    Map<String, List> mapMonthStepInfoList = new HashMap<>();
    Map<String, List> mapWeekStepInfoList = new HashMap<>();

    public StepStatisticManage(Context mcontext) {
        this.context = mcontext;

    }

    public static StepStatisticManage getInstance(Context context) {
        if (instance == null) {
            instance = new StepStatisticManage(context);
        }
        return instance;
    }

/*
*
* 统计步数 天 模式
*
*
 *  */

    private static SimpleDateFormat mBirthdayFormat = new SimpleDateFormat(
            "M/dd yyyy");
    private static SimpleDateFormat mNormalFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

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


    public StepInfos getDayModeStepByDate(String date) {

        //   date = ConvertNormalTimeFromDate(date);
        if (mapDayStepInfoList.containsKey(date)) {
            return mapDayStepInfoList.get(date);
        } else {
            StepInfos sif = SqlHelper.instance().getOneDateStep(MyApplication.account, date);
            mapDayStepInfoList.put(date, sif);
            return sif;
        }

        //  return null;
    }

    public StepInfos getToDayModeStep(String date) {

        StepInfos sif = SqlHelper.instance().getOneDateStep(MyApplication.account, date);
        mapDayStepInfoList.put(date, sif);
        return sif;
    }

    public void getDayModeStepListByDate(String startDate, String endDate) {

        startDate = ConvertNormalTimeFromDate(startDate);
        endDate = ConvertNormalTimeFromDate(endDate);
        List<StepInfos> stepInfosList = SqlHelper.instance().getLastDateStep(MyApplication.account, startDate, endDate);

        for (StepInfos stepInfos : stepInfosList) {
            mapDayStepInfoList.put(stepInfos.dates, stepInfos);
        }

    }

    public void resolveStepInfo(StepInfos stepInfos) {  //计算指定日期的步数与时间关系 横纵坐标系
        dayHourKey = new ArrayList<>();
        dayHourValue = new ArrayList<>();
        if (stepInfos.getStepOneHourInfo() == null) {
            return;
        }
        Set<Map.Entry<Integer, Integer>> set = stepInfos.getStepOneHourInfo().entrySet();
        Iterator<Map.Entry<Integer, Integer>> itor = set.iterator();

        while (itor.hasNext()) {
            Map.Entry<Integer, Integer> entry = itor.next();
            //System.out.println(entry.getKey() + " : " + entry.getValue());
            if (entry.getValue() > 0) {
                dayHourKey.add(entry.getKey() / 60);      //key 是保存的分钟数，  得到小时数
                dayHourValue.add(entry.getValue());
            }
        }
    }

    public List<Integer> getOneDayStepKeyDetails() {

        return dayHourKey;
    }

    public List<Integer> getOneDayStepHourValueDetails() {
        return dayHourValue;

    }


    public int getTotalStep(List<StepInfos> stepInfosList) {
        int steps = 0;
        for (StepInfos stepInfos : stepInfosList) {
            steps += stepInfos.getStep();
        }
        return steps;
    }


    public float getTotalDistance(List<StepInfos> stepInfosList) {
        float distance = 0;
        for (StepInfos stepInfos : stepInfosList) {
            distance += stepInfos.getDistance();
        }
        return distance;
    }


    public int getTotalCal(List<StepInfos> stepInfosList) {
        int cals = 0;
        for (StepInfos stepInfos : stepInfosList) {
            cals += stepInfos.getCalories();
        }
        return cals;
    }

    public int getTotalSportTimes(List<StepInfos> stepInfosList) {
        int times = 0;
        for (StepInfos stepInfos : stepInfosList) {
            if (stepInfos.getStepOneHourInfo() != null) {
                times += stepInfos.getStepOneHourInfo().size();
            }
        }
        return times;
    }

/*
*
* 统计步数 周 模式
*
*
 *  */


    public List getWeekModeStepListByDate(String startTime, int position) { //month ="2016-08-08"

        if (mapWeekStepInfoList.containsKey(startTime)) {
            return mapWeekStepInfoList.get(startTime);
        }

        String endTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(startTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(startTime.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, Integer.valueOf(startTime.split("-")[2]));
        calendar.add(Calendar.DATE, 6);
        endTime = TimeUtil.formatYMD(calendar.getTime());

        // month = month.substring(0, month.lastIndexOf("-"));
        List stepInfosList = SqlHelper.instance().getWeekLastDateStep(MyApplication.account, startTime, endTime);
        if (stepInfosList == null) {
            stepInfosList = new ArrayList<>();
        }
        //  System.out.println("stepInfosList:" + stepInfosList.size());
        if (!mapWeekStepInfoList.containsKey(startTime)) {
            mapWeekStepInfoList.put(startTime, stepInfosList);
        }
        return stepInfosList;
    }


    public List getTheWeekModeStepList(String startTime) { //month ="2016-08-08"


        String endTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(startTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(startTime.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, Integer.valueOf(startTime.split("-")[2]));
        calendar.add(Calendar.DATE, 6);
        endTime = TimeUtil.formatYMD(calendar.getTime());

        // month = month.substring(0, month.lastIndexOf("-"));
        List stepInfosList = SqlHelper.instance().getWeekLastDateStep(MyApplication.account, startTime, endTime);
        if (stepInfosList == null) {
            stepInfosList = new ArrayList<>();
        }
        mapWeekStepInfoList.put(startTime, stepInfosList);
        return stepInfosList;
    }


    public void resolveWeekModeStepInfo(List<StepInfos> stepInfoList) {

        weekDayKey = new ArrayList<>();
        weekDayValue = new ArrayList<>();
        for (StepInfos stepInfos : stepInfoList) {
            if (stepInfos.getStep() > 0) {
                weekDayKey.add(TimeUtil.getWeekIndexByDate(stepInfos.getDates())-1); //根据日期计算是周几 的索引 周一为0
                weekDayValue.add(stepInfos.getStep());
            }
        }
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

    public List getMonthModeStepListByDate(String month, int position) { //month ="2016-08-08"

        month = month.substring(0, month.lastIndexOf("-"));

        if (mapMonthStepInfoList.containsKey(month)) {
            return mapMonthStepInfoList.get(month);
        }

        List stepInfosList = SqlHelper.instance().getMonthStepListByMonth(MyApplication.account, month);
        if (stepInfosList == null) {
            stepInfosList = new ArrayList<>();
        }
        //  System.out.println("stepInfosList:" + stepInfosList.size());
        if (!mapMonthStepInfoList.containsKey(month)) {
            mapMonthStepInfoList.put(month, stepInfosList);
        }
        return stepInfosList;
    }

    public List getTheMonthModeStepListBy(String month) { //month ="2016-08-08"

        month = month.substring(0, month.lastIndexOf("-"));


        List stepInfosList = SqlHelper.instance().getMonthStepListByMonth(MyApplication.account, month);
        if (stepInfosList == null) {
            stepInfosList = new ArrayList<>();
        }
        mapMonthStepInfoList.put(month, stepInfosList);
        return stepInfosList;
    }


    public void resolveMonthModeStepInfo(List<StepInfos> stepInfoList) {

        monthDayKey = new ArrayList<>();
        monthDayValue = new ArrayList<>();
        for (StepInfos stepInfos : stepInfoList) {
            if (stepInfos.getStep() > 0) {
                monthDayKey.add(Integer.valueOf(stepInfos.getDates().split("-")[2]) - 1);
                monthDayValue.add(stepInfos.getStep());
            }
        }
    }

    public List<Integer> getMonthDayKey() {
        return monthDayKey;
    }

    public List<Integer> getMonthDayValue() {
        return monthDayValue;
    }
}
