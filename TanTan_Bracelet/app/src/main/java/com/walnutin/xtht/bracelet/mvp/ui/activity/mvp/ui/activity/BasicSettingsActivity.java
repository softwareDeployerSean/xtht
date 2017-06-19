package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IFindDeviceDatalistener;
import com.veepoo.protocol.listener.data.ILongSeatDataListener;
import com.veepoo.protocol.listener.data.INightTurnWristeDataListener;
import com.veepoo.protocol.model.datas.FindDeviceData;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.datas.LongSeatData;
import com.veepoo.protocol.model.datas.NightTurnWristeData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.veepoo.protocol.operate.LongSeatOperater;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicItemSupport;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicSettingsMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerBasicSettingsComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BasicSettingsModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BasicSettingsContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.BasicSettingsPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.BasicSettingsAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class BasicSettingsActivity extends BaseActivity<BasicSettingsPresenter> implements BasicSettingsContract.View {

    @BindView(R.id.basic_settings_recyclerview)
    public RecyclerView basicSettingsRecylcerView;

    List<BasicSettingsMenue> basicSettingsMenues;

    BasicSettingsAdapter adapter;

    private static final String TAG = "[TAN][" + BasicSettingsActivity.class.getSimpleName() + "]";
    VPOperateManager mVPOperateManager;
    FunctionSocailMsgData socailMsgData;

    private String initStateStr = "";

    private BasicItemSupport basicItemSupport;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(basicItemSupport.getFanwanModel() == -1
                    || basicItemSupport.getLongSeat() == -1 || basicItemSupport.getMsgPush() == -1) {
                LogUtils.debugInfo(TAG + "------Don't handle all menue return");
                return;
            }
            mPresenter.loadBasicSettingsMenue(basicItemSupport);
        }
    };

    private void sendMsg(String message, int what) {
        if (message == null || message.equals("")) {
            mHandler.sendEmptyMessage(what);
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

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
        basicItemSupport = new BasicItemSupport();
        mVPOperateManager = VPOperateManager.getMangerInstance(this);

        //消息推送
        basicItemSupport.setMsgPush(0);

        //读取夜间转腕 读取
        mVPOperateManager.readNightTurnWriste(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "读取夜间转腕 onResponse=" + i);
            }
        }, new INightTurnWristeDataListener() {
            @Override
            public void onNightTurnWristeDataChange(NightTurnWristeData nightTurnWristeData) {
                String message = "夜间转腕-读取:\n" + nightTurnWristeData.toString();
                LogUtils.debugInfo(TAG + message);
                basicItemSupport.setFanwanModel(nightTurnWristeData.isNightTureWirsteStatusOpen() ? 0 : 1);

                sendMsg(null, 1);
            }
        });

        //久坐模式 读取
        mVPOperateManager.readLongSeat(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "久坐模式 onResponse=" + i);
            }
        }, new ILongSeatDataListener() {
            @Override
            public void onLongSeatDataChange(LongSeatData longSeat) {
                String message = "设置久坐-读取:\n" + longSeat.toString();
                LogUtils.debugInfo(TAG + message);
                boolean isSupport = longSeat.getStatus() == LongSeatOperater.LSStatus.READ_SUCCESS;
                basicItemSupport.setLongSeat(isSupport ? 0 : 1);
                sendMsg(null, 1);
            }
        });

//        //手机该丢读取
//        mVPOperateManager.readFindDevice(new IBleWriteResponse() {
//            @Override
//            public void onResponse(int i) {
//                LogUtils.debugInfo(TAG + "手机该丢读取 onResponse=" + i);
//            }
//        } , new IFindDeviceDatalistener() {
//            @Override
//            public void onFindDevice(FindDeviceData findDeviceData) {
//                String message = "防丢-读取:\n" + findDeviceData.toString();
//                LogUtils.debugInfo(TAG + message);
//                boolean isDeviceFound = false;
//                if(findDeviceData != null) {
//                    isDeviceFound  = true;
//                }
//                basicItemSupport.setFindDevice(isDeviceFound ? 0 : 1);
//                sendMsg(null, 1);
//            }
//        });
    }

    @Override
    public void setAdapter(List<BasicSettingsMenue> settingsMenues) {

        basicItemSupport = new BasicItemSupport();

        basicSettingsMenues = new ArrayList<>();
        for (int i = 0; i < settingsMenues.size(); i++) {
            basicSettingsMenues.add(settingsMenues.get(i));
        }

        adapter = new BasicSettingsAdapter(this, basicSettingsMenues);

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