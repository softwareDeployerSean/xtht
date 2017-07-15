package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;

import com.walnutin.xtht.bracelet.app.MyApplication;


/**
 * Created by Administrator on 2016/5/9.
 */
public class DensityUtils {

    public  static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
     //   System.out.println("dip2px" + scale);
        return (int) (dpValue * scale + 0.5f);
    }
    public  static int dip2px( float dpValue) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
     //   System.out.println("dip2px" + scale);
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
