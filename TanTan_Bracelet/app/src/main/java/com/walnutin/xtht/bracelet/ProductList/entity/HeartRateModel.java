package com.walnutin.xtht.bracelet.ProductList.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：MrJiang on 2016/7/29 16:18
 */
public class HeartRateModel implements Serializable{
    public transient List<Integer> recentRateList = new ArrayList<>();
    public transient List<Integer> realRateList = new ArrayList<>();
    public transient int lowRate;
    public transient int HighRate;
//    public transient int isUpload;
    public String account;
    public String testMomentTime;     // 一次测试结束的时间点
    public int currentRate;           //本次测得的心率值
    public int durationTime;         // 一次测试持续时长
    public boolean isRunning;      // 是否跑步测的
    public Map<Long, Integer> heartTrendMap = new LinkedHashMap<Long, Integer>(); // Key 为时间戳、value是实时心率值

    public List<Integer> getRecentRateList() {
        return recentRateList;
    }

    public void setRecentRateList(List<Integer> recentRateList) {
        this.recentRateList = recentRateList;
    }

    public int getLowRate() {
        return lowRate;
    }

    public void setLowRate(int lowRate) {
        this.lowRate = lowRate;
    }

    public int getHighRate() {
        return HighRate;
    }

    public void setHighRate(int highRate) {
        HighRate = highRate;
    }

    public int getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(int currentRate) {
        this.currentRate = currentRate;
    }

    public List<Integer> getRealRateList() {
        return realRateList;
    }

    public void setRealRateList(List<Integer> realRateList) {
        this.realRateList = realRateList;
    }

    public String getTestMomentTime() {
        return testMomentTime;
    }

    public void setTestMomentTime(String testMomentTime) {
        this.testMomentTime = testMomentTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
