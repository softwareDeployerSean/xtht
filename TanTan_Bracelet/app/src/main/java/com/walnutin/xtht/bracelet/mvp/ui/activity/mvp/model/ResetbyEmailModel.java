package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetbyEmailContract;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class ResetbyEmailModel extends BaseModel implements ResetbyEmailContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ResetbyEmailModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<String> verify_code(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .validCode(body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}