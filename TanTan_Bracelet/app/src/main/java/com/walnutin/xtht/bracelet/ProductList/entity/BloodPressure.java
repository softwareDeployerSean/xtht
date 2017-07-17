package com.walnutin.xtht.bracelet.ProductList.entity;


import java.io.Serializable;

/**
 * 作者：MrJiang on 2017/6/3 14:42
 */

public class BloodPressure implements Serializable {
    public String account ;
    public int diastolicPressure; //舒张压  120~160
    public int systolicPressure; //收缩压    80~120
    public int durationTime;         // 一次测试持续时长
    public String testMomentTime;     // 一次测试结束的时间点

    final static long serialVersionUID = 1;

    public BloodPressure(String account, int diastolicPressure,
                         int systolicPressure, int durationTime, String testMomentTime) {
        this.account = account;
        this.diastolicPressure = diastolicPressure;
        this.systolicPressure = systolicPressure;
        this.durationTime = durationTime;
        this.testMomentTime = testMomentTime;
    }

    public BloodPressure() {
    }

    public int getDiastolicPressure() {
        return this.diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getSystolicPressure() {
        return this.systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDurationTime() {
        return this.durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public String getTestMomentTime() {
        return this.testMomentTime;
    }

    public void setTestMomentTime(String testMomentTime) {
        this.testMomentTime = testMomentTime;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
