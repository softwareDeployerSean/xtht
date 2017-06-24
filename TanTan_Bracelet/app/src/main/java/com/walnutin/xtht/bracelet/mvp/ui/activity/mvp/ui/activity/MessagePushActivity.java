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
import com.veepoo.protocol.listener.data.ISocialMsgDataListener;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.datas.PwdData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerMessagePushComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.MessagePushModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.MessagePushContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.MessagePushPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.MessagePushAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MessagePushActivity extends BaseActivity<MessagePushPresenter> implements MessagePushContract.View {

    private static final String TAG = "[TAN][" + MessagePushActivity.class.getSimpleName() + "]";

    @BindView(R.id.ep_messagepush_recyclerview)
    RecyclerView messagePushRecyclerView;

    VPOperateManager mVPOperateManager;

    FunctionSocailMsgData socailMsgData = null;

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
//        VPOperateManager.getMangerInstance(this).readSocialMsg(writeResponse, new ISocialMsgDataListener() {
//            @Override
//            public void onSocialMsgSupportDataChange(FunctionSocailMsgData socailMsgData) {
//                String message = " 社交信息提醒-读取:\n" + socailMsgData.toString();
////                Logger.t(TAG).i(message);
//                LogUtils.debugInfo(TAG + message);
//                sendMsg(message, -1);
//            }
//        });
    }
    WriteResponse writeResponse = new WriteResponse();
    class WriteResponse implements IBleWriteResponse {

        @Override
        public void onResponse(int code) {
//            Logger.t(TAG).i("write cmd status:" + code);
            LogUtils.debugInfo(TAG + "readSocialMsg failed ");

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

    public void updateSocailMsgData(int pos,  EFunctionStatus state) {
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
                break;
            case 5:
                //其它，暂时不知道对应哪一个
                break;
        }
        LogUtils.debugInfo(TAG + "socailMsgData=" + socailMsgData);
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