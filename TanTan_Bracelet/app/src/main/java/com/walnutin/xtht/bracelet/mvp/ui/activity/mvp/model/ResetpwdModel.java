package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetpwdContract;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class ResetpwdModel extends BaseModel implements ResetpwdContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ResetpwdModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public Observable<UserBean> get_checkdata(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .check_password(body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}