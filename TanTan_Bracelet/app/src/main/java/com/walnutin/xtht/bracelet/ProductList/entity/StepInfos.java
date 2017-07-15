package com.walnutin.xtht.bracelet.ProductList.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * 作者：MrJiang
 * caro
 * sid uid calories 字段目前无用
 */
public class StepInfos implements Serializable{
    public  int stepGoal;
    public Map<Integer, Integer> stepOneHourInfo;
    public String dates;//日期
    public int step;//statistic_step
    public int calories;
    private Integer weekOfYear;
    public float distance;//距离
    protected String account;//账户
    private transient int isUpLoad;

   /* private int target;*/


    public int isUpLoad() {
        return isUpLoad;
    }

    public void setUpLoad(int upLoad) {
        isUpLoad = upLoad;
    }


    public int getIsUpLoad() {
        return isUpLoad;
    }

    public void setIsUpLoad(int isUpLoad) {
        this.isUpLoad = isUpLoad;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Map<Integer, Integer> getStepOneHourInfo() {
        return stepOneHourInfo;
    }

    public void setStepOneHourInfo(Map<Integer, Integer> stepOneHourInfo) {
        this.stepOneHourInfo = stepOneHourInfo;
    }

    public Integer getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(Integer stepGoal) {
        this.stepGoal = stepGoal;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
