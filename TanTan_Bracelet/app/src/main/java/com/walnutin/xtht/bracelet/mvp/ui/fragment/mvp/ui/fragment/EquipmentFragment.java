package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.EpConnectedActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerEquipmentComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.EquipmentModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.EquipmentPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.DrawableCenterButton;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class EquipmentFragment extends BaseFragment<EquipmentPresenter> implements EquipmentContract.View {

    @BindView(R.id.ep_list_recyclerview)
    public RecyclerView ePListRecyclerView;

    @BindView(R.id.ep_search_btn)
    public DrawableCenterButton epSearchBtn;

    public static EquipmentFragment newInstance() {
        EquipmentFragment fragment = new EquipmentFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerEquipmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .equipmentModule(new EquipmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void setStype(int epSize) {
        int titleBarHeight = 80;
        int epAllSize = 100 * epSize;
        int windowHeight = getWindowHeight();
        LogUtils.debugInfo("TAG windowHeight=" + windowHeight);;
        int layoutHeiht = (windowHeight - titleBarHeight - epAllSize) / 2;

        LogUtils.debugInfo("TAG windowHeight * 0.25=" + windowHeight * 0.25);;
        if(layoutHeiht <= windowHeight * 0.25) {
            layoutHeiht = (int) (windowHeight * 0.23);
        }
        LogUtils.debugInfo("TAG layoutHeiht=" + layoutHeiht);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)epSearchBtn.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, px2dip(getActivity(), layoutHeiht));
        epSearchBtn.setLayoutParams(layoutParams);

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getWindowWidth() {
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public int getWindowHeight() {
        WindowManager wm = getActivity().getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipment, container, false);
    }


    @Override
    public void setdApter(EpSearchListAdapter adapter) {
        ePListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ePListRecyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new EpSearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), EpConnectedActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.searchEpList();
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