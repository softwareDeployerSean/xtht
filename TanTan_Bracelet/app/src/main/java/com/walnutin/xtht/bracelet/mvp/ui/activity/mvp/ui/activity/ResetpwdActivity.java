package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerResetpwdComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ResetpwdModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetpwdContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ResetpwdPresenter;


import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ResetpwdActivity extends BaseActivity<ResetpwdPresenter> implements ResetpwdContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerResetpwdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .resetpwdModule(new ResetpwdModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_resetpwd; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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

    @OnClick({R.id.bt_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ok:
                launchActivity(new Intent(ResetpwdActivity.this, MainActivity.class));
                break;
        }
    }

}