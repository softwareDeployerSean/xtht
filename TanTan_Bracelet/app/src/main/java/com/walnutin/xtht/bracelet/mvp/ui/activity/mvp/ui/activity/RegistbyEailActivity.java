package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRegistbyEailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RegistbyEailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RegistbyEailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RegistbyEailPresenter;



import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegistbyEailActivity extends BaseActivity<RegistbyEailPresenter> implements RegistbyEailContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRegistbyEailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registbyEailModule(new RegistbyEailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_registby_eail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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