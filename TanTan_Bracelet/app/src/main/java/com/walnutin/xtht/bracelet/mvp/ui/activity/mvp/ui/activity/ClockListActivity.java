package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IAlarmDataListener;
import com.veepoo.protocol.model.datas.AlarmData;
import com.veepoo.protocol.model.datas.LanguageData;
import com.veepoo.protocol.model.settings.AlarmSetting;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerClockListComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ClockListModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ClockListPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.ClockListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.PickerView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    PickerView hour_pv;
    PickerView minute_pv;

    VPOperateManager mVPOperateManager;

    List<AlarmSetting> mAlarmSettingList = new ArrayList<AlarmSetting>();
    ClockListAdapter clockListAdapter;

    AlertView mAlertView;

    Button cancleBtn;
    Button okBtn;

    private int selectedHour;
    private int selectedMin;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    sortClock();
                    mPresenter.loadClockList();
                    break;
                case 1:
                    sortClock();
                    clockListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void sortClock() {
        Collections.sort(mAlarmSettingList, new Comparator<AlarmSetting>() {

            @Override
            public int compare(AlarmSetting a1, AlarmSetting a2) {
                if(!a1.isOpen()) {
                    return -1;
                }
                int i = a1.getAlarmTime() - a2.getAlarmTime();
                if (i == 0) {
                    return 1;
                }
                return i;
            }
        });
    }

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
                showAddClockDialog();
                break;
        }
    }

    private ViewGroup decorView;
    private ViewGroup rootView;
    private ViewGroup contentContainer;
    View contentView;

    private void showAddClockDialog() {
        if (decorView == null) {
            decorView = (ViewGroup) this.getWindow().getDecorView().findViewById(android.R.id.content);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (rootView == null) {
            rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview, decorView, false);
        }

        if (contentContainer == null) {
            contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            contentContainer.setLayoutParams(params);
            contentView = layoutInflater.inflate(R.layout.clock_add_dialog, contentContainer, false);
            contentContainer.addView(contentView);
        }

        if (hour_pv == null) {
            hour_pv = (PickerView) contentView.findViewById(R.id.clock_add_alert_hour_pv);

            List<String> hours = new ArrayList<String>();
            for (int i = 0; i < 24; i++) {
                hours.add(i + "");
            }
            hour_pv.setData(hours);
            hour_pv.setSelected(0);
        }

        if(minute_pv == null) {
            minute_pv = (PickerView) contentView.findViewById(R.id.clock_add_alert_minute_pv);
            List<String> minutes = new ArrayList<String>();
            for (int i = 0; i < 60; i++) {
                minutes.add(i < 10 ? "0" + i : "" + i);
            }
            minute_pv.setData(minutes);
            minute_pv.setSelected(0);
        }


        hour_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                setSelectedHour(Integer.parseInt(text));
            }
        });
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                setSelectedMin(Integer.parseInt(text));
            }
        });

        if(cancleBtn == null) {
            cancleBtn = (Button) contentView.findViewById(R.id.clock_add_cancel_btn);
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disMissDialog();
                }
            });
        }

        if(okBtn == null) {
            okBtn = (Button) contentView.findViewById(R.id.clock_add_ok_btn);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isSupportAdd = false;
                    for(int i = 0; i < mAlarmSettingList.size(); i ++) {
                        AlarmSetting as = mAlarmSettingList.get(i);
                        if(as.getAlarmTime() == 0 && as.isOpen() == false) {
                            mAlarmSettingList.remove(i);
                            isSupportAdd = true;
                            break;
                        }
                    }
                    if(!isSupportAdd) {
                        ToastUtils.showToast("设备最多支持三个闹钟，请删除后再添加", ClockListActivity.this);
                        return;
                    }

                    AlarmSetting addClock = new AlarmSetting(selectedHour, selectedMin, true);
                    mAlarmSettingList.add(addClock);
                    mVPOperateManager.settingAlarm(new IBleWriteResponse() {
                        @Override
                        public void onResponse(int i) {
                            LogUtils.debugInfo(TAG + "添加闹钟 onResponse i =" + i);
                        }
                    }, new IAlarmDataListener() {
                        @Override
                        public void onAlarmDataChangeListener(AlarmData alarmData) {
                            String message = "设置闹钟:\n" + alarmData.toString();
                            LogUtils.debugInfo(TAG + message);

                            mAlarmSettingList.clear();

                            for(int i = 0; i < alarmData.getAlarmSettingList().size(); i++) {
                                mAlarmSettingList.add(alarmData.getAlarmSettingList().get(i));
                            }

                            sendMsg(null, 1);
                            disMissDialog();
                        }
                    }, mAlarmSettingList);

                }
            });
        }

        decorView.addView(rootView);
    }

    private void disMissDialog() {
        decorView.removeView(rootView);
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

            @Override
            public void ondelClick(int position) {
                deleteClock(position);
            }
        });

        clockListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clockListRecyclerView.setAdapter(clockListAdapter);
    }

    private void deleteClock(int position) {
        for (int i = 0; i < mAlarmSettingList.size(); i++) {
            if (position == i) {
                AlarmSetting as = mAlarmSettingList.get(i);
                mAlarmSettingList.remove(i);
                as.setAlarmTime(0);
                as.setOpen(false);
                mAlarmSettingList.add(as);
            }
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
                    sendMsg(message, 1);
                }
            }, mAlarmSettingList);
        }
    }

    private void updateClock(int position, boolean isOpen) {

        for (int i = 0; i < mAlarmSettingList.size(); i++) {
            if (position == i) {
                AlarmSetting as = mAlarmSettingList.get(i);
                mAlarmSettingList.remove(i);
                as.setOpen(isOpen);
                mAlarmSettingList.add(as);
            }
        }

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
                sendMsg(message, 1);
            }
        }, mAlarmSettingList);

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


    public int getSelectedMin() {
        return selectedMin;
    }

    public void setSelectedMin(int selectedMin) {
        this.selectedMin = selectedMin;
    }

    public int getSelectedHour() {
        return selectedHour;
    }

    public void setSelectedHour(int selectedHour) {
        this.selectedHour = selectedHour;
    }
}