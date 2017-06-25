package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.inuker.bluetooth.library.search.SearchResult;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.Device;
import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


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
    public List<Device> searchEpList() {
        List<Device> epList = null;
        epList = new ArrayList<Device>();
//        for(int i = 0; i < 21; i++) {
//            ep = new SearchResult();
//            ep.setName("第" + (i + 1) + "个手环");
//            epList.add(ep);
//        }
        return epList;
    }

    @Override
    public Observable<String> getBraceletObservable(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .bindBracelet(body);
    }

    @Override
    public Observable<String> hasBoundObservable(String mac) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .hasBound(mac);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}