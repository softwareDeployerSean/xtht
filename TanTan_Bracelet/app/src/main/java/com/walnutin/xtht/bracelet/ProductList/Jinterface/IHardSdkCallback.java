package com.walnutin.xtht.bracelet.ProductList.Jinterface;

/**
 * Created by chenliu on 2017/3/13.
 */

public interface IHardSdkCallback {
    void onCallbackResult(int flag, boolean state, Object obj);

    void onStepChanged(int step, float distance, int calories, boolean finish_status);

    void onSleepChanged(int lightTime, int deepTime, int sleepAllTime, int[] sleepStatusArray, int[] timePointArray, int[] duraionTimeArray);

    void onHeartRateChanged(int rate, int status);

    void bloodPressureChange(int hightPressure, int lowPressure,int status);
}
