package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRunningOutsideComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RunningOutsideModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningOutsideContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RunningOutsidePresenter;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RunningOutsideActivity extends BaseActivity<RunningOutsidePresenter> implements RunningOutsideContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRunningOutsideComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .runningOutsideModule(new RunningOutsideModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_running_outside; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

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


}