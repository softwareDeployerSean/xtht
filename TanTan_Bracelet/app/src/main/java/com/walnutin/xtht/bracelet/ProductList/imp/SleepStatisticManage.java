package com.walnutin.xtht.bracelet.ProductList.imp;

import android.content.Context;


import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
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
public class SleepStatisticManage {

    private Context context;

    List<Integer> monthDayKey;
    List<Integer> monthTotalSleepDayValue;
    List<Integer> monthDeepSleepDayValue;
    List<Integer> monthLightSleepDayValue;
    List<Integer> monthsoberSleepDayValue;

    List<Integer> weekDayKey;
    List<Integer> weekDeepSleepDayValue;
    List<Integer> weekLightSleepDayValue;
    List<Integer> weeksoberSleepDayValue;
    List<Integer> weekTotalSleepDayValue;
    private static SleepStatisticManage instance;
    private Map<String, SleepModel> mapSleepMode = new HashMap<>();
    Map<String, List> mapMonthSleepInfoList = new HashMap<>();
    Map<String, List> mapWeekSleepInfoList = new HashMap<>();
    public SleepModelImpl sleepModelImpl;

    public SleepStatisticManage(Context mcontext) {
        this.context = mcontext;

        sleepModelImpl = new SleepModelImpl(context);
    }

    public static SleepStatisticManage getInstance(Context context) {
        if (instance == null) {
            instance = new SleepStatisticManage(context);
        }
        return instance;
    }

/*
*
* 统计睡眠天 模式
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


    public SleepModel getDayModeSleepByDate(String date) {

        //   date = ConvertNormalTimeFromDate(date);
        if (mapSleepMode.containsKey(date)) {
            return mapSleepMode.get(date);
        } else {
            SleepModel sm = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, date);
            mapSleepMode.put(date, sm);
            return sm;
        }


    }

    public SleepModel getToDayModeSleep(String date) {

        SleepModel sm = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, date);
        mapSleepMode.put(date, sm);
        return sm;
    }

    public void generateVirtualData() {
        sleepModelImpl.testInsertSleepData();
    }

    public void getDayModeSleepListByDate(String startDate, String endDate) {
        startDate = ConvertNormalTimeFromDate(startDate);
        endDate = ConvertNormalTimeFromDate(endDate);
        List<SleepModel> sleepList = SqlHelper.instance().getSleepListByTime(MyApplication.account, startDate, endDate);

        for (SleepModel sleepMode : sleepList) {
            mapSleepMode.put(sleepMode.date, sleepMode);
        }

    }

    public void setSleepMode(SleepModel sleepMode) {

        sleepModelImpl.setSleepModel(sleepMode);
    }

    public void setTimePointArray(int[] timePointArray) {
        sleepModelImpl.setTimePointArray(timePointArray);
    }

    public void setStartSleep() {
        sleepModelImpl.setStartSleep();
    }

    public void setEndSleep() {
        sleepModelImpl.setEndSleep();
    }


    public int getDurationLen() {
        return sleepModelImpl.getAllDurationTime();
    }

    public int getLightTime() {
        return sleepModelImpl.getLightTime();
    }

    public int getSoberTime() {
        return sleepModelImpl.getSoberTime();
    }

    public int getDeepTime() {
        return sleepModelImpl.getDeepTime();
    }

    public String getStartSleep() {
        return sleepModelImpl.getStartSleep();
    }

    public String getEndSleep() {
        return sleepModelImpl.getEndSleep();
    }


    public int getTotalTime() {
        return sleepModelImpl.getTotalTime();
    }

    public int getSleepScore() {

        return sleepModelImpl.getSleepScore();
    }

    public int[] getSleepStatusArray() {
        return sleepModelImpl.getSleepStatusArray();
    }

    public List<Integer> getDurationStartPos() {
        return sleepModelImpl.getDurationStartPos();
    }

    public int[] getTimePointArray() {
        return sleepModelImpl.getTimePointArray();
    }

    public int[] getDuraionTimeArray() {
        return sleepModelImpl.getDuraionTimeArray();
    }



/*
*
* 统计步数 周 模式
*
*
 *  */


    public List getWeekModeSleepListByDate(String startTime, int position) { //startTime ="2016-08-08"

        if (mapWeekSleepInfoList.containsKey(startTime)) {
            return mapWeekSleepInfoList.get(startTime);
        }

        String endTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(startTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(startTime.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, Integer.valueOf(startTime.split("-")[2]));
        calendar.add(Calendar.DATE, 6);
        endTime = TimeUtil.formatYMD(calendar.getTime());

        List sleepList = SqlHelper.instance().getWeekSleepListByTime(MyApplication.account, startTime, endTime);
        if (sleepList == null) {
            sleepList = new ArrayList<>();
        }
        //  System.out.println("stepInfosList:" + stepInfosList.size());
        if (!mapWeekSleepInfoList.containsKey(startTime)) {
            mapWeekSleepInfoList.put(startTime, sleepList);
        }
        return sleepList;
    }


    public void resolveWeekModeSleepInfo(List<SleepModel> sleepModelList) {

        weekDayKey = new ArrayList<>();
        weekTotalSleepDayValue = new ArrayList<>();
        weekDeepSleepDayValue = new ArrayList<>();
        weekLightSleepDayValue = new ArrayList<>();
        weeksoberSleepDayValue = new ArrayList<>();
        for (SleepModel sleepModel : sleepModelList) {
            if (sleepModel.getTotalTime() > 1) {
                weekDayKey.add(TimeUtil.getWeekIndexByDate(sleepModel.date) - 1); //根据日期计算是周几 的索引 周一为1
                weekTotalSleepDayValue.add(sleepModel.getTotalTime());
                weekDeepSleepDayValue.add(sleepModel.getDeepTime());
                weekLightSleepDayValue.add(sleepModel.getLightTime());
                weeksoberSleepDayValue.add(sleepModel.getSoberTime());

            }
        }
    }

    public List<Integer> getWeekDayKey() {
        return weekDayKey;
    }

    public List<Integer> getWeekTotalSleepDayValue() {
        return weekTotalSleepDayValue;
    }

/*
*
* 统计步数 月 模式
*
*
 *  */

    public List getTheMonthModeStepList(String month) { //month ="2016-08-08"

        month = month.substring(0, month.lastIndexOf("-"));


        List stepInfosList = SqlHelper.instance().getMonthSleepListByMonth(MyApplication.account, month);
        if (stepInfosList == null) {
            stepInfosList = new ArrayList<>();
        }
        mapMonthSleepInfoList.put(month, stepInfosList);
        return stepInfosList;
    }

    public List getMonthModeStepListByDate(String month, int position) { //month ="2016-08-08"

        month = month.substring(0, month.lastIndexOf("-"));

        if (mapMonthSleepInfoList.containsKey(month)) {
            return mapMonthSleepInfoList.get(month);
        }

        List stepInfosList = SqlHelper.instance().getMonthSleepListByMonth(MyApplication.account, month);
        if (stepInfosList == null) {
            stepInfosList = new ArrayList<>();
        }
        //  System.out.println("stepInfosList:" + stepInfosList.size());
        if (!mapMonthSleepInfoList.containsKey(month)) {
            mapMonthSleepInfoList.put(month, stepInfosList);
        }
        return stepInfosList;
    }


    public void resolveMonthModeStepInfo(List<SleepModel> sleepModelList) {

        monthDayKey = new ArrayList<>();
        monthTotalSleepDayValue = new ArrayList<>();
        monthDeepSleepDayValue = new ArrayList<>();
        monthLightSleepDayValue = new ArrayList<>();
        monthsoberSleepDayValue = new ArrayList<>();
        for (SleepModel sleepModel : sleepModelList) {
            if (sleepModel.getTotalTime() > 1) {
                monthDayKey.add(Integer.valueOf(sleepModel.date.split("-")[2]) - 1);
                monthTotalSleepDayValue.add(sleepModel.getTotalTime());
                monthDeepSleepDayValue.add(sleepModel.getDeepTime());
                monthLightSleepDayValue.add(sleepModel.getLightTime());
                monthsoberSleepDayValue.add(sleepModel.soberTime);
            }
        }
    }

    public int getPerSleepTotalTime(List<SleepModel> sleepModelList) { // 平均总时间
        int totalTime = 0;
        int index = 0;
        for (SleepModel sleepModel : sleepModelList) {
            if (sleepModel.totalTime > 0) {
                totalTime += sleepModel.totalTime;
                index++;
            }
        }
        if (index != 0) {
            totalTime = totalTime / index;
        }
        return totalTime;
    }


    public int getPerSleepDeepTime(List<SleepModel> sleepModelList) { // 平均深睡眠
        int deepTime = 0;
        int index = 0;
        for (SleepModel sleepModel : sleepModelList) {
            if (sleepModel.deepTime > 0) {
                deepTime += sleepModel.deepTime;
                index++;
            }
        }
        if (index != 0) {
            deepTime = deepTime / index;
        }
        return deepTime;
    }


    public int getPerSleepLightTime(List<SleepModel> sleepModelList) { // 平均浅睡眠
        int lightTime = 0;
        int index = 0;
        for (SleepModel sleepModel : sleepModelList) {
            if (sleepModel.lightTime > 0) {
                lightTime += sleepModel.lightTime;
                index++;
            }
        }
        if (index != 0) {
            lightTime = lightTime / index;
        }
        return lightTime;
    }


    public int getPerSleepSoberTime(List<SleepModel> sleepModelList) { // 平均清醒
        int soberTime = 0;
        int index = 0;
        for (SleepModel sleepModel : sleepModelList) {
            soberTime += sleepModel.soberTime;
            index++;
        }
        if (index != 0) {
            soberTime = soberTime / index;
        }
        return soberTime;
    }


    public List<Integer> getMonthDayKey() {
        return monthDayKey;
    }

    public List<Integer> getMonthTotalSleepDayValue() {
        return monthTotalSleepDayValue;
    }

    public List<SleepModel> getTheWeekModeSleepList(String startTime) {
        String endTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(startTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(startTime.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, Integer.valueOf(startTime.split("-")[2]));
        calendar.add(Calendar.DATE, 6);
        endTime = TimeUtil.formatYMD(calendar.getTime());
        List sleepList = SqlHelper.instance().getWeekSleepListByTime(MyApplication.account, startTime, endTime);
        if (sleepList == null) {
            sleepList = new ArrayList<>();
        }
        mapWeekSleepInfoList.put(startTime, sleepList);
        return sleepList;
    }

    public List<Integer> getMonthDeepSleepDayValue() {
        return monthDeepSleepDayValue;
    }

    public List<Integer> getMonthLightSleepDayValue() {
        return monthLightSleepDayValue;
    }

    public List<Integer> getMonthsoberSleepDayValue() {
        return monthsoberSleepDayValue;
    }

    public List<Integer> getWeekDeepSleepDayValue() {
        return weekDeepSleepDayValue;
    }

    public List<Integer> getWeekLightSleepDayValue() {
        return weekLightSleepDayValue;
    }

    public List<Integer> getWeeksoberSleepDayValue() {
        return weeksoberSleepDayValue;
    }
}
