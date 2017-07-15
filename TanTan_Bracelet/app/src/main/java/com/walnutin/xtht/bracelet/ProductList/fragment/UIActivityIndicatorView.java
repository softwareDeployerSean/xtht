package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.walnutin.xtht.bracelet.R;


/**
 * Created by wantao on 17/1/7.
 */
public class UIActivityIndicatorView extends View {
    public UIActivityIndicatorView(Context context) {
        this(context, null);
    }

    private int startColor = 0xff686868;
    private float strokeWidth = 0;
    private int startAngle = 0;

    public UIActivityIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIActivityIndicatorView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int i1 = typedArray.getIndex(i);
            if (i1 == R.styleable.UIActivityIndicatorView_AIV_startColor) {
                startColor = typedArray.getColor(R.styleable.UIActivityIndicatorView_AIV_startColor, startColor);

            } else if (i1 == R.styleable.UIActivityIndicatorView_AIV_startAngle) {
                startAngle = typedArray.getInt(R.styleable.UIActivityIndicatorView_AIV_startAngle, startAngle);

            } else if (i1 == R.styleable.UIActivityIndicatorView_AIV_strokeWidth) {
                strokeWidth = typedArray.getDimension(R.styleable.UIActivityIndicatorView_AIV_strokeWidth, strokeWidth);

            }
        }

        initialize();
    }

    private final int LineCount = 12;
    private final int MinAlpha = 0;
    private final int AngleGradient = 360 / LineCount;
    private Paint paint;
    private int[] colors = new int[LineCount];

    private void initialize() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int alpha = Color.alpha(startColor);
        int red = Color.red(startColor);
        int green = Color.green(startColor);
        int blue = Color.blue(startColor);
        int alpha_gradient = Math.abs(alpha - MinAlpha) / LineCount;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.argb(alpha - alpha_gradient * i, red, green, blue);
        }
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float radius = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom()) * 0.5f;
        if (strokeWidth == 0) strokeWidth = pointX(AngleGradient / 2, radius / 2) / 2;
        paint.setStrokeWidth(strokeWidth);
        for (int i = 0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawLine(
                    centerX + pointX(-AngleGradient * i + startAngle, radius / 2),
                    centerY + pointY(-AngleGradient * i + startAngle, radius / 2),
                    centerX + pointX(-AngleGradient * i + startAngle, radius - strokeWidth / 2),   //  这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                    centerY + pointY(-AngleGradient * i + startAngle, radius - strokeWidth / 2),   //  这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                    paint);
        }
    }

    private float pointX(int angle, float radius) {
        return (float) (radius * Math.cos(angle * Math.PI / 180));
    }

    private float pointY(int angle, float radius) {
        return (float) (radius * Math.sin(angle * Math.PI / 180));
    }

    private Handler animHandler = new Handler();
    private Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            startAngle += AngleGradient;
            invalidate();
            animHandler.postDelayed(animRunnable, 50);
        }
    };

    public void start() {
        animHandler.post(animRunnable);
    }

    public void stop() {
        animHandler.removeCallbacks(animRunnable);
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }
}