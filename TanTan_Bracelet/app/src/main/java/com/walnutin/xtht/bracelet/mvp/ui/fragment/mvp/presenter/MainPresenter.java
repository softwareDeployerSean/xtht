package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter;

import android.app.Application;

import com.alibaba.fastjson.JSON;
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

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MainContract;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }


    public void getBindBracelet(String token) {
        mModel.getBindBraceletObservable(token)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            //mRootView.showLoading();//显示上拉刷新的进度条
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String json) {
                        //mRootView.hideLoading();
                        JSONObject jo = JSON.parseObject(json);
                        String mac = (String) jo.get("braceletMac");
                        LogUtils.debugInfo(TAG + "查询绑定的手环成功，mac=" + mac);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "mac", mac);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.debugInfo(TAG + "查询绑定的手环失败");
                    }
                });

    }

    public RxErrorHandler getmErrorHandler() {
        return mErrorHandler;
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