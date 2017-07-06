package com.walnutin.xtht.bracelet.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.R;

import java.util.Collections;

import io.reactivex.internal.operators.maybe.MaybeNever;

/**
 * Created by Leiht on 2017/7/3.
 */

public class RateView extends View {

    private Context mContext;
    // 默认边距
    private int Margin = 70;
    // 原点坐标
    private int Xpoint;
    private int Ypoint;
    // X,Y轴的单位长度
    private int Xscale = 20;
    private int Yscale = 20;
    // X,Y轴上面的显示文字
//    private String[] Xlabel = { "0", "1", "2", "3", "4", "5", "6", "7", "8" };
    private String[] Xlabel = {"0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24"};

    private String[] Ylabel = new String[150];

    // 标题文本
    private String Title;
    // 曲线数据
    private int[] Data = {75, 89, 110, 68, 120, 87, 69, 98, 150, 123, 86, 145, 75};

    public RateView(Context context, String[] xlabel, String[] ylabel,
                    String title, int[] data) {
        super(context);
        this.Xlabel = xlabel;
        this.Ylabel = ylabel;
        this.Title = title;
        this.Data = data;
        this.mContext = context;
    }

    public RateView(Context context) {
        super(context);
        this.mContext = context;
    }

    public RateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public RateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    // 初始化数据值
    public void init() {

        for (int i = 0; i < 150; i++) {
            Ylabel[i] = String.valueOf(i);
        }

        Margin = getWidth() / 10;

        Xpoint = this.Margin;
        Ypoint = this.getHeight() - this.Margin;
        Xscale = (this.getWidth() - 2 * this.Margin) / (this.Xlabel.length - 1);
        Yscale = (this.getHeight() - 2 * this.Margin)
                / (this.Ylabel.length - 1);
        Log.d("TAG", "getWidth()=" + getWidth() + ",getHeight=" + getHeight());
    }

    public int getMargin() {
        return Margin;
    }

    public void setMargin(int margin) {
        Margin = margin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint p1 = new Paint();
        p1.setStyle(Paint.Style.STROKE);
        p1.setAntiAlias(true);
        p1.setColor(mContext.getResources().getColor(R.color.blue_rate_line));
        p1.setStrokeWidth(2);
        init();
        this.drawXLine(canvas, p1);
        this.drawYLine(canvas, p1);
        this.drawTable(canvas);
        this.drawData(canvas);
    }


    // 画横轴
    private void drawXLine(Canvas canvas, Paint p) {
        p.setStrokeWidth(3);
        canvas.drawLine(Xpoint, Ypoint, this.Margin, this.Margin, p);
//        canvas.drawLine(Xpoint, this.Margin, Xpoint - Xpoint / 3, this.Margin
//                + this.Margin / 3, p);
//        canvas.drawLine(Xpoint, this.Margin, Xpoint + Xpoint / 3, this.Margin
//                + this.Margin / 3, p);
    }

    // 画纵轴
    private void drawYLine(Canvas canvas, Paint p) {
        p.setStrokeWidth(3);
        canvas.drawLine(Xpoint, Ypoint, this.getWidth() - this.Margin, Ypoint,
                p);
//        canvas.drawLine(this.getWidth() - this.Margin, Ypoint, this.getWidth()
//                - this.Margin - this.Margin / 3, Ypoint - this.Margin / 3, p);
//        canvas.drawLine(this.getWidth() - this.Margin, Ypoint, this.getWidth()
//                - this.Margin - this.Margin / 3, Ypoint + this.Margin / 3, p);
    }

    // 画表格
    private void drawTable(Canvas canvas) {
        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        Path path = new Path();

        // 纵向线
//        for (int i = 1; i * Xscale <= (this.getWidth() - this.Margin); i++) {
//            int startX = Xpoint + i * Xscale;
//            int startY = Ypoint;
//            int stopY = Ypoint - (this.Ylabel.length - 1) * Yscale;
//            path.moveTo(startX, startY);
//            path.lineTo(startX, stopY);
//            canvas.drawPath(path, paint);
//        }
        // 横向线
        paint.setColor(Color.BLUE);
        paint.setTextSize(this.Margin * 2 / 5);
        paint.setColor(Color.DKGRAY);
        for (int i = 1; (Ypoint - i * Yscale) >= this.Margin; i++) {
            int startX = Xpoint;
            int startY = Ypoint - i * Yscale;
            int stopX = Xpoint + (this.Xlabel.length - 1) * Xscale;


            if (i == 75) {
                paint.setStyle(Paint.Style.STROKE);
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                paint.setPathEffect(effects);
                path.moveTo(startX, startY);
                path.lineTo(stopX, startY);
                canvas.drawPath(path, paint);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setPathEffect(null);
            if (i == 30 || i == 60 || i == 90 || i == 120 || i == 149) {
                if (i == 149) {
                    canvas.drawText(String.valueOf(Integer.parseInt(this.Ylabel[i]) + 1), this.Margin / 4, startY
                            + this.Margin / 4, paint);
                } else {
                    canvas.drawText(this.Ylabel[i], this.Margin / 4, startY
                            + this.Margin / 4, paint);
                }
            }
        }
    }

    private void sortData(int[] datas) {
        int min,max;
        min=max=datas[0];
        for(int i=0;i<datas.length;i++)
        {
            if(datas[i]>max)   // 判断最大值
                max=datas[i];
            if(datas[i]<min)   // 判断最小值
                min=datas[i];
        }
    }

    // 画数据
    private void drawData(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLUE);
        p.setTextSize(this.Margin / 2);

        int maxValue = Data[0];
        int minValue = Data[0];
        for(int i = 0; i < Data.length; i++) {
            if(Data[i]>maxValue)   // 判断最大值
                maxValue=Data[i];
            if(Data[i]<minValue)   // 判断最小值
                minValue=Data[i];
        }

        // 纵向线
        for (int i = 0; i * Xscale <= (this.getWidth() - this.Margin * 2); i++) {
            int startX = Xpoint + i * Xscale;
            if (i == 0) {
                p.setColor(Color.BLACK);
                p.setTextSize(this.Margin * 2 / 5);
                canvas.drawText(this.Xlabel[i], startX - this.Margin / 4,
                        this.getHeight() - this.Margin / 4, p);
            } else {
                p.setColor(Color.BLACK);
                p.setTextSize(this.Margin * 2 / 5);
                canvas.drawText(this.Xlabel[i], startX - this.Margin / 4,
                        this.getHeight() - this.Margin / 4, p);
                p.setColor(mContext.getResources().getColor(R.color.red_rate_line));
                p.setTextSize(this.Margin / 2);
                if(Data[i] == maxValue || Data[i] == minValue) {
                    canvas.drawCircle(startX, calY(Data[i]), 10, p);
                }
                p.setStrokeWidth(3);
                canvas.drawLine(Xpoint + (i - 1) * Xscale, calY(Data[i - 1]), startX, calY(Data[i]), p);
            }
        }
    }

    /**
     * @param y
     * @return
     */
    private int calY(int y) {
        int y0 = 0;
        int y1 = 0;
        //Log.i("zzzz", "y:"+y);
        try {
            y0 = Integer.parseInt(Ylabel[0]);
            //Log.i("zzzz", "y0"+y0);
            y1 = Integer.parseInt(Ylabel[1]);
            //Log.i("zzzz","y1"+y1);
        } catch (Exception e) {
            //Log.i("zzzz", "string changed is err");
            return 0;
        }
        try {
            //Log.i("zzzz", "返回数据"+(Ypoint-(y-y0)*Yscale/(y1-y0)) );
            return Ypoint - ((y - y0) * Yscale / (y1 - y0));
        } catch (Exception e) {
            //Log.i("zzzz", "return is err");
            return 0;
        }
    }
}

