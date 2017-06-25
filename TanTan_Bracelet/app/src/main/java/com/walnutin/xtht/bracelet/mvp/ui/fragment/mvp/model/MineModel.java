package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MineContract;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


@ActivityScope
public class MineModel extends BaseModel implements MineContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MineModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public Observable<ResponseBody> download(String path) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .downloadPicFromNet(path);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}