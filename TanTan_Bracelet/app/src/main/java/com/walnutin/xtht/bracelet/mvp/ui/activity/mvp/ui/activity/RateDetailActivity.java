package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRateDetailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RateDetailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RateDetailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RateDetailPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.SleepAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CanotSlidingViewpager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RateDetailActivity extends BaseActivity<RateDetailPresenter> implements RateDetailContract.View {


    @BindView(R.id.recycle)
    RecyclerView recycle;
    SleepAdapter sleepAdapter;
    List<String> list = new ArrayList<>();
    Context context;
    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRateDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .rateDetailModule(new RateDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_rate_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        context=this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycle.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 12; i++) {
            list.add("0" + i);
        }
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycle.measure(w, h);
        LogUtils.debugInfo("宽" + recycle.getMeasuredWidth() + "搞" + recycle.getMeasuredHeight());


        ViewTreeObserver viewTreeObserver = recycle.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recycle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LogUtils.debugInfo("是不是" + recycle.getHeight() + "==" + recycle.getWidth());
                sleepAdapter = new SleepAdapter(list,context, recycle.getWidth());
                recycle.setAdapter(sleepAdapter);
            }
        });


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