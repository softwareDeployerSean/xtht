package com.walnutin.xtht.bracelet.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.jess.arms.utils.DeviceUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suns on 2017-06-17.
 */

public class ConmonUtils {
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /*
        * 验证邮箱
        * @param email
        * @return
                */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 利用MD5进行加密
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException     没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5(String str) {

        //确定计算方法
        MessageDigest md5 = null;
        String newstr = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            //加密后的字符串
            newstr = Base64.encodeToString(md5.digest(str.getBytes("utf-8")), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return newstr;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String getYingCun(int a) {
        int yingchi = (int) (a / 12);
        if (a % 12 == 0) {
            return yingchi + "'";
        } else {
            int yingcun = a - yingchi * 12;
            return yingchi + "'" + yingcun + "”";
        }

    }

    public static boolean hasNetwork(Context context) {
        if (DeviceUtils.getNetworkType(MyApplication.getAppContext()) == 0) {//没有网络
            ToastUtils.showToast(context.getString(R.string.no_net), context);
            return false;
        } else {
            return true;
        }
    }

    public static void saveArray(Context context, List<LatLng> list) {
        if (list.size() <= 0) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("Status_size", list.size());
        for (int i = 0; i < list.size(); i++) {
            String json = new Gson().toJson(list.get(i));
            editor.putString("Status_" + i, json);
        }
        editor.commit();

    }

    public static void deleteArray(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    public static List loadArray(Context context) {
        List<LatLng> list = new ArrayList<>();
        list.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            LatLng userinfo = null;
            String gson = sharedPreferences.getString("Status_" + i, null);
            if (!TextUtils.isEmpty(gson)) {
                userinfo = new Gson().fromJson(gson, LatLng.class);
            }
            list.add(userinfo);
        }

        return list;
    }

}
