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
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningOutsideContract;

import java.util.HashMap;


@ActivityScope
public class RunningOutsidePresenter extends BasePresenter<RunningOutsideContract.Model, RunningOutsideContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public RunningOutsidePresenter(RunningOutsideContract.Model model, RunningOutsideContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void post_sportdata(String distance, String duration, String average, String pathlineSring, String stratpoint, String endpoint, String time, String calorie, String height, String tag_title,String steprate,String heartrate) {
        HashMap hashMap = new HashMap();
        hashMap.put("averagespeed", average);
        hashMap.put("date", time);
        hashMap.put("distance", distance);
        hashMap.put("duration", duration);
        hashMap.put("endpoint", endpoint);
        hashMap.put("pathline", pathlineSring);
        hashMap.put("startpoint", stratpoint);
        hashMap.put("token", DataHelper.getStringSF(MyApplication.getAppContext(), "token"));
        hashMap.put("calorie", calorie);
        hashMap.put("altitude", height);
        hashMap.put("sign", tag_title);
        hashMap.put("steprate", steprate);
        hashMap.put("heartrate", heartrate);

        String jsonStr = JSONObject.toJSONString(hashMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        mModel.sportData(body)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            mRootView.showLoading();//显示上拉刷新的进度条
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String users) {
                        mRootView.hideLoading();
                        mRootView.showMessage(users);
                        mRootView.post_success();
                        //mRootView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.hideLoading();
                        super.onError(e);
                        LogUtils.debugInfo("load_p" + e.toString());
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