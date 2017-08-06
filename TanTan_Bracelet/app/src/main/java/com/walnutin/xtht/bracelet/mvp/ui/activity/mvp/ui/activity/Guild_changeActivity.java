package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.GuildActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerGuild_changeComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.Guild_changeModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Guild_changeContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.Guild_changePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.LoadingPresenter;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class Guild_changeActivity extends BaseActivity<Guild_changePresenter> implements Guild_changeContract.View {


    String isload = "";

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerGuild_changeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .guild_changeModule(new Guild_changeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_guild;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        isload = DataHelper.getStringSF(MyApplication.getAppContext(), "isload");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isload.equals("default")) {
                    startActivity(new Intent(Guild_changeActivity.this, LoadActivity.class));
                    finish();
                } else {
                    if (!DataHelper.getStringSF(MyApplication.getAppContext(), "username").equals("default") && !DataHelper.getStringSF(MyApplication.getAppContext(), "userpassword").equals("default")) {
                        LogUtils.debugInfo("取出来"+DataHelper.getStringSF(MyApplication.getAppContext(), "userpassword"));
                        mPresenter.load(DataHelper.getStringSF(MyApplication.getAppContext(), "username"), DataHelper.getStringSF(MyApplication.getAppContext(), "userpassword"));
                    }

                }
            }
        }.start();
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

    @Override
    public void load_success() {
        startActivity(new Intent(Guild_changeActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void load_fail() {
        startActivity(new Intent(Guild_changeActivity.this, LoadingActivity.class));
        finish();
    }
}
