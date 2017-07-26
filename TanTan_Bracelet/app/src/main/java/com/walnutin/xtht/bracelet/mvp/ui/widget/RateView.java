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

import com.walnutin.xtht.bracelet.R;

/**
 * Created by Leiht on 2017/7/3.
 */

public class RateView extends View {

    // 默认边距
    private int Margin = 70;
    // 原点坐标
    private int Xpoint;
    private int Ypoint;
    // X,Y轴的单位长度
    private int Xscale = 20;
    private float Yscale = 20;

    // X,Y轴上面的显示文字
    private String[] Xlabel = {"0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24"};

    private int[] brolenLineDisplay = new int[]{};

    /**
     * Y轴的最大高度
     * 此值原则上必须动态设置，并且大于等于Ylabel的最大值
     */
    private int maxYlabel = 150;

    private int xDisplayInterval = 1;
    private int xInterval = 0;
    private String[] Ylabel = new String[150];

    private int[] yDisPlay;

    private int marginPer = 10;

    /**
     * 0:正常显示
     * 1：秒换算成时分秒
     */
    private int yDisPlayType = 0;

    /**
     * X Y 虚线颜色
     */
    private int lineColor = Color.BLUE;

    /**
     * 折线的颜色
     */
    private int brokenLineColor = Color.BLUE;

    // 曲线数据
    private int[] datas = {75, 89, 110, 68, 120, 87, 69, 98, 150, 123, 86, 145, 75};

    public RateView(Context context, String[] xlabel, String[] ylabel, int[] data) {
        super(context);
        this.Xlabel = xlabel;
        this.Ylabel = ylabel;
        this.datas = data;
    }

    public RateView(Context context) {
        super(context);
    }

    public RateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 初始化数据值
    public void init() {

//        for (int i = 0; i < 150; i++) {
//            Ylabel[i] = String.valueOf(i);
//        }

        Margin = getWidth() / marginPer;

        Xpoint = this.Margin;
        Ypoint = this.getHeight() - this.Margin;

        if(Xlabel.length < xDisplayInterval) {
            xInterval = this.Xlabel.length;
        }else if(this.Xlabel.length % xDisplayInterval == 0) {
            xInterval = this.Xlabel.length;
        }else {
            xInterval = this.Xlabel.length % xDisplayInterval + this.Xlabel.length;
        }

        Log.d("TAG", "xInterval=" + xInterval);

        float f = (float)(Ylabel.length);

        Xscale = (this.getWidth() - 2 * this.Margin) / xInterval;
        Yscale = (this.getHeight() -  2 * this.Margin)
                / f;
        Log.d("TAG", "getWidth()=" + getWidth() + ",getHeight=" + getHeight() + ", Yscale=" + Yscale);
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
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(2);
        init();
        this.drawXLine(canvas, linePaint);
        this.drawYLine(canvas, linePaint);
        this.drawTable(canvas);
        this.drawData(canvas);
    }


    // 画横轴
    private void drawXLine(Canvas canvas, Paint linePaint) {
        linePaint.setStrokeWidth(2);
        canvas.drawLine(Xpoint, Ypoint, this.Margin, this.Margin, linePaint);
//        canvas.drawLine(Xpoint, this.Margin, Xpoint - Xpoint / 3, this.Margin
//                + this.Margin / 3, p);
//        canvas.drawLine(Xpoint, this.Margin, Xpoint + Xpoint / 3, this.Margin
//                + this.Margin / 3, p);
    }

    // 画纵轴
    private void drawYLine(Canvas canvas, Paint p) {
        p.setStrokeWidth(4);
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
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        Path path = new Path();
        PathEffect effects = new DashPathEffect(new float[]{10, 5}, 1);
        paint.setPathEffect(effects);
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
        if(yDisPlayType == 1) {
            paint.setTextSize(this.Margin * 2 / 8);
        }else {
            paint.setTextSize(this.Margin * 2 / 6);
        }
        paint.setColor(Color.DKGRAY);
        for (int i = 1; (Ypoint - i * Yscale) >= this.Margin; i++) {
            int startX = Xpoint;
            int startY = (int)(Ypoint - i * Yscale);
            int stopX = Xpoint + (this.Xlabel.length - 1) * Xscale;

            boolean isBrokenLineDisplay = false;
            for(int j = 0; j < brolenLineDisplay.length; j++) {
                if(brolenLineDisplay[j] == i) {
                    isBrokenLineDisplay = true;
                }
            }
            if (isBrokenLineDisplay) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setPathEffect(effects);
                paint.setStrokeWidth(2);
                paint.setColor(getResources().getColor(R.color.blue_6AD489));
                path.moveTo(startX, startY);
                path.lineTo(this.getWidth() - Margin, startY);
                canvas.drawPath(path, paint);
            }

            boolean isYDisPlay = false;
            if(yDisPlay != null) {
                for (int z = 0; z < yDisPlay.length; z++) {
                    if (yDisPlay[z] == i) {
                        isYDisPlay = true;
                        break;
                    }
                }
            }
            if (isYDisPlay) {
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                String text = "";
                if(yDisPlayType == 1) {
                    text = secToTime(Integer.parseInt(this.Ylabel[i]));
                }else {
                    text = this.Ylabel[i];
                }
                if (i == Ylabel.length-1) {

                    canvas.drawText(text, this.Margin / 4, startY
                            + this.Margin / 4, paint);
                } else {
                    canvas.drawText(text, this.Margin / 4, startY
                            + this.Margin / 4, paint);
                }
            }
        }
    }

    private String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00'00''";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "'" + unitFormat(second) + "''";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "'" + unitFormat(minute) + "''" + unitFormat(second) + "''";
            }
        }
        return timeStr;
    }

    public String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    // 画数据
    private void drawData(Canvas canvas) {
        Paint brokenPaint = new Paint();
        brokenPaint.setAntiAlias(true);
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setTextSize(this.Margin / 2);
        brokenPaint.setStrokeWidth(3);
        // 纵向线
        for (int i = 0; i * Xscale <= (this.getWidth() - this.Margin * 2); i++) {
            int startX = Xpoint + i * Xscale;
            if (i < datas.length) {
                if (i == 0) {
                    brokenPaint.setColor(Color.BLACK);
                    brokenPaint.setTextSize(this.Margin * 2 / 5);
                    if (i % xDisplayInterval == 0) {
                        canvas.drawText(this.Xlabel[i], startX - this.Margin / 4 + 5,
                                this.getHeight() - this.Margin / 4, brokenPaint);
                    }

                    if (datas[0] != 0) {
                        brokenPaint.setColor(brokenLineColor);
                        brokenPaint.setTextSize(this.Margin / 2);
                        canvas.drawCircle(startX, calY(datas[i]), 4, brokenPaint);
                    }
                } else if (i == datas.length - 1) {
                    if (datas[datas.length - 1] != 0) {
                        if(this.Xlabel.length % xDisplayInterval == 0) {
                            brokenPaint.setColor(Color.BLACK);
                            brokenPaint.setTextSize(this.Margin * 2 / 5);
                            canvas.drawText(this.Xlabel[i], startX - this.Margin / 4,
                                    this.getHeight() - this.Margin / 4, brokenPaint);
                        }
                        brokenPaint.setColor(brokenLineColor);
                        brokenPaint.setTextSize(this.Margin / 2);
                        if (i % xDisplayInterval == 0) {
                            canvas.drawCircle(startX, calY(datas[i]), 4, brokenPaint);
                        }
                        if (datas[i - 1] != 0) {
                            canvas.drawCircle(startX, calY(datas[i]), 4, brokenPaint);
                            canvas.drawLine(Xpoint + (i - 1) * Xscale, calY(datas[i - 1]), startX, calY(datas[i]), brokenPaint);
                        }
                    } else {
                        brokenPaint.setColor(Color.BLACK);
                        brokenPaint.setTextSize(this.Margin * 2 / 5);
                        if (i % xDisplayInterval == 0) {
                            canvas.drawText(this.Xlabel[i], startX - this.Margin / 4,
                                    this.getHeight() - this.Margin / 4, brokenPaint);
                        }

                    }
                } else {
                    brokenPaint.setColor(Color.BLACK);
                    brokenPaint.setTextSize(this.Margin * 2 / 5);
                    if (i % xDisplayInterval == 0) {
                        canvas.drawText(this.Xlabel[i], startX - this.Margin / 4,
                                this.getHeight() - this.Margin / 4, brokenPaint);
                    }
                    brokenPaint.setColor(brokenLineColor);
                    brokenPaint.setTextSize(this.Margin / 2);

                    if (datas[i] == 0) {
                        continue;
                    }

                    if (datas[i - 1] == 0) {
                        canvas.drawCircle(startX, calY(datas[i]), 4, brokenPaint);
                        continue;
                    }

                    canvas.drawCircle(startX, calY(datas[i]), 4, brokenPaint);
                    canvas.drawLine(Xpoint + (i - 1) * Xscale, calY(datas[i - 1]), startX, calY(datas[i]), brokenPaint);
                }
            }else if(i == xInterval) {
                brokenPaint.setColor(Color.BLACK);
                brokenPaint.setTextSize(this.Margin * 2 / 5);
                canvas.drawText(String.valueOf(xInterval), startX - this.Margin / 4 + 5,
                        this.getHeight() - this.Margin / 4, brokenPaint);
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
            return (int)(Ypoint - ((y - y0) * Yscale / (y1 - y0)));
        } catch (Exception e) {
            //Log.i("zzzz", "return is err");
            return 0;
        }
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setBrokenLineColor(int brokenLineColor) {
        this.brokenLineColor = brokenLineColor;
    }

    public void setXlabel(String[] xlabel) {
        this.Xlabel = xlabel;
    }

    public void setMaxYlabel(int maxYlabel) {
        this.maxYlabel = maxYlabel;
    }

    public void setxDisplayInterval(int xDisplayInterval) {
        this.xDisplayInterval = xDisplayInterval;
    }

    public void setDatas(int[] datas) {
        this.datas = datas;
    }

    public void setBrolenLineDisplay(int[] brolenLineDisplay) {
        this.brolenLineDisplay = brolenLineDisplay;
    }

    public void setyDisPlay(int[] yDisPlay) {
        this.yDisPlay = yDisPlay;
    }

    public void setYlabel(String[] ylabel) {
        this.Ylabel = ylabel;
    }

    public void setyDisPlayType(int type) {
        this.yDisPlayType = type;
    }

    public void setMarginPer(int marginPer) {
        this.marginPer = marginPer;
    }
}


