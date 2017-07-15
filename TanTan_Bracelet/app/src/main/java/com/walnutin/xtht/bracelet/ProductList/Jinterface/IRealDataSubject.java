package com.walnutin.xtht.bracelet.ProductList.Jinterface;

/**
 * 作者：MrJiang on 2016/9/27 15:45
 */
public interface IRealDataSubject {
    void stepChanged(int step, float distance, int calories, boolean finish_status);

    void sleepChanged(int lightTime, int deepTime, int sleepAllTime, int[] sleepStatusArray, int[] timePointArray, int[] duraionTimeArray);

    void heartRateChanged(int rate, int status);

    void bloodPressureChange(int hightPressure, int lowPressure,int status);
}
