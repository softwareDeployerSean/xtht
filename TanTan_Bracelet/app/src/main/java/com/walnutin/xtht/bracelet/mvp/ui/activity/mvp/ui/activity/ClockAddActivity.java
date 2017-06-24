package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerClockAddComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ClockAddModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockAddContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ClockAddPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.WeekAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.PickerView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ClockAddActivity extends BaseActivity<ClockAddPresenter> implements ClockAddContract.View {

    @BindView(R.id.clock_add_hour_pv)
    PickerView hour_pv;
    @BindView(R.id.clock_add_minute_pv)
    PickerView minute_pv;
    @BindView(R.id.clock_add_week_recyclerview)
    RecyclerView weekRecyclerView;

    @BindView(R.id.toolbar_right)
    TextView toolbarRight;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerClockAddComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .clockAddModule(new ClockAddModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_clock_add; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @OnClick({R.id.toolbar_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right:
                break;
        }
    }


    @Override
    public void setAdapter(WeekAdapter adapter) {
        weekRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weekRecyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, R.drawable.divider_mileage));
        weekRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        toolbarRight.setText(getString(R.string.ok));

        List<String> hours = new ArrayList<String>();
        List<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            hours.add(i + "");
        }
        for (int i = 0; i < 60; i++) {
            minutes.add(i < 10 ? "0" + i : "" + i);
        }

        hour_pv.setData(hours);
        hour_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                Toast.makeText(ClockAddActivity.this, "选择了 " + text + " 秒",
                        Toast.LENGTH_SHORT).show();
            }
        });
        hour_pv.setSelected(0);

        minute_pv.setData(minutes);
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                Toast.makeText(ClockAddActivity.this, "选择了 " + text + " 分",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mPresenter.setWeeks();

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