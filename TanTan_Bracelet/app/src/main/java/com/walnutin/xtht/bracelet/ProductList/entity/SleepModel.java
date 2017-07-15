package com.walnutin.xtht.bracelet.ProductList.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MrJiang
 */
public class SleepModel implements Serializable {

    public transient List<Integer> durationStartPos = new ArrayList<>(); // �
    public transient String startSleep;
    public transient String endSleep;
    public transient int allDurationTime;  ///睡眠总时长
    public String account;
    public String date ;//="2015-10-11"
    public int[] duraionTimeArray;  // 每种睡眠状态持续了多长时间={30,15,45,30,30,30,45,30,30,15,15,15,45,15,15}
    public int[] timePointArray ;    // 每一种状态结束的时间点（分钟数）={1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388}
    public int[] sleepStatusArray ;  // 睡眠状态 2,1,0表示清醒、深睡、浅睡={1,0,1,0,2,0,1,2,1,0,1,0,1,1,2}
    public int lightTime ;         // 浅度睡眠 时长 =225
    public int deepTime;          // 深度睡眠时长 =135
    public int soberNum ;           // 清醒次数
    public int soberTime;           // 清醒时长
    public int totalTime ;          //睡眠总时长 =390
    public int sleepStatus;

    public int[] getDuraionTimeArray() {
        return duraionTimeArray;
    }

    public void setDuraionTimeArray(int[] duraionTimeArray) {
        this.duraionTimeArray = duraionTimeArray;
    }

    public int[] getTimePointArray() {
        return timePointArray;
    }

    public void setTimePointArray(int[] timePointArray) {
        this.timePointArray = timePointArray;
    }

    public int[] getSleepStatusArray() {
        return sleepStatusArray;
    }

    public void setSleepStatusArray(int[] sleepStatusArray) {
        this.sleepStatusArray = sleepStatusArray;
    }

    public List<Integer> getDurationStartPos() {
        return durationStartPos;
    }

    public void setDurationStartPos(List<Integer> durationStartPos) {
        this.durationStartPos = durationStartPos;
    }

    public String getStartSleep() {
        return startSleep;
    }

    public void setStartSleep(String startSleep) {
        this.startSleep = startSleep;
    }

    public String getEndSleep() {
        return endSleep;
    }

    public void setEndSleep(String endSleep) {
        this.endSleep = endSleep;
    }

    public int getAllDurationTime() {
        return allDurationTime;
    }

    public void setAllDurationTime(int allDurationTime) {
        this.allDurationTime = allDurationTime;
    }

    public int getLightTime() {
        return lightTime;
    }

    public void setLightTime(int lightTime) {
        this.lightTime = lightTime;
    }

    public int getDeepTime() {
        return deepTime;
    }

    public void setDeepTime(int deepTime) {
        this.deepTime = deepTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getSleepStatus() {
        return sleepStatus;
    }

    public void setSleepStatus(int sleepStatus) {
        this.sleepStatus = sleepStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSoberNum() {
        return soberNum;
    }

    public void setSoberNum(int soberNum) {
        this.soberNum = soberNum;
    }

    public int getSoberTime() {
        return soberTime;
    }

    public void setSoberTime(int soberTime) {
        this.soberTime = soberTime;
    }
}
