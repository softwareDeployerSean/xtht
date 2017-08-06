package com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;


/**
 * Created by Leiht on 2017/8/6.
 */

public class CanotSlidingVerticalViewpager extends VerticalViewPager {

    /**
     * 上一次x坐标
     */
    private float beforeX;

    /**
     * 上一次Y坐标
     */
    private float beforeY;

    private int touchSlop = 0;

    private boolean isCanScroll = true;

    public CanotSlidingVerticalViewpager(Context context) {
        super(context);
    }

    public CanotSlidingVerticalViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //-----禁止左滑-------左滑：上一次坐标 > 当前坐标
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                //按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                case MotionEvent.ACTION_DOWN:
                    beforeX = ev.getX();
                    beforeY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionXValue = ev.getX() - beforeX;

                    float motionYValue = ev.getY() - beforeY;

                    Log.d("TAG", "motionValue=" + motionXValue);
                    if (motionYValue < 0) {//禁止左滑
                        return false;
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }

    }
    public boolean isScrollble() {
        return isCanScroll;
    }

    /**
     * 设置 是否可以滑动
     *
     * @param isCanScroll
     */
    public void setScrollble(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}
