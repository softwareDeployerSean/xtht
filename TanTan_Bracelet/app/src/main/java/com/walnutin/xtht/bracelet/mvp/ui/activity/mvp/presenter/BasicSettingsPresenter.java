package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.alibaba.fastjson.JSONObject;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicItemSupport;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicSettingsMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BasicSettingsContract;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.BasicSettingsAdapter;

import java.util.HashMap;
import java.util.List;


@ActivityScope
public class BasicSettingsPresenter extends BasePresenter<BasicSettingsContract.Model, BasicSettingsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public BasicSettingsPresenter(BasicSettingsContract.Model model, BasicSettingsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void unbindBracelet(String token) {
        HashMap hashMap = new HashMap();
        hashMap.put("token", token);
//        String jsonStr = JSONObject.toJSONString(hashMap);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        String jsonStr = JSONObject.toJSONString(token);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        LogUtils.debugInfo(TAG + "------------token=" + token);
        mModel.getUnBindBraceletObservable(token)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            //mRootView.showLoading();//显示上拉刷新的进度条
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String baseJson) {
                        //mRootView.hideLoading();
                        LogUtils.debugInfo(TAG + "解绑手环成功");
                        LogUtils.debugInfo(TAG + baseJson);
                        mRootView.unBindSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        mRootView.hideLoading();
                        super.onError(e);
                        LogUtils.debugInfo(TAG + "解绑手环失败");
                    }
                });
    }

    public void loadBasicSettingsMenue(BasicItemSupport basicItemSupport) {
        List<BasicSettingsMenue> basicSettingsMenues = mModel.getBasicSettingsMenues(basicItemSupport);

        mRootView.setAdapter(basicSettingsMenues);
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