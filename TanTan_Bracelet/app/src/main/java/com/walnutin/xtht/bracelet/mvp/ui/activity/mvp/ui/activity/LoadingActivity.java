package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerLoadingComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.LoadingModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.LoadingContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.LoadingPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;


import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoadingActivity extends BaseActivity<LoadingPresenter> implements LoadingContract.View , OnItemClickListener {

    AlertView alertView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoadingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loadingModule(new LoadingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_loading; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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

    @OnClick({R.id.bt_load, R.id.tv_froget_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_load:
                launchActivity(new Intent(LoadingActivity.this,MainActivity.class));
                break;
            case R.id.tv_froget_pwd://点击保存按钮
                forgetpwd();
                break;
        }
    }


    @Override
    public void forgetpwd() {
        alertView= new AlertView(null, null, null, null, new String[]{getString(R.string.reset_pwdbyphone), getString(R.string.reset_pwdbyemail)}, this, AlertView.Style.ActionSheet, this);
        alertView.show();
    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:
                launchActivity(new Intent(LoadingActivity.this,ResetbyPhoneActivity.class));
                break;
            case 1:
                launchActivity(new Intent(LoadingActivity.this,ResetbyEmailActivity.class));
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
            if(alertView!=null){
                if (alertView.isShowing()){
                    alertView.dismiss();
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}