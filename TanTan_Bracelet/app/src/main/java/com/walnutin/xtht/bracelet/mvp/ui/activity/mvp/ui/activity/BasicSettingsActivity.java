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
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerBasicSettingsComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BasicSettingsModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BasicSettingsContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.BasicSettingsPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.BasicSettingsAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class BasicSettingsActivity extends BaseActivity<BasicSettingsPresenter> implements BasicSettingsContract.View {

    @BindView(R.id.basic_settings_recyclerview)
    public RecyclerView basicSettingsRecylcerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBasicSettingsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .basicSettingsModule(new BasicSettingsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_basic_settings; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.loadBasicSettingsMenue();
    }

    @Override
    public void setAdapter(BasicSettingsAdapter adapter) {
        basicSettingsRecylcerView.setLayoutManager(new LinearLayoutManager(this));
        basicSettingsRecylcerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, R.drawable.divider_mileage));
        basicSettingsRecylcerView.setAdapter(adapter);
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