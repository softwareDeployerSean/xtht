package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.alibaba.fastjson.JSONObject;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.widget.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindbyPhoneContract;

import java.util.HashMap;


@ActivityScope
public class BindbyPhonePresenter extends BasePresenter<BindbyPhoneContract.Model, BindbyPhoneContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public BindbyPhonePresenter(BindbyPhoneContract.Model model, BindbyPhoneContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void unbind(String number) {
        HashMap hashMap = new HashMap();
        if (ConmonUtils.isMobileNO(number)) {
            hashMap.put("phone", number);
        } else if (ConmonUtils.checkEmail(number)) {
            hashMap.put("email", number);
        }
        hashMap.put("token", DataHelper.getStringSF(mApplication, "token"));
        String jsonStr = JSONObject.toJSONString(hashMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        mModel.remove_bind(body)
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
                        UserBean userBean = DataHelper.getDeviceData(mApplication.getBaseContext(), "UserBean");
                        if (ConmonUtils.isMobileNO(number)) {
                            userBean.setPhone("");
                        } else if (ConmonUtils.checkEmail(number)) {
                            userBean.setEmail("");
                        }
                        DataHelper.saveDeviceData(mApplication.getBaseContext(), "UserBean", userBean);
                        mRootView.bind_success();
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