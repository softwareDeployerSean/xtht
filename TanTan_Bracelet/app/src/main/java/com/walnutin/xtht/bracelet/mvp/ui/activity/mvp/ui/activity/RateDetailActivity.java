package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.walnutin.xtht.bracelet.mvp.ui.widget.RateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RateDetailActivity extends BaseActivity<RateDetailPresenter> implements RateDetailContract.View {


    @BindView(R.id.recycle)
    RecyclerView recycle;
    SleepAdapter sleepAdapter;
    List<String> list = new ArrayList<>();
    Context context;

    @BindView(R.id.heart_rate_detail_view)
    RateView heartRateView;

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
        setHeartRateView();
    }

    private void setHeartRateView() {
        heartRateView.setLineColor(Color.parseColor("#6AD489"));
        heartRateView.setBrokenLineColor(Color.parseColor("#8CC44A"));

        String[] yLables = new String[150];
        for (int i = 0; i < yLables.length; i++) {
            yLables[i] = String.valueOf(i);
        }

        heartRateView.setYlabel(yLables);

        String[] xLabels = new String[24];
        int[] datas = new int[24];

        for(int i = 0; i < 24; i++) {
            xLabels[i] = String.valueOf(i);
            datas[i] = new Random().nextInt(120)%(120-80+1) + 80;
        }

        heartRateView.setDatas(datas);
        heartRateView.setXlabel(xLabels);

        int max = getMaxArray(datas);
        int min = getMinArray(datas);

        int linMax = ((int) ((max + 20) / 5)) * 5 + 5;

        String[] yLabels = new String[linMax + linMax / 5];
        for (int i = 0; i < yLabels.length; i++) {
            yLabels[i] = String.valueOf(i);
        }
        heartRateView.setYlabel(yLabels);
        heartRateView.setxDisplayInterval(2);

        int a = linMax / 4;

        int[] brokenLineDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
        heartRateView.setBrolenLineDisplay(brokenLineDisplay);

        int[] yDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
        heartRateView.setyDisPlay(yDisplay);
    }

    private int getMaxArray(int[] array) {
        int i, min, max;

        min = max = array[0];
        System.out.print("数组A的元素包括：");
        for (i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if (array[i] > max)   // 判断最大值
                max = array[i];
            if (array[i] < min)   // 判断最小值
                min = array[i];
        }
        return max;
    }

    private int getMinArray(int[] array) {
        int i, min, max;

        min = max = array[0];
        System.out.print("数组A的元素包括：");
        for (i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if (array[i] > max)   // 判断最大值
                max = array[i];
            if (array[i] < min)   // 判断最小值
                min = array[i];
        }
        return min;
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