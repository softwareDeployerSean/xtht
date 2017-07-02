package com.walnutin.xtht.bracelet.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.CountdownActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningIndoorActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningOutsideActivity;

/**
 * @author donkor
 */
public class MyCountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 4000;//倒计时总时间为31S，时间防止从29s开始显示（以倒计时30s为例子）
    private TextView btn;
    private String endStrRid;
    private Activity activity;

    /**
     * 参数 millisInFuture         倒计时总时间（如30s,60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     * 参数 btn               点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * 参数 endStrRid   倒计时结束后，按钮对应显示的文字
     */
    public MyCountTimer(long millisInFuture, long countDownInterval, TextView btn, String endStrRid, Activity activity) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
        this.activity = activity;
    }

    /**
     * 参数上面有注释
     */
    public MyCountTimer(TextView btn, String endStrRid) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    /**
     * 计时完毕时触发
     */
    @Override
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
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        //每隔一秒修改一次UI
        btn.setText(millisUntilFinished / 1000 + "");

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


