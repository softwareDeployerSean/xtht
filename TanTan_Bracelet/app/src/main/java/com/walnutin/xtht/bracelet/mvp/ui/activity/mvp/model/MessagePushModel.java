package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.walnutin.xtht.bracelet.mvp.model.entity.MarginMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.MessagePushContract;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class MessagePushModel extends BaseModel implements MessagePushContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MessagePushModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public List<MarginMenue> getMenueData(FunctionSocailMsgData socailMsgData) {
        List<MarginMenue> msgMenues = new ArrayList<>();

        MarginMenue menue1 = new MarginMenue(1, "Facebook", "", false, 0, socailMsgData.getFacebook() == EFunctionStatus.SUPPORT || socailMsgData.getFacebook() == EFunctionStatus.SUPPORT_OPEN ? true : false);
        msgMenues.add(menue1);

        MarginMenue menue2 = new MarginMenue(2, "Twitter", "", false, 0, socailMsgData.getTwitter() == EFunctionStatus.SUPPORT || socailMsgData.getTwitter() == EFunctionStatus.SUPPORT_OPEN ? true : false);
        msgMenues.add(menue2);

        MarginMenue menue3 = new MarginMenue(3, "QQ", "", false, 0, socailMsgData.getQq() == EFunctionStatus.SUPPORT || socailMsgData.getQq() == EFunctionStatus.SUPPORT_OPEN ? true : false);
        msgMenues.add(menue3);

        MarginMenue menue4 = new MarginMenue(4, "微信", "", false, 0, socailMsgData.getWechat() == EFunctionStatus.SUPPORT || socailMsgData.getWechat() == EFunctionStatus.SUPPORT_OPEN ? true : false);
        msgMenues.add(menue4);

        MarginMenue menue5 = new MarginMenue(1, "短信", "", false, 0, socailMsgData.getMsg() == EFunctionStatus.SUPPORT || socailMsgData.getMsg() == EFunctionStatus.SUPPORT_OPEN ? true : false);
        msgMenues.add(menue5);

        MarginMenue menue6 = new MarginMenue(1, "其它", "", true, 150, false);
        msgMenues.add(menue6);
        return msgMenues;
    }
}