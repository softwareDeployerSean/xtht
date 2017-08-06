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
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Guild_changeContract;

import java.util.HashMap;


@ActivityScope
public class Guild_changePresenter extends BasePresenter<Guild_changeContract.Model, Guild_changeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public Guild_changePresenter(Guild_changeContract.Model model, Guild_changeContract.View rootView
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

        LogUtils.debugInfo("进来啊啊啊" + password);
        String jsonStr = JSONObject.toJSONString(hashMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        mModel.get_registdata(body)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<UserBean>(mErrorHandler) {
                    @Override
                    public void onNext(UserBean users) {
                        DataHelper.saveDeviceData(MyApplication.getAppContext(), "UserBean", users);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "username", name);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "userpassword", password);
                        if (ConmonUtils.checkEmail(name)) {
                            DataHelper.setStringSF(MyApplication.getAppContext(), "load_tag", "email");
                        } else {
                            DataHelper.setStringSF(MyApplication.getAppContext(), "load_tag", "phone");
                        }
                        DataHelper.setStringSF(MyApplication.getAppContext(), "token", users.getToken());
                        mRootView.load_success();
                        //mRootView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.load_fail();
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