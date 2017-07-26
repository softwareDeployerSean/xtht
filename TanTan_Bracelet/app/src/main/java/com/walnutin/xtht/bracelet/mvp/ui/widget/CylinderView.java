package com.walnutin.xtht.bracelet.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.walnutin.xtht.bracelet.app.utils.DensityUtil;

import java.util.Random;

/**
 * Created by he_pan on 2017/6/6.
 * The only genius that is worth anything is the genius for hard work
 * 柱形图(粗直线)
 *
 * @author he_pan
 */

public class CylinderView extends View {

    private int mWidth;
    private int mHeight;
    private float centerX;
    private float centerY;
    private float leftX;
    private float rightX;
    private float hourTextHeight;//小时文字的高度
    private float stepTextHeight;//总步数文字高度
    private int startColor = 0xff18FF00;
    private int endColor = 0xffFAFF01;
    private Paint cylinderPaint;
    private Paint linePaint;
    private Paint textPaint;
    private int strokeWidth;
    private int totalStep = 0;//总共步数
    private int maxStep = 0;//单位最大步数
    private Context ctx;
    private int[] datas;

    public CylinderView(Context context) {
        this(context, null);
    }

    public CylinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        init(context);
    }

    //初始化画笔
    private void init(Context context) {
        cylinderPaint = new Paint();
        cylinderPaint.setAntiAlias(true);
        cylinderPaint.setStyle(Paint.Style.STROKE);
        strokeWidth = DensityUtil.dip2px(context, 8);
        cylinderPaint.setStrokeWidth(strokeWidth);//宽度设为12dp
        cylinderPaint.setStrokeCap(Paint.Cap.ROUND);
        cylinderPaint.setColor(Color.parseColor("#18FF00"));
        LinearGradient mColorShader = new LinearGradient(0, 500, 10, 0, startColor, endColor, Shader.TileMode.MIRROR);
        cylinderPaint.setShader(mColorShader);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DensityUtil.dip2px(context, 1));
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(0xff6ad489);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xff444444);
        textPaint.setStrokeWidth(DensityUtil.dip2px(context, 1));


//        datas = new int[24];
//        for (int i = 0; i < 24; i++) {
//            int r = new Random().nextInt(100);
//            datas[i] = r;
//            totalStep += r;
//            maxStep = Math.max(maxStep, r);
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        leftX = mWidth * 0.05f;
        rightX = mWidth * 0.95f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);//文本
        drawLines(canvas);
        drawCylinder(canvas);
    }

    private void drawText(Canvas canvas) {
        //总步数
        Rect totalRect = new Rect();
        String totalStr = totalStep + "";
        textPaint.setTextSize(DensityUtil.dip2px(ctx, 30));
        textPaint.getTextBounds(totalStr, 0, totalStr.length(), totalRect);
        stepTextHeight = totalRect.height();
        canvas.translate(centerX, 0);//改变画布坐标参考点,方便计算
        canvas.drawText(totalStr, -totalRect.width() / 2, totalRect.height(), textPaint);

        //时间轴
        canvas.translate(-centerX, 0);
        Rect rect = new Rect();
        String h = "6";
        textPaint.setTextSize(DensityUtil.dip2px(ctx, 12));
        textPaint.getTextBounds(h, 0, h.length(), rect);
        hourTextHeight = rect.height();
        float interval = (rightX - leftX - 80) / 4;//数据宽度比最外层线要靠内
        for (int i = 0; i < 5; i++) {
            if (i != 4) {
                String hour = String.valueOf((i * 6) % 24);
                canvas.drawText(hour, leftX + interval * i, mHeight, textPaint);
            } else {
                String hour = "23";
                canvas.drawText(hour, leftX + interval * i, mHeight, textPaint);
            }
        }

    }

    //x,y轴
    private void drawLines(Canvas canvas) {
        canvas.drawLine(leftX, 1, leftX, mHeight - hourTextHeight - 10, linePaint);
        canvas.drawLine(leftX, mHeight - hourTextHeight - 10, rightX, mHeight - hourTextHeight - 10, linePaint);
    }

    //柱子
    private void drawCylinder(Canvas canvas) {
        if(datas != null && datas.length > 0) {
            float startY = mHeight - hourTextHeight - DensityUtil.dip2px(ctx, 10);//起始y坐标
            float maxHeight = mHeight - hourTextHeight - stepTextHeight - DensityUtil.dip2px(ctx, 30);
            float intervalX = (rightX - leftX - 80) / 24;//数据宽度比最外层线要靠内
            float intervalY = maxHeight / maxStep;//根据最大步数,分割y轴
            //值需要计算起始x坐标和终止y坐标即可
            float startX = leftX;
            float endY;
            cylinderPaint.setStrokeWidth((float) (intervalX * 0.7));
            for (int i = 0; i < 24; i++) {
                startX += intervalX;
                endY = startY - datas[i] * intervalY;//向上画y轴变小
                canvas.drawLine(startX, startY, startX, endY, cylinderPaint);
            }
        }
    }

    public void setDatas(int[] datas) {
        this.datas = datas;
        for (int i = 0; i < 24; i++) {
            int r = datas[i];
            totalStep += r;
            maxStep = Math.max(maxStep, r);
        }
        postInvalidate();
    }
}

