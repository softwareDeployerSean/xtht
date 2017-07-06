package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IDeviceFuctionDataListener;
import com.veepoo.protocol.listener.data.IPwdDataListener;
import com.veepoo.protocol.listener.data.ISocialMsgDataListener;
import com.veepoo.protocol.model.datas.FunctionDeviceSupportData;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.datas.PwdData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.veepoo.protocol.model.enums.ESocailMsg;
import com.veepoo.protocol.model.settings.ContentSetting;
import com.veepoo.protocol.model.settings.ContentSmsSetting;
import com.veepoo.protocol.util.VPLogger;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerMessagePushComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.MessagePushModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.MessagePushContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.MessagePushPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.MessagePushAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;


import java.util.logging.Logger;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MessagePushActivity extends BaseActivity<MessagePushPresenter> implements MessagePushContract.View {

    private static final String TAG = "[TAN][" + MessagePushActivity.class.getSimpleName() + "]";

    @BindView(R.id.ep_messagepush_recyclerview)
    RecyclerView messagePushRecyclerView;

    VPOperateManager mVPOperateManager;

    FunctionSocailMsgData socailMsgData = null;

    /**
     * 密码验证获取以下信息
     */
    int watchDataDay = 3;
    int contactMsgLength = 0;
    int allMsgLenght = 4;
    private int deviceNumber = -1;
    private String deviceVersion;
    private String deviceTestVersion;
    boolean isOadModel = false;
    boolean isNewSportCalc = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    mPresenter.loadMenue(socailMsgData);
                    break;
                default:
                    LogUtils.debugInfo(TAG + "Does not match msg.what nothing todo...");
            }
        }
    };

    private void sendMsg(String message, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMessagePushComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .messagePushModule(new MessagePushModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_message_push; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    @Override
    public void setAdapter(MessagePushAdapter adapter) {
        CustomLinearLayoutManager mCustomLinearLayoutManager = new CustomLinearLayoutManager(this);
        mCustomLinearLayoutManager.setScrollEnabled(false);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtils.debugInfo(TAG + "RecyclerView position click");
            }
        });

        messagePushRecyclerView.setLayoutManager(mCustomLinearLayoutManager);
        messagePushRecyclerView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        socailMsgData = new FunctionSocailMsgData();
        mVPOperateManager = VPOperateManager.getMangerInstance(this);
        mVPOperateManager.readSocialMsg(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "readSocialMsg onResponse i = " + i);
            }
        }, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData functionSocailMsgData) {
                String message = " 社交信息提醒-读取:\n" + functionSocailMsgData.toString();
                LogUtils.debugInfo(TAG + message);

                socailMsgData.setPhone(functionSocailMsgData.getPhone());
                socailMsgData.setMsg(functionSocailMsgData.getMsg());
                socailMsgData.setWechat(functionSocailMsgData.getWechat());
                socailMsgData.setQq(functionSocailMsgData.getQq());
                socailMsgData.setFacebook(functionSocailMsgData.getFacebook());
                socailMsgData.setTwitter(functionSocailMsgData.getTwitter());
                socailMsgData.setWhats(functionSocailMsgData.getWhats());
                socailMsgData.setSina(functionSocailMsgData.getSina());
                socailMsgData.setFlickr(functionSocailMsgData.getFlickr());
                socailMsgData.setLinkin(functionSocailMsgData.getLinkin());

                sendMsg(message, -1);
            }
        });

    }

    private void readPara() {
        boolean is24Hourmodel = false;
        mVPOperateManager.confirmDevicePwd(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG, "confirmDevicePwd onResponse i=" + i);
            }
        }, new IPwdDataListener() {
            @Override
            public void onPwdDataChange(PwdData pwdData) {
                String message = "PwdData:\n" + pwdData.toString();
                LogUtils.debugInfo(TAG + "onPwdDataChange" + message);
//                sendMsg(message, 1);

                deviceNumber = pwdData.getDeviceNumber();
                deviceVersion = pwdData.getDeviceVersion();
                deviceTestVersion = pwdData.getDeviceTestVersion();

            }
        }, new IDeviceFuctionDataListener() {
            @Override
            public void onFunctionSupportDataChange(FunctionDeviceSupportData functionSupport) {
                String message = "FunctionDeviceSupportData:\n" + functionSupport.toString();
                LogUtils.debugInfo(TAG + message);
//                sendMsg(message, 2);
                EFunctionStatus newCalcSport = functionSupport.getNewCalcSport();
                if (newCalcSport != null && newCalcSport.equals(EFunctionStatus.SUPPORT)) {
                    isNewSportCalc = true;
                } else {
                    isNewSportCalc = false;
                }
                watchDataDay = functionSupport.getWathcDay();
                contactMsgLength = functionSupport.getContactMsgLength();
                allMsgLenght = functionSupport.getAllMsgLength();
                LogUtils.debugInfo(TAG + "onFunctionSupportDataChange" + message);
//                VPLogger.i("数据读取处理，ORIGIN_DATA_DAY:" + watchDataDay);
            }
        }, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData socailMsgData) {
                String message = "FunctionSocailMsgData:\n" + socailMsgData.toString();
                LogUtils.debugInfo(TAG + message);
//                sendMsg(message, 3);
                LogUtils.debugInfo(TAG + "onSocialMsgSupportDataChange" + message);
            }
        }, "0000", is24Hourmodel);
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

    public void updateSocailMsgData(int pos, EFunctionStatus state) {
        switch (pos) {
            case 0:
                socailMsgData.setFacebook(state);
                break;
            case 1:
                socailMsgData.setTwitter(state);
                break;
            case 2:
                socailMsgData.setQq(state);
                break;
            case 3:
                socailMsgData.setWechat(state);
                break;
            case 4:

                socailMsgData.setMsg(state);
                /**短信，可以只传电话号码**/
                ContentSetting contentsmsSetting0 = new ContentSmsSetting(ESocailMsg.SMS, contactMsgLength, allMsgLenght, "0755-86562490", "公司研发的项目主要在医疗健康智能穿戴、智能家居、新型智能交友产品、飞机航模、智能安全锁五个领域方面");
                /**短信，传联系人姓名以及电话号码，最终显示的联系人姓名**/
                ContentSetting contentsmsSetting1 = new ContentSmsSetting(ESocailMsg.SMS, contactMsgLength, allMsgLenght, "深圳市维亿魄科技有限公司", "0755-86562490", "公司研发的项目主要在医疗健康智能穿戴、智能家居、新型智能交友产品、飞机航模、智能安全锁五个领域方面");
                mVPOperateManager.sendSocialMsgContent(new IBleWriteResponse() {
                    @Override
                    public void onResponse(int i) {
                        LogUtils.debugInfo(TAG + "sendSocialMsgContent onResponse i=" + i);
                    }
                }, contentsmsSetting1);
                break;
            case 5:
                //其它，暂时不知道对应哪一个
                socailMsgData.setOther(state);
                LogUtils.debugInfo(TAG + "socailMsgData=" + socailMsgData);
                break;
        }
        LogUtils.debugInfo(TAG + "------------------------------socailMsgData=" + socailMsgData);
        mVPOperateManager.settingSocialMsg(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "settingSocialMsg onResponse i=" + i);
            }
        }, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData socailMsgData) {
                String message = " 社交信息提醒-设置:\n" + socailMsgData.toString();
                LogUtils.debugInfo(TAG + message);
            }
        }, socailMsgData);
    }

}