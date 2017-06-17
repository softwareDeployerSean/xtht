package com.walnutin.xtht.bracelet.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.walnutin.xtht.bracelet.mvp.model.entity.CropOption;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.CropOptionAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by john on 2016/9/23.
 *
 * 图片剪切工具
 */
public class PictureCutUtils {
    /***
     * 剪切图片
     * @param context
     */
    public static void cropRaWPhoto(final Activity context, Uri mImageCaptureUri, final int CODE_RESULT_REQUEST) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();

        if (size == 0) {

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

                context.startActivityForResult(i, CODE_RESULT_REQUEST);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = context.getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = context.getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        context.getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                context.startActivityForResult(cropOptions.get(item).appIntent,
                                        CODE_RESULT_REQUEST);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(context,"取消", Toast.LENGTH_SHORT).show();
//                        if (mImageCaptureUri != null) {
//                            context.getContentResolver().delete(mImageCaptureUri, null,
//                                    null);
//                            mImageCaptureUri = null;
//                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }


    /**
     * 设置图片框---提取保存剪切后的图片数据，并设置头像部分的View　
     * @param arg2
     */
    public static void setImageToHeadView(Intent arg2, SharedPreferences sharedPreferences, ImageView check_head_photo) {
        Bitmap bitmap;
        Bundle extras = arg2.getExtras();
        String user_photo = sharedPreferences.getString("user_phone","");//账户号——用户的唯一标识
        String filePath = buildPath(user_photo);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(user_photo,filePath);
        editor.commit();
        if (extras != null){
            bitmap = extras.getParcelable("data");
            check_head_photo.setImageBitmap(BitmapHandler.createCircleBitmap(bitmap));//设置圆形图片
            int quality = 100;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
            while (baos.toByteArray().length >10*1024){
                quality -=5;
                baos.reset();//清空字节
                bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
                if(quality == 10){
                    break;
                }
            }
            File f = new File(filePath);
            if (!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            Log.e("CODE_RESULT_REQUEST1111",filePath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                fos.write(baos.toByteArray());
                fos.flush();
                Log.e("CODE_RESULT_REQUEST2222",filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static String PICTURE_URI= Environment.getExternalStorageDirectory()+"/TanTan/picture";

    /**
     * 构建存储路径
     *
     * @return
     */
    public  static String buildPath(String user_photo) {

        File dir = new File(PICTURE_URI);
        if (!dir.exists())
            dir.mkdirs();
        UUID uuid = UUID.randomUUID();
        String sysid = uuid.toString().replaceAll("-", "");
        String fileName = sysid;
        File file = new File(dir, fileName + user_photo+".jpg");
        return file.getPath();
    }

    /**
     * 设置图片框---提取保存剪切后的图片数据，并设置头像部分的View　
     * @param arg2
     */
    public static String setImageToHeadView(Intent arg2, Context context) {
        Bitmap bitmap;
        Bundle extras = arg2.getExtras();
        String user_photo ="Tantan";//账户号——用户的唯一标识
        String filePath = buildPath(user_photo);

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(user_photo,filePath);
//        editor.commit();
        if (extras != null){
            bitmap = extras.getParcelable("data");
//            check_head_photo.setImageBitmap(BitmapHandler.createCircleBitmap(bitmap));//设置圆形图片
            int quality = 100;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
            while (baos.toByteArray().length >10*1024){
                quality -=5;
                baos.reset();//清空字节
                bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
                if(quality == 10){
                    break;
                }
            }
            File f = new File(filePath);
            if (!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            Log.e("CODE_RESULT_REQUEST1111",filePath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                fos.write(baos.toByteArray());
                fos.flush();
                Log.e("CODE_RESULT_REQUEST2222",filePath);
                return filePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally {
                if (fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
