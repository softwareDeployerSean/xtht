package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Personal_dataContract;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@ActivityScope
public class Personal_dataModel extends BaseModel implements Personal_dataContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public Personal_dataModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public Observable<String> change_data(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .change_data(body);
    }

    @Override
    public Observable<String> post_img(RequestBody token, MultipartBody.Part body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .post_img(token,body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}