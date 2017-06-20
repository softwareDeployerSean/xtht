package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.veepoo.protocol.model.settings.AlarmSetting;
import com.walnutin.xtht.bracelet.mvp.model.entity.Clock;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockListContract;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class ClockListModel extends BaseModel implements ClockListContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ClockListModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public List<AlarmSetting> getClockList() {
        List<AlarmSetting> clockList = new ArrayList<>();

        return clockList;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}