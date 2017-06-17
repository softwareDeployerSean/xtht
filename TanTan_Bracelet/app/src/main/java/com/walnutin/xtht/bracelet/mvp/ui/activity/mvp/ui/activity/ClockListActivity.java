package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerClockListComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ClockListModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ClockListPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.ClockListAdapter;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ClockListActivity extends BaseActivity<ClockListPresenter> implements ClockListContract.View {

    @BindView(R.id.clock_list_recyclerview)
    public RecyclerView clockListRecyclerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerClockListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .clockListModule(new ClockListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_clock_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.loadClockList();
    }


    @Override
    public void setAdapter(ClockListAdapter adapter) {
        clockListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clockListRecyclerView.setAdapter(adapter);
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
    public Context getContext() {
        return this;
    }
}