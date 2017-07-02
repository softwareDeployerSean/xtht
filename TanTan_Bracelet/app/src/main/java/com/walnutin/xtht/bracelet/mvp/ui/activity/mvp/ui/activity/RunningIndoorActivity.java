package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRunningIndoorComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RunningIndoorModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningIndoorContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RunningIndoorPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomerRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RunningIndoorActivity extends BaseActivity<RunningIndoorPresenter> implements RunningIndoorContract.View {

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


    @OnClick({R.id.iv_jiesu, R.id.iv_stop, R.id.iv_goin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_jiesu:
                break;
            case R.id.iv_stop:
                set_button_click();
                break;
            case R.id.iv_goin:
                set_button_nomal();
                break;
        }
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

}