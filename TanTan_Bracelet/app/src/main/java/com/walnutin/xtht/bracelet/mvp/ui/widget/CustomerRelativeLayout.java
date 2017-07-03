package com.walnutin.xtht.bracelet.mvp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.R;

/**
 * Created by user on 6/30/17.
 */

public class CustomerRelativeLayout extends RelativeLayout {
    /**
     * 父布局
     */
    private ViewGroup mParentView;
    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private int downX;
    /**
     * 按下点的Y坐标
     */
    private int downY;
    /**
     * 临时存储X坐标
     */
    private int tempY;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * 宽度
     */
    private int viewHeight;

    private boolean isSilding;

    private OnFinishListener onFinishListener;
    private boolean isFinish;
    Context context;
    private ImageView iv_suo;
    private ImageView iv_tag;
    private TextView tv_tag;

    /**
     * 向上或者向下
     * fasle:向下，不可点击
     * true: 向上,可点击
     */
    boolean isUpOrDown = true;

    public CustomerRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CustomerRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.define_relative, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        this.context = context;
        iv_suo = (ImageView) findViewById(R.id.iv_suo);
        iv_tag = (ImageView) findViewById(R.id.iv_jiantou);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
    }

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = tempY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                // 满足此条件屏蔽touch事件
                if (isUpOrDown) {
                    if (Math.abs(downY - moveY) > mTouchSlop
                            && Math.abs((int) ev.getRawX() - downX) < mTouchSlop) {
                        return true;
                    }
                } else {
                    if (Math.abs(moveY - downY) > mTouchSlop
                            && Math.abs((int) ev.getRawX() - downX) < mTouchSlop) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isUpOrDown) {
                    int moveY = (int) event.getRawY();// 触摸点相对于屏幕的位置
                    int deltaY = moveY - tempY;
                    tempY = moveY;
                    isSilding = false;
                    if (Math.abs(downY - moveY) > mTouchSlop && moveY > mParentView.getY()
                            && Math.abs((int) event.getRawX() - downX) < mTouchSlop) {
                        isSilding = true;
                    }
                    Log.d("TAG", "Math.abs(downY - moveY) = " + Math.abs(downY - moveY));
                    Log.d("TAG", "mParentView.getHeight() = " + mParentView.getHeight());
                    Log.d("TAG", "downY = " + downY);
                    if (downY - moveY >= 0 && isSilding) {
                        mParentView.scrollBy(0, -deltaY);
                    }
                } else {
                    int moveY = (int) event.getRawY();// 触摸点相对于屏幕的位置
                    int deltaY = tempY - moveY;
                    tempY = moveY;
                    if (Math.abs(moveY - downY) > mTouchSlop
                            && Math.abs((int) event.getRawX() - downX) < mTouchSlop) {
                        isSilding = true;
                    }

                    if (moveY - downY >= 0 && isSilding) {
                        mParentView.scrollBy(0, deltaY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if (mParentView.getScrollY() <= -viewHeight / 5) {
                    isFinish = true;
                    scrollBottom();
                } else if (mParentView.getScrollY() >= viewHeight / 5) {
                    isFinish = true;
                    scrollTop();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mParentView = (ViewGroup) this.getParent();
            viewHeight = this.getHeight();
        }
    }

    /***
     * 接口回调
     */
    public void setOnFinishListener(
            OnFinishListener onSildingFinishListener) {
        this.onFinishListener = onSildingFinishListener;
    }

    /**
     * 滚动出下界面
     */
    private void scrollBottom() {
//        final int delta = (viewHeight + mParentView.getScrollY());
//        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta + 1,
//                Math.abs(delta));

        tv_tag.setText("上滑锁定");
        iv_tag.setImageResource(R.mipmap.weisuo);
        iv_suo.setImageResource(R.mipmap.shanghua);

        int delta = mParentView.getScrollY();
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta,
                Math.abs(delta));
        postInvalidate();

        if (onFinishListener != null) {
            onFinishListener.onFinish(isUpOrDown);
        }
        isUpOrDown = !isUpOrDown;
    }

    /**
     * 滚动出上界面
     */
    private void scrollTop() {
//        final int delta = (viewHeight - mParentView.getScrollY());
//        mScroller.startScroll(0, mParentView.getScrollY(), 0, delta - 1,
//                Math.abs(delta));

        tv_tag.setText("下滑解锁");
        iv_tag.setImageResource(R.mipmap.xiahua);
        iv_suo.setImageResource(R.mipmap.suo);
        int delta = mParentView.getScrollY();
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta,
                Math.abs(delta));

        postInvalidate();
        if (onFinishListener != null) {
            onFinishListener.onFinish(isUpOrDown);
        }
        isUpOrDown = !isUpOrDown;
    }


    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mParentView.getScrollY();

        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta,
                Math.abs(delta));

        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                if (onFinishListener != null) {
//                    onFinishListener.onFinish(isUpOrDown);
//                    isUpOrDown = !isUpOrDown;
                } else {
                    // 没有设置OnSildingFinishListener，让其滚动到其实位置
                    scrollOrigin();
                    isFinish = false;
                }
            }
        }
    }

    public interface OnFinishListener {
        public void onFinish(boolean isUpOrDown);
    }


}
