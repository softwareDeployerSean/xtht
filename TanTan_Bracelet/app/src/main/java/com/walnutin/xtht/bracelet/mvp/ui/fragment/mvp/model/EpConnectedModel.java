package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.EpMenue;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EpConnectedContract;

import java.util.ArrayList;
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
        epMenues = new ArrayList<EpMenue>();
        EpMenue epMenue1 = new EpMenue(1, "来电识别", "laidianshibie.png");
        epMenues.add(epMenue1);

        EpMenue epMenue2 = new EpMenue(2, "日常闹钟", "richangnaozhong.png");
        epMenues.add(epMenue2);

        EpMenue epMenue3 = new EpMenue(3, "消息推送", "xiaoxi.png");
        epMenues.add(epMenue3);

        EpMenue epMenue4 = new EpMenue(4, "问题诊断", "wenti.png");
        epMenues.add(epMenue4);

        EpMenue epMenue5 = new EpMenue(5, "固件更新", "gujiangengxin.png");
        epMenues.add(epMenue5);

        return epMenues;
    }
}