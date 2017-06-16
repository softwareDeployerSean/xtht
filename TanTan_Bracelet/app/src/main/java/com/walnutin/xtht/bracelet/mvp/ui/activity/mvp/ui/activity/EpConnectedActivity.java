package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerEpConnectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.EpConnectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.EpConnectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.EpConnectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpConnectedMenueAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomGridLayoutManager;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class EpConnectedActivity extends BaseActivity<EpConnectedPresenter> implements EpConnectedContract.View {

    @BindView(R.id.ep_connected_menue)
    public RecyclerView epMenue;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerEpConnectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .epConnectedModule(new EpConnectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_ep_connected; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.loadMenue();
    }

    @Override
    public void setAdapter(EpConnectedMenueAdapter adapter) {
//        epMenue.setLayoutManager(new CustomGridLayoutManager(this, 3, false));
//        epMenue.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
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