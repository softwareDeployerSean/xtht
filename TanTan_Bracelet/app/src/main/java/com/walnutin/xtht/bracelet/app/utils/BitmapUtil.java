package com.walnutin.xtht.bracelet.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by YuPeng on 2016/8/31.
 * 图片压缩帮助类
 */
public class BitmapUtil {

	public static byte[] getBitmapMaxSize(String imgPath,int maxSize){
		Bitmap image = getBitmapFromPath(imgPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length > maxSize) {
			if(options <= 0){
				break;
			}
			//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
			System.out.println("大小="+baos.toByteArray().length / 1024);
		}
		return baos.toByteArray();
		
	}
	public static Bitmap getBitmapFromPath(String path){
		Bitmap bitmap = null;
		FileInputStream fis = null;
		try {
			File file = new File(path);
			fis = new FileInputStream(path);
			byte[] buff = new byte[(int) file.length()];
			fis.read(buff);
			bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
		
	}
	/**
	 * 根据图片像素宽高压缩
	 * @param path
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap getScaleBitmap(String path,int maxWidth,int maxHeight){
		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		int oldWidth = options.outWidth;
		int oldHeight = options.outHeight;
		
		int sampleSize = 1;//表示不缩放
		//计算应该缩放的系数
		if(oldWidth*1f/oldHeight  > maxWidth*1f/maxHeight){
			sampleSize = (int) (oldWidth/maxWidth);
		}else{
			sampleSize = (int) (oldHeight/maxHeight);
		}
		if(sampleSize < 1){
			sampleSize = 1;
		}
		options.inJustDecodeBounds = false;
		//设置缩放系数
		options.inSampleSize = sampleSize;
		bitmap = BitmapFactory.decodeFile(path, options);
		if(bitmap == null){
			return bitmap;
		}
		return bitmap;
	}
	
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024> 200) {
			if(options <= 0){
				break;
			}
			//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
			System.out.println("大小="+baos.toByteArray().length / 1024);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
}