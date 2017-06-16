package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.EpMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.EpConnectedContract;

import java.util.List;


@ActivityScope
public class EpConnectedModel extends BaseModel implements EpConnectedContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public EpConnectedModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public List<EpMenue> getMenues() {
        List<EpMenue> epMenues = null;
        EpMenue epMenue1 = new EpMenue(1, "laidianshibie.png", "来电识别");
        epMenues.add(epMenue1);

        EpMenue epMenue2 = new EpMenue(2, "richangnaozhong.png", "日常闹钟");
        epMenues.add(epMenue2);

        EpMenue epMenue3 = new EpMenue(3, "xiaoxi.png", "消息推送");
        epMenues.add(epMenue3);

        EpMenue epMenue4 = new EpMenue(4, "wenti.png", "问题诊断");
        epMenues.add(epMenue4);

        EpMenue epMenue5 = new EpMenue(5, "gujiangengxin.png", "固件更新");
        epMenues.add(epMenue5);

        return null;
    }
}