package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.content.SharedPreferences;

/**

 */
public class DeviceSharedPf {
    private String sp_name = "deviceinfo";
    private SharedPreferences sharedPreferences;
    private static DeviceSharedPf instance;

    public static DeviceSharedPf getInstance(Context context) {

        if (instance == null) {
            instance = new DeviceSharedPf(context);
        }
        return instance;
    }

    private DeviceSharedPf(Context context) {
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

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key,boolean is) {
        return sharedPreferences.getBoolean(key, is);
    }

    public int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }



}
