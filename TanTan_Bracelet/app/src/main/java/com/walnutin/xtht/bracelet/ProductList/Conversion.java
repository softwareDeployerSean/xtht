package com.walnutin.xtht.bracelet.ProductList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 作者：MrJiang on 2016/6/23 09:50
 */
public class Conversion {
    public static List stringToList(String toastBase64) {
        List<Object> cateList = null;
        if (toastBase64 == null) {
            return null;
        }
      //  Log.d("result", "get sp tasteBase64 ----------->: " + toastBase64);
        // 第一步：转换文本数据为二进制数据
        byte[] cateBase64Bytes = Base64.decodeBase64(toastBase64.getBytes());
        // 开启输入流数组字节流
        ByteArrayInputStream cateBAIS = new ByteArrayInputStream(cateBase64Bytes);
        // 开启对象输入流
        ObjectInputStream cateOIS;
        try {
            // 数组字流绑定对象流（把数组转化为对象）
            cateOIS = new ObjectInputStream(cateBAIS);
            // 这里的cateList 就是我们得到的List对象
            cateList = (List<Object>) cateOIS.readObject();
            if (cateList != null) {
           //     Log.d("result", "get sp cateList.size ----------->: " + cateList.size());
            }
            cateOIS.close();
            cateBAIS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cateList != null) {
            Log.d("result", "cateList.size==" + cateList.size());
            //   this.dailyInfoList = cateList;
        } else {
            //    cateList = getVirtualStepList();
        }
        return cateList;
    }

    public static String listToString(List objectList) {
        ByteArrayOutputStream tasteBAOS = new ByteArrayOutputStream();
        ObjectOutputStream tasteOOS = null;
        try {
            tasteOOS = new ObjectOutputStream(tasteBAOS);
            tasteOOS.writeObject(objectList);
            tasteBAOS.close();
            tasteOOS.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.d("result", "save sp IOException -----------> " + e1);
        }
        /* 第二步转换二进制数据为文本数据 Base64.encode转换二进制数据为文本数据) */
        String tasteBase64 = null;
        try {
            tasteBase64 = new String(Base64.encodeBase64(tasteBAOS.toByteArray()));
         //   Log.d("result", "save sp tasteBase64 ----------->: " + tasteBase64);
        } catch (Exception e) {
            e.printStackTrace();
    //        Log.d("result", "save sp Exception -----------> " + e);
        }
        return tasteBase64;
    }

    public static Object stringToObject(String toastBase64) {
        Object cateList = null;
        if (toastBase64 == null) {
            return null;
        }
      //  Log.d("result", "get sp tasteBase64 ----------->: " + toastBase64);
        // 第一步：转换文本数据为二进制数据
        byte[] cateBase64Bytes = Base64.decodeBase64(toastBase64.getBytes());
        // 开启输入流数组字节流
        ByteArrayInputStream cateBAIS = new ByteArrayInputStream(cateBase64Bytes);
        // 开启对象输入流
        ObjectInputStream cateOIS;
        try {
            // 数组字流绑定对象流（把数组转化为对象）
            cateOIS = new ObjectInputStream(cateBAIS);
            // 这里的cateList 就是我们得到的List对象
            cateList = (Object) cateOIS.readObject();
            if (cateList != null) {
                //    Log.d("result", "get sp cateList.size ----------->: " + cateList.size());
            }
            cateOIS.close();
            cateBAIS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cateList != null) {
            //    Log.d("result", "cateList.size==" + cateList.size());
            //   this.dailyInfoList = cateList;
        } else {
            //    cateList = getVirtualStepList();
        }
        return cateList;
    }

    public static String objectToString(Object objectList) {
        ByteArrayOutputStream tasteBAOS = new ByteArrayOutputStream();
        ObjectOutputStream tasteOOS = null;
        try {
            tasteOOS = new ObjectOutputStream(tasteBAOS);
            tasteOOS.writeObject(objectList);
            tasteBAOS.close();
            tasteOOS.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.d("result", "save sp IOException -----------> " + e1);
        }
        /* 第二步转换二进制数据为文本数据 Base64.encode转换二进制数据为文本数据) */
        String tasteBase64 = null;
        try {
            tasteBase64 = new String(Base64.encodeBase64(tasteBAOS.toByteArray()));
          //  Log.d("result", "save sp tasteBase64 ----------->: " + tasteBase64);
        } catch (Exception e) {
            e.printStackTrace();
       //     Log.d("result", "save sp Exception -----------> " + e);
        }
        return tasteBase64;
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 60) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return android.util.Base64.encodeToString(appicon, android.util.Base64.DEFAULT);

    }


    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToBitmap(String st) {
        //  OutputStream out;
        Bitmap bitmap = null;
        try {
            //  out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = android.util.Base64.decode(st, android.util.Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            //    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized Drawable byteToDrawable(String icon) {

        byte[] img = android.util.Base64.decode(icon, android.util.Base64.DEFAULT);
        Bitmap bitmap;
        if (img != null) {


            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bitmap);

            return drawable;
        }
        return null;

    }

    public static  synchronized String drawableToByte(Drawable drawable) {

        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;

            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();

            String icon = android.util.Base64.encodeToString(imagedata, android.util.Base64.DEFAULT);
            return icon;
        }
        return null;
    }
}
