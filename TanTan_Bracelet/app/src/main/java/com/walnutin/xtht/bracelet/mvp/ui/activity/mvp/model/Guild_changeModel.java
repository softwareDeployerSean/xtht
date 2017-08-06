package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Guild_changeContract;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class Guild_changeModel extends BaseModel implements Guild_changeContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public Guild_changeModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }


    @Override
    public Observable<UserBean> get_registdata(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .login(body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}