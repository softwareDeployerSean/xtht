package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.content.SharedPreferences;

/**

 */
public class SleepSharedPf {
    private String sp_name = "sleepinfo";
    private SharedPreferences sharedPreferences;
    private static SleepSharedPf instance;

    public static SleepSharedPf getInstance(Context context) {

        if (instance == null) {
            instance = new SleepSharedPf(context);
        }
        return instance;
    }

    private SleepSharedPf(Context context) {
        sharedPreferences = context.getSharedPreferences(sp_name,
                Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public String getString(String key, String defalut) {
        return sharedPreferences.getString(key, defalut);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }


    public int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setSleepClockDuration(int value) { // 闹钟定时长
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("sleepClockDuration", value); // 相差进度
        editor.commit();
    }

    public int getSleepClockDuration(int defaultValue) {
        return sharedPreferences.getInt("sleepClockDuration", defaultValue);

    }

    public void setStartClockProgress(int progress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("startSleepClock", progress); // 相差进度
        editor.commit();
    }

    public void setAwakeClockProgress(int progress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("wakeSleepClock", progress); // 相差进度
        editor.commit();
    }

    public int getStartClockProgress(int progress) {
        return sharedPreferences.getInt("startSleepClock", progress);
    }

    public int getAwakeClockProgress(int progress) {
        return sharedPreferences.getInt("wakeSleepClock", progress);

    }

    public void setClockOpen(boolean isopen) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sleep_clock", isopen);
        editor.commit();
    }

    public boolean getClockOpenStatus() {
        return sharedPreferences.getBoolean("sleep_clock", false);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setStartSleep(String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TimeUtil.getCurrentDate() + "_startSleep", time);
        editor.commit();
    }

    public String getStartSleep(String date, String time) {
        return sharedPreferences.getString(date + "_startSleep", time);

    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    public void setSleepGoal(int hour) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("target_sleep", hour);
        editor.commit();
    }


    public void deleteCache() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();

    }

}
