package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class EquipmentModel extends BaseModel implements EquipmentContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public EquipmentModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public List<Epuipment> searchEpList() {
        List<Epuipment> epList = null;
        Epuipment ep = null;
        epList = new ArrayList<Epuipment>();
        for(int i = 0; i < 1; i++) {
            ep = new Epuipment();
            ep.setName("第" + (i + 1) + "个手环");
            epList.add(ep);
        }
        return epList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}