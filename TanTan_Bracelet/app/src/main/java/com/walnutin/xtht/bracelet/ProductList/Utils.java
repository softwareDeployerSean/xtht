package com.walnutin.xtht.bracelet.ProductList;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 作者：MrJiang on 2017/5/26 13:43
 */
public class Utils {
    private static final String TAG = "Utils";

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static long currentTimeInMillis() {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(false);
    }

    public static boolean isHaveApp(String packageName, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getContactPhoneNumByName(Context context, String name) {
        String contactNames = "";

        //使用ContentResolver查找联系人数据
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //遍历查询结果，找到所需号码
        while (cursor.moveToNext()) {
            //获取联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的名字
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(contactName)) {
                //使用ContentResolver查找联系人的电话号码
                Cursor phone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                if (phone.moveToNext()) {
                    String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Toast.makeText(context, phoneNumber + "", Toast.LENGTH_SHORT).show();
                    //       Log.d(TAG, "电话：" + phoneNumber);
                    return phoneNumber;
                }

            }
        }
        return contactNames;
    }

    public static String getContactNameByNumber(Context context, String phoneNum) {
        //  String number = "110";
        String contactName = "";
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phoneNum);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(0);
            //   Log.i(TAG, name);
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactName;
    }

    public static String getContactNameFromPhoneNum(Context context, String phoneNum) // 根据电话号码得到联系人名字
    {
        return getContactNameByNumber(context, phoneNum);
//        String contactName = "";
//        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
//                ContactsContract.CommonDataKinds.Phone.NUMBER};
//        // 将自己添加到 msPeers 中
//        Cursor cursor = context.getContentResolver().query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                projection, // Which columns to return.
//                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
//                        + phoneNum + "'", // WHERE clause.
//                null, // WHERE clause value substitution
//                null); // Sort order.
//
//        if (cursor == null) {
//            Log.w("phoneNum", "getPeople null");
//            return contactName;
//        }
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            // 取得联系人名字
//            int nameFieldColumnIndex = cursor
//                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//            String name = cursor.getString(nameFieldColumnIndex);
//            return name;
//        }
//        return contactName;
        //    ContentResolver cr = context.getContentResolver();
//        Cursor pCur = cr.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
//                new String[]{ phoneNum }, null);
//
//        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
//                ContactsContract.PhoneLookup.NUMBER};
//        Uri uri = Uri.withAppendedPath(
//                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
//                Uri.encode(phoneNum));
//        Cursor pCur = context.getContentResolver().query(uri, projection,
//                null, null, null);
//
//        if (pCur.moveToFirst()) {
//            contactName = pCur
//                    .getString(pCur
//                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            pCur.close();
//        }
        //return contactName;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public static String getChannel(Context context) {
        String channel = "";
        try {
            channel = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return channel;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qingcheng");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String feetToCm(String values) {

        int inch = Integer.valueOf(values.split("'")[0]);

        String footTmp = values.split("'")[1]; // 10";
        footTmp = footTmp.substring(0, footTmp.indexOf("\""));

        int foot = Integer.valueOf(footTmp);

        int cmValue = (int) Math.round(inch * 30.48 + foot * 2.54);
        return cmValue + "";
    }

    public static String CmToFeet(String values) {
        int a = Integer.valueOf(values);
        int foot = (int) (12 * a / 30.48);
        int inch = foot / 12;  //英尺
        foot = foot % 12;  // 英寸
        String str = inch + "'" + foot + "\"";
        return str;
    }

    public static String poundToKg(String values) {
        int pound = Integer.valueOf(values);
        int kgValue = (int) Math.round(0.4536 * pound);
        return kgValue + "";
    }

    public static String KgToPound(String valueString) {
        int a = Integer.valueOf(valueString);

        int poundValue = (int) Math.round(a * 2.2);
        return poundValue + "";
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}