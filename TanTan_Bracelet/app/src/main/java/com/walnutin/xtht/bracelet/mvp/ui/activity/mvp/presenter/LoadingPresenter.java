package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.alibaba.fastjson.JSONObject;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.LoadingContract;

import java.util.HashMap;


@ActivityScope
public class LoadingPresenter extends BasePresenter<LoadingContract.Model, LoadingContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public LoadingPresenter(LoadingContract.Model model, LoadingContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }


    public void load(String name, String password) {

        HashMap hashMap = new HashMap();
        hashMap.put("userName", name);
        hashMap.put("password", password);
        String jsonStr = JSONObject.toJSONString(hashMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        mModel.get_registdata(body)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            mRootView.showLoading();//显示上拉刷新的进度条
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<UserBean>(mErrorHandler) {
                    @Override
                    public void onNext(UserBean users) {
                        mRootView.hideLoading();
                        DataHelper.saveDeviceData(MyApplication.getAppContext(), "UserBean", users);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "username", name);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "token", users.getToken());
                        mRootView.load_success();
                        //mRootView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.hideLoading();
                        super.onError(e);
                    }


                });

    }







    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}