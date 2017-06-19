package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ISocialMsgDataListener;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerKnownCallComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.KnownCallModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.KnownCallContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.KnownCallPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class KnownCallActivity extends BaseActivity<KnownCallPresenter> implements KnownCallContract.View {

    private static final String TAG = "[TAN][" + KnownCallActivity.class.getSimpleName() + "]";
    VPOperateManager mVPOperateManager;
    FunctionSocailMsgData socailMsgData;

    @BindView(R.id.known_call_sv)
    public SwitchView phoneSW;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            EFunctionStatus phone = socailMsgData.getPhone();
            LogUtils.debugInfo(TAG + "phone ==" + phone);
            if(phone == EFunctionStatus.SUPPORT || phone == EFunctionStatus.SUPPORT_OPEN) {
                phoneSW.setState(true);
            }else {
                phoneSW.setState(false);
            }
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerKnownCallComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .knownCallModule(new KnownCallModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_known_call; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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

                sendMsg(null, -1);
            }
        });

        phoneSW.setmOnStateTriggerListener(new SwitchView.OnStateTriggerListener() {
            @Override
            public void triggerOn() {
                LogUtils.debugInfo(TAG + "来电提醒，开关打开");
                socailMsgData.setPhone(EFunctionStatus.SUPPORT_OPEN);
                setSocialMsg();
            }

            @Override
            public void triggerOff() {
                LogUtils.debugInfo(TAG + "来电提醒，开关关闭");
                socailMsgData.setPhone(EFunctionStatus.SUPPORT_CLOSE);
                setSocialMsg();
            }
        });
    }

    private void setSocialMsg() {
        mVPOperateManager.settingSocialMsg(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "来电提醒 设置 onResponse i=" + i);
            }
        }, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData socailMsgData) {
                String message = " 来电提醒 设置:\n" + socailMsgData.toString();
                LogUtils.debugInfo(TAG + message);
            }
        }, socailMsgData);
    }

    private void sendMsg(String message, int what) {
        LogUtils.debugInfo(TAG + "sendMsg message=" + message + ", what=" + what);
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