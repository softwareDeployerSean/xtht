package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IBatteryDataListener;
import com.veepoo.protocol.model.datas.BatteryData;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerEpConnectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.EpConnectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.EpConnectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.EpConnectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpConnectedMenueAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomGridLayoutManager;
import com.zhy.autolayout.AutoRelativeLayout;


import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class EpConnectedActivity extends BaseActivity<EpConnectedPresenter> implements EpConnectedContract.View {

    @BindView(R.id.ep_connected_menue)
    public RecyclerView epMenue;
    @BindView(R.id.ep_connected_name)
    public TextView epNameTextView;
    @BindView(R.id.ep_connected_status)
    public TextView epStateTextView;
    @BindView(R.id.ep_connected_dianliang)
    public TextView epPower;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            epPower.setText(message);
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerEpConnectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .epConnectedModule(new EpConnectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_ep_connected; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.loadMenue();

        Intent intent = getIntent();
        SearchResult searchResult = intent.getParcelableExtra("searchResult");
        if(searchResult != null) {
            epNameTextView.setText(searchResult.getName());
            SharedPreferences sharedPreferences = getSharedPreferences("bracelet", MODE_PRIVATE);
            String address = sharedPreferences.getString("connected_address", "null");
            if(address.equals(searchResult.getAddress())) {
                epStateTextView.setText(getResources().getString(R.string.ep_connected));
            }else {
                epStateTextView.setText(getResources().getString(R.string.ep_not_connected));
            }
            VPOperateManager mVpoperateManager = VPOperateManager.getMangerInstance(BaseApplication.getAppContext());
            mVpoperateManager.readBattery(new IBleWriteResponse(){
                @Override
                public void onResponse(int i) {

                }
            }, new IBatteryDataListener() {
                @Override
                public void onDataChange(BatteryData batteryData) {
                    String message = batteryData.getBatteryLevel() * 25 + "%";
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = message;
                    mHandler.sendMessage(msg);
                }
            });
        }else {
            epNameTextView.setText(getResources().getString(R.string.default_ep_name));
            epStateTextView.setText(getResources().getString(R.string.ep_connected));
        }

    }

    @Override
    public void setAdapter(EpConnectedMenueAdapter adapter) {
        epMenue.setLayoutManager(new CustomGridLayoutManager(this, 3, false));
        epMenue.setAdapter(adapter);

        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent callIntent = new Intent(EpConnectedActivity.this, KnownCallActivity.class);
                        startActivity(callIntent);
                        break;
                    case 1:
                        Intent clockIntent = new Intent(EpConnectedActivity.this, ClockListActivity.class);
                        startActivity(clockIntent);
                        break;
                    case 2:
                        Intent messageIntent = new Intent(EpConnectedActivity.this, MessagePushActivity.class);
                        startActivity(messageIntent);
                        break;
                    case 3:
                        Intent questionIntent = new Intent(EpConnectedActivity.this, QuestionHandlerActivity.class);
                        startActivity(questionIntent);
                        break;
                    case 4:
                        Intent updateIntent = new Intent(EpConnectedActivity.this, HardUpdateActivity.class);
                        startActivity(updateIntent);
                        break;
                }
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
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



    @OnClick({R.id.ep_connected_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ep_connected_rl:
                Intent basicSettingsIntent = new Intent(this, BasicSettingsActivity.class);
                startActivity(basicSettingsIntent);
                break;
        }
    }




}