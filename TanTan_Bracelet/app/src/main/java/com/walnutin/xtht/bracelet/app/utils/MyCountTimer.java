package com.walnutin.xtht.bracelet.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.CountdownActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningIndoorActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningOutsideActivity;

/**
 * @author donkor
 */
public class MyCountTimer {
    public static final int TIME_COUNT = 4000;//倒计时总时间为31S，时间防止从29s开始显示（以倒计时30s为例子）
    private TextView btn;
    private String endStrRid;
    private Activity activity;
    Handler mhandler = new Handler();
    long time = 4;


    public MyCountTimer(Activity activity,String endStrRid,TextView textView) {
        this.endStrRid = endStrRid;
        mhandler.post(timerrunable);
        btn=textView;
        this.activity=activity;
    }

    Runnable timerrunable = new Runnable() {
        @Override
        public void run() {
            if (time == 1) {
                mhandler.removeCallbacks(timerrunable);
                onFinish();
            } else {
                onTick(--time);
                mhandler.postDelayed(timerrunable,1000);
            }

        }
    };


    /**
     * 计时完毕时触发
     */

    public void onFinish() {
        if (endStrRid.equals("running_indoor")) {
            activity.startActivity(new Intent(activity, RunningIndoorActivity.class));
            activity.finish();
        } else {
            Intent intent = new Intent(activity, RunningOutsideActivity.class);
            intent.putExtra("tag", endStrRid);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * 计时过程显示
     */

    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        //每隔一秒修改一次UI
        btn.setText(millisUntilFinished + "");

        // 设置透明度渐变动画
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //设置动画持续时间
        alphaAnimation.setDuration(1000);
        btn.startAnimation(alphaAnimation);

        // 设置缩放渐变动画
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 2f, 0.5f, 2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        btn.startAnimation(scaleAnimation);
    }
}


