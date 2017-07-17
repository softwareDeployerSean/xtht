package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerMonthSelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.MonthSelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MonthSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.MonthSelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MonthSelectedFragment extends BaseFragment<MonthSelectedPresenter> implements MonthSelectedContract.View {
    @BindView(R.id.month_hv)
    HistogramView monthHv;

    public static MonthSelectedFragment newInstance() {
        MonthSelectedFragment fragment = new MonthSelectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMonthSelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .monthSelectedModule(new MonthSelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);

        String[] xLables = new String[maxDate];
        for(int i = 0; i < maxDate; i++) {
            xLables[i] = String.valueOf(i + 1);
        }

        int[] datas = new int[xLables.length];
        for (int i = 0; i < datas.length; i++) {
            int r = new Random().nextInt(100);
            datas[i] = r;
        }

        monthHv.setxLables(xLables);
        monthHv.setDatas(datas);
        monthHv.setIntervalPercent(0.7f);
        Log.d("TAG", "Color.RED=" + Color.RED);
        monthHv.setStartColor(Color.parseColor("#6B289B"));
        monthHv.setEndColor(Color.parseColor("#D0B3EB"));
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

}