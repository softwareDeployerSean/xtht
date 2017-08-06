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

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.util.DensityUtil;

import java.util.Random;

/**
 * 柱形图
 *
 * @author Ray
 */

public class HistogramView extends View {

    private int mWidth;
    private int mHeight;

    private float leftX;
    private float rightX;
    private float xTextHeight;//X轴上单个文字的高度
    private int xTextWidth; //X轴上单个文字的宽度

    private int startColor = Color.parseColor("#6B289B");
    private int endColor = Color.parseColor("#D0B3EB");
    private Paint histogramPaint;

    private Paint textPaint;
    private int strokeWidth;
    private int maxY = 0;//Y轴最大值

    private float intervalPercent = 0.5f;

    private Context ctx;
    private int[] datas;

    private String[] xLables;

    private int xDisplayInterval = -1;

    /**
     * X轴上显示方式
     * 0：全显示
     * 1：显示奇数
     * 2：显示偶数
     * 可持续添加
     */
    private int xDisplayType = 0;

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        init(context);
    }

    //初始化画笔
    private void init(Context context) {
        histogramPaint = new Paint();
        histogramPaint.setAntiAlias(true);
        histogramPaint.setStyle(Paint.Style.STROKE);
        strokeWidth = DensityUtil.dip2px(context, 8);
        histogramPaint.setStrokeWidth(strokeWidth);
        histogramPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xff444444);
        textPaint.setStrokeWidth(DensityUtil.dip2px(context, 1));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        leftX = mWidth * 0.05f;
        rightX = mWidth * 0.95f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawText(canvas);//X轴文字

        drawCylinder(canvas);
    }

    //画X轴文字
    private void drawText(Canvas canvas) {
        Rect rect = new Rect();
        String h = "10";
        textPaint.setTextSize(DensityUtil.dip2px(ctx, 12));
        textPaint.getTextBounds(h, 0, h.length(), rect);
        xTextHeight = rect.height();
        xTextWidth = rect.width();
        if (xLables != null && xLables.length > 0) {
            if (xDisplayInterval != -1) {
                int xInterval = 0;
                if (xLables.length < xDisplayInterval) {
                    xInterval = this.xLables.length;
                } else if (this.xLables.length % xDisplayInterval == 0) {
                    xInterval = this.xLables.length;
                } else {
                    xInterval = this.xLables.length / xDisplayInterval + 1;
                }


                float interval = (rightX - leftX - 80) / (xLables.length - 1);//数据宽度比最外层线要靠内

                for (int i = 0; i < xLables.length; i++) {
                    if (i % xDisplayInterval == 0 || i == xLables.length - 1) {
                        String text = String.valueOf((xLables[i]));
                        canvas.drawText(text, leftX + interval * i, mHeight, textPaint);
                    }
                }

            } else if (xDisplayType == 0) {
                float interval = (rightX - leftX - 80) / (xLables.length - 1);//数据宽度比最外层线要靠内
                for (int i = 0; i < xLables.length; i++) {
                    String text = String.valueOf((xLables[i]));
                    canvas.drawText(text, leftX + interval * i, mHeight, textPaint);

                }
            } else if (xDisplayType == 1) {//显示奇数
                float interval;
                if (xLables.length % 2 == 0) { //偶数
                    interval = (rightX - leftX - 80) / (xLables.length / 2) - 1;
                } else {//奇数
                    interval = (rightX - leftX - 80) / ((xLables.length - 1) / 2);
                }
                for (int i = 0; i < xLables.length; i++) {
                    if ((i + 1) % 2 != 0) {
                        String text = String.valueOf(xLables[i]);
                        canvas.drawText(text, leftX + interval * (i / 2), mHeight, textPaint);
                    }
                }
            }
        }

    }

    //画柱状图
    private void drawCylinder(Canvas canvas) {
        if (xLables != null) {
            float startY = mHeight - xTextHeight - DensityUtil.dip2px(ctx, 15);//起始y坐标
            float maxHeight = mHeight - xTextHeight - DensityUtil.dip2px(ctx, 30);
            float intervalX = (rightX - leftX - 80) / (xLables.length - 1);//数据宽度比最外层线要靠内
            float intervalY = maxHeight / maxY;//根据最大步数,分割y轴
            //值需要计算起始x坐标和终止y坐标即可
            float startX = leftX;
            float endY;
            histogramPaint.setStrokeWidth((float) (intervalX * intervalPercent));
            histogramPaint.setColor(startColor);
            LinearGradient mColorShader = new LinearGradient(0, 500, 10, 0, startColor, endColor, Shader.TileMode.MIRROR);
            histogramPaint.setShader(mColorShader);
            if (datas != null && datas.length > 0) {
                for (int i = 0; i < datas.length; i++) {
                    startX = leftX + xTextWidth / 2 + intervalX * i;
                    endY = startY - datas[i] * intervalY;
                    if(datas[i] > 0) {
                        canvas.drawLine(startX, startY, startX, endY, histogramPaint);
                    }
                }
            }
        }
    }

    /**
     * 设置柱状渐变起始颜色
     * 格式：0xffxxxxxx
     *
     * @param startColor
     */
    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    /**
     * 设置柱状渐变色结尾颜色
     * 格式：0xffxxxxxx
     *
     * @param endColor
     */
    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    /**
     * 设置X轴显示文字的数组
     *
     * @param xLables
     */
    public void setxLables(String[] xLables) {
        this.xLables = xLables;
    }

    /**
     * 设置数据
     * 注意，此数据的长度必须和xLables相同
     *
     * @param datas
     */
    public void setDatas(int[] datas) {
        this.datas = datas;
        for (int i = 0; i < datas.length; i++) {
//            int r = new Random().nextInt(100);
//            datas[i] = r;
            maxY = Math.max(maxY, datas[i]);
        }
        postInvalidate();
    }

    /**
     * 设置柱子占平分后距离的百分比
     * 如果不设置默认为0.5
     *
     * @param intervalPercent
     */
    public void setIntervalPercent(float intervalPercent) {
        this.intervalPercent = intervalPercent;
    }

    /**
     * 设置X轴显示方式（主要考虑文字或数字太多显示太挤）
     * 显示方式参考xDisplayType属性注释
     * 不设置默认为0，显示全部
     *
     * @param xDisplayType
     */
    public void setxDisplayType(int xDisplayType) {
        this.xDisplayType = xDisplayType;
    }

    public void setxDisplayInterval(int xDisplayInterval) {
        this.xDisplayInterval = xDisplayInterval;
    }
}
