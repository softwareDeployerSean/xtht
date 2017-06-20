package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IAlarmDataListener;
import com.veepoo.protocol.model.datas.AlarmData;
import com.veepoo.protocol.model.settings.AlarmSetting;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerClockListComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ClockListModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ClockListPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.ClockListAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ClockListActivity extends BaseActivity<ClockListPresenter> implements ClockListContract.View {

    private static final String TAG = "[TAN][" + ClockListActivity.class.getSimpleName() + "]";

    @BindView(R.id.clock_list_recyclerview)
    public RecyclerView clockListRecyclerView;
    @BindView(R.id.toolbar_right)
    TextView toolbarRight;

    VPOperateManager mVPOperateManager;

    List<AlarmSetting> mAlarmSettingList = new ArrayList<AlarmSetting>();
    ClockListAdapter clockListAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mPresenter.loadClockList();
                    break;
            }
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

        mVPOperateManager = VPOperateManager.getMangerInstance(this);
        mVPOperateManager.readAlarm(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "读取闹钟设置 onResponse i=" + i);
            }
        }, new IAlarmDataListener() {
            @Override
            public void onAlarmDataChangeListener(AlarmData alarmData) {
                String message = "读取闹钟:\n" + alarmData.toString();
                LogUtils.debugInfo(TAG + message);
                if (alarmData != null && alarmData.getAlarmSettingList() != null && alarmData.getAlarmSettingList().size() > 0) {
                    for (int i = 0; i < alarmData.getAlarmSettingList().size(); i++) {
                        mAlarmSettingList.add(alarmData.getAlarmSettingList().get(i));
                    }
                }
                sendMsg(null, 0);
            }
        });

        toolbarRight.setText(getString(R.string.add));
    }

    @OnClick({R.id.toolbar_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right:
                Intent clockAddIntent = new Intent(this, ClockAddActivity.class);
                startActivity(clockAddIntent);
                break;
        }
    }

    @Override
    public void setAdapter() {
        clockListAdapter = new ClockListAdapter(this, mAlarmSettingList);

        clockListAdapter.setmOnSwitchChangedListenerer(new ClockListAdapter.OnSwitchChangedListenerer() {
            @Override
            public void onSwitchOn(int position) {
                updateClock(position, true);
            }

            @Override
            public void onSwitchOff(int position) {
                updateClock(position, false);
            }
        });

        clockListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clockListRecyclerView.setAdapter(clockListAdapter);
    }

    private void updateClock(int position, boolean isOpen) {
        AlarmSetting alarmSetting = new AlarmSetting(4, 40, true);
//        alarmSetting.setOpen(isOpen);
//        alarmSetting.setAlarmTime(5 * 60 + 30);
//        List<AlarmSetting> list = new ArrayList<>();
//        list.add(alarmSetting);
//        LogUtils.debugInfo(TAG + "list=" + list);
//        mVPOperateManager.settingAlarm(new IBleWriteResponse() {
//            @Override
//            public void onResponse(int i) {
//                LogUtils.debugInfo(TAG + "更新闹钟 position=" + position  + ",  onResponse i =" + i);
//            }
//        }, new IAlarmDataListener() {
//            @Override
//            public void onAlarmDataChangeListener(AlarmData alarmData) {
//                String message = "设置闹钟:\n" + alarmData.toString();
//                LogUtils.debugInfo(TAG + message);
//            }
//        }, list);
        if (position == 0) {
            List<AlarmSetting> alarmSettingList = new ArrayList<>(3);

            AlarmSetting alarmSetting1 = new AlarmSetting(14, 10, true);
            AlarmSetting alarmSetting2 = new AlarmSetting(15, 20, true);
            AlarmSetting alarmSetting3 = new AlarmSetting(16, 30, true);

            alarmSettingList.add(alarmSetting1);
            alarmSettingList.add(alarmSetting2);
            alarmSettingList.add(alarmSetting3);


            mVPOperateManager.settingAlarm(new IBleWriteResponse() {
                @Override
                public void onResponse(int i) {
                    LogUtils.debugInfo(TAG + "更新闹钟 position=" + position + ",  onResponse i =" + i);
                }
            }, new IAlarmDataListener() {
                @Override
                public void onAlarmDataChangeListener(AlarmData alarmData) {
                    String message = "设置闹钟:\n" + alarmData.toString();
                    LogUtils.debugInfo(TAG + message);
//                sendMsg(message, 1);
                }
            }, alarmSettingList);
        }else {
            List<AlarmSetting> alarmSettingList = new ArrayList<>(1);

            AlarmSetting alarmSetting1 = new AlarmSetting(1, 10, true);


            alarmSettingList.add(alarmSetting1);


            mVPOperateManager.settingAlarm(new IBleWriteResponse() {
                @Override
                public void onResponse(int i) {
                    LogUtils.debugInfo(TAG + "更新闹钟 position=" + position + ",  onResponse i =" + i);
                }
            }, new IAlarmDataListener() {
                @Override
                public void onAlarmDataChangeListener(AlarmData alarmData) {
                    String message = "设置闹钟:\n" + alarmData.toString();
                    LogUtils.debugInfo(TAG + message);
//                sendMsg(message, 1);
                }
            }, alarmSettingList);
        }

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