package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRunningIndoorComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RunningIndoorModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningIndoorContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RunningIndoorPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomerRelativeLayout;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnDismissListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RunningIndoorActivity extends BaseActivity<RunningIndoorPresenter> implements RunningIndoorContract.View, OnItemClickListener, OnDismissListener {

    @BindView(R.id.layout)
    public CustomerRelativeLayout mCustomerRelativeLayout;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_calories)
    TextView tvCalories;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.iv_jiesu)
    ImageView ivJiesu;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.iv_goin)
    ImageView ivGoin;
    @BindView(R.id.tv_jieshu)
    TextView tvJieshu;
    @BindView(R.id.tv_stop)
    TextView tvStop;
    @BindView(R.id.tv_goin)
    TextView tvGoin;

    //计时器
    private Timer timer1;
    private TimerTask timerTask;
    AlertView alertView;
    //数据库

    private DbAdapter DbHepler;
    private PathRecord record;
    private long mStartTime;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRunningIndoorComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .runningIndoorModule(new RunningIndoorModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_running_indoor; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    int cnt = 0;

    @Override
    public void initData(Bundle savedInstanceState) {
        mCustomerRelativeLayout.setOnFinishListener(new CustomerRelativeLayout.OnFinishListener() {
            @Override
            public void onFinish(boolean isUpOrDown) {
                if (isUpOrDown) {
                    //可点击
                    set_click(false);
                } else {
                    //不可点击
                    set_click(true);
                }
            }
        });
        set_button_nomal();
        timer1 = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTime.setText(getStringTime(cnt++));
                    }
                });
            }
        };
        timer1.schedule(timerTask, 0, 1000);

        //给对象设置
        record = new PathRecord();
        mStartTime = System.currentTimeMillis();
        record.setDate(getcueDate(mStartTime));

    }

    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    double distance = 0;

    @OnClick({R.id.iv_jiesu, R.id.iv_stop, R.id.iv_goin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_jiesu:
                distance = Double.parseDouble(tvLength.getText().toString().trim());
                if (distance < 50) {
                    short_distance();
                } else {
                    exit();
                }
                break;
            case R.id.iv_stop:
                set_button_click();
                if (timerTask != null) {
                    timerTask.cancel();  //将原任务从队列中移除
                }
                break;
            case R.id.iv_goin:
                if (timerTask != null) {
                    timerTask.cancel();  //将原任务从队列中移除
                }
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTime.setText(getStringTime(cnt++));
                            }
                        });
                    }
                };
                timer1.schedule(timerTask, 0, 1000);
                set_button_nomal();
                break;
        }
    }

    int tag = 0;

    public void short_distance() {
        tag = 2;
        alertView = new AlertView(null, getString(R.string.short_distance), getString(R.string.tuichu), new String[]{getString(R.string.goin)}, null, this, AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView.show();
    }

    public void exit() {
        tag = 1;
        alertView = new AlertView(null, getString(R.string.save_map), getString(R.string.canecl), new String[]{getString(R.string.confirm)}, null, this, AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView.show();
    }


    public void set_button_nomal() {
        ivStop.setVisibility(View.VISIBLE);
        tvStop.setVisibility(View.VISIBLE);
        ivJiesu.setVisibility(View.GONE);
        tvJieshu.setVisibility(View.GONE);
        ivGoin.setVisibility(View.GONE);
        tvGoin.setVisibility(View.GONE);
    }

    public void set_button_click() {
        ivStop.setVisibility(View.GONE);
        tvStop.setVisibility(View.GONE);
        ivJiesu.setVisibility(View.VISIBLE);
        tvJieshu.setVisibility(View.VISIBLE);
        ivGoin.setVisibility(View.VISIBLE);
        tvGoin.setVisibility(View.VISIBLE);
    }

    public void set_click(Boolean b) {
        ivStop.setClickable(b);
        tvStop.setClickable(b);
        ivJiesu.setClickable(b);
        tvJieshu.setClickable(b);
        ivGoin.setClickable(b);
        tvGoin.setClickable(b);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ToastUtils.showToast(getString(R.string.click_end), this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDismiss(Object o) {
        if (tag == 2) {
            finish();
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:
                if (tag == 2) {
                    alertView.dismiss();
                    tag = 3;
                    LogUtils.debugInfo("继续22");
                } else {
                    /*mEndTime = System.currentTimeMillis();
                    saveRecord(record.getPathline(), record.getDate());*/
                    saveRecord();
                    finish();
                }
                break;

        }
    }

    //保存数据库
    protected void saveRecord() {
        DbHepler = new DbAdapter(this);
        DbHepler.open();
        String duration = getDuration();
        float distance = getDistance();
        String average = getAverage(distance);
        DbHepler.createrecord(String.valueOf(distance), duration, average,
                "", "", "", record.getDate());
        DbHepler.close();
    }

    private String getDuration() {
        return tvTime.getText().toString().trim();
    }

    private Float getDistance() {
        return Float.parseFloat(tvLength.getText().toString().trim());
    }

    private String getAverage(float distance) {
        Float f = new Float(get_second() / distance);
        int i = f.intValue();
        return ConmonUtils.secToTime(i);
    }

    private int get_second() {
        String time = getDuration();
        String a[] = time.split(":");
        int sec = Integer.parseInt(a[0]) * 60 * 60 + Integer.parseInt(a[1]) * 60 + Integer.parseInt(a[2]);
        return sec;
    }

    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

}