/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 */
public class DeviceHomeDataSp {
    SharedPreferences mSettings;
    private static DeviceHomeDataSp pref = null;

    public static DeviceHomeDataSp getInstance(Context context) {
        if (pref == null) {
            pref = new DeviceHomeDataSp(context);

        }
        return pref;
    }

    private DeviceHomeDataSp(Context context) {
        mSettings = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }

    public boolean isMetric() {
        return true;
    }

    public float getStepLength() {
        try {
            return Float.valueOf(mSettings.getString("step_length", "50").trim()); // 步长
        } catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }

    public float getBodyWeight() {
        try {
            return Float.valueOf(mSettings.getString("body_weight", "50").trim()); //50千克
        } catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }


    public void setLong(String key, long defalutValue) {
        mSettings.edit().putLong(key, defalutValue).commit();
    }

    public void setString(String key, String defalutValue) {
        mSettings.edit().putString(key, defalutValue).commit();
    }

    public boolean isRunning() {
        return mSettings.getString("exercise_type", "walk").equals("running");
    }


    public String getString(String key, String DefalutValue) {
        return mSettings.getString(key, DefalutValue); // steps/minute
    }

    public int getInt(String key, int DefalutValue) {
        return mSettings.getInt(key, DefalutValue); // steps/minute
    }

    public long getLong(String key, long DefalutValue) {
        return mSettings.getLong(key, DefalutValue); // steps/minute
    }


    public float getFloat(String key, float DefalutValue) {
        return mSettings.getFloat(key, DefalutValue); // steps/minute
    }


    public void setDistance(Float distance) {
        mSettings.edit().putFloat("distance", distance).commit();
    }

    public void setSteps(int step) {
        mSettings.edit().putInt("steps", step).commit();
    }

    public void setStepGoal(int step) {
        mSettings.edit().putInt("goals", step).commit();
    }

    public void setCalories(int calories) {
        mSettings.edit().putInt("calories", calories).commit();
    }

    public void setMinitues(int minitues) {
        mSettings.edit().putInt("minitues", minitues).commit();
    }


    public boolean wakeAggressively() {
        return mSettings.getString("operation_level", "run_in_background").equals("wake_up");
    }

    public boolean keepScreenOn() {
        return mSettings.getString("operation_level", "run_in_background").equals("keep_screen_on");
    }

    //
    // Internal

    public void saveServiceRunningWithTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", Utils.currentTimeInMillis());
        editor.commit();
    }


    public void saveTimestamp() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong("last_seen", System.currentTimeMillis());
        editor.commit();
    }

    public long getLast_Seen() {
        return mSettings.getLong("last_seen", 0); //
    }


}
