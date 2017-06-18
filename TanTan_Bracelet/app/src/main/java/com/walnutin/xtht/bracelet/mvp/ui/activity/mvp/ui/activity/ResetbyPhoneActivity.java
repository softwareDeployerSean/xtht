package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerResetbyPhoneComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ResetbyPhoneModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ResetbyPhonePresenter;


import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ResetbyPhoneActivity extends BaseActivity<ResetbyPhonePresenter> implements ResetbyPhoneContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerResetbyPhoneComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .resetbyPhoneModule(new ResetbyPhoneModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_resetby_phone; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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


    @OnClick({R.id.bt_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                launchActivity(new Intent(ResetbyPhoneActivity.this,ResetpwdActivity.class));
                break;
        }
    }
}