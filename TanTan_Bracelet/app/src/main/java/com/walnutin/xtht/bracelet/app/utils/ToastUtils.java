package com.walnutin.xtht.bracelet.app.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.walnutin.xtht.bracelet.R;

/**
 * Toast 显示工具
 */
public final class ToastUtils {

	//以下为自定义的一个Toast
	private static Toast mToast2;
	private static TextView mToastTv;
	public static void showToast(String text,Context context){
		if(mToast2 == null){
			mToast2 = new Toast(context);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.define_toast, null);
			mToast2.setView(view); //为Toast对象设置显示的视图
			mToast2.setDuration(Toast.LENGTH_SHORT);
			mToastTv = (TextView) view.findViewById(R.id.toastTv);
			mToast2.setGravity(Gravity.CENTER, 0, 100); //0代表向x坐标右边偏移0像素
		}
		mToastTv.setText(text); //设置要显示的文本 注意这里不能掉用mToast2.setText方法
		mToast2.show();
	}
}
