package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindEndContract;

import io.reactivex.Observable;


@ActivityScope
public class BindEndModel extends BaseModel implements BindEndContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public BindEndModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public Observable<String> get_code(String email) {
          return mRepositoryManager.obtainRetrofitService(UserService.class)
                .sendcode(email);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}