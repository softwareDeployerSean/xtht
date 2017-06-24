package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindbyPhoneContract;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class BindbyPhoneModel extends BaseModel implements BindbyPhoneContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public BindbyPhoneModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public Observable<String> remove_bind(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .unbindAccount(body);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}