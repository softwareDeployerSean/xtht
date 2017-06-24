package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;
import android.content.SharedPreferences;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Personal_dataContract;

import java.io.File;
import java.util.HashMap;


@ActivityScope
public class Personal_dataPresenter extends BasePresenter<Personal_dataContract.Model, Personal_dataContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public Personal_dataPresenter(Personal_dataContract.Model model, Personal_dataContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public RxErrorHandler getmErrorHandler() {
        return mErrorHandler;
    }


    public void change_data(String nickname, String sex, String birth, int dailyGoals, String height, String weightOfUnit, String weight,String heightofunit) {
        HashMap hashMap = new HashMap();
        hashMap.put("nickname", nickname);
        hashMap.put("sex", sex);
        hashMap.put("birth", birth);
        hashMap.put("dailyGoals", dailyGoals);
        hashMap.put("height", height);
        hashMap.put("weightOfUnit", weightOfUnit);
        hashMap.put("weight", weight);
        hashMap.put("heightOfUnit",heightofunit);
        hashMap.put("token", DataHelper.getStringSF(MyApplication.getAppContext(), "token"));
        String jsonStr = JSONObject.toJSONString(hashMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);
        mModel.change_data(body)
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
                        userBean.setNickname(nickname);
                        userBean.setBirth(birth);
                        userBean.setSex(sex);
                        userBean.setDailyGoals(dailyGoals);
                        userBean.setHeight(height);
                        userBean.setHeightOfUnit(heightofunit);
                        userBean.setWeight(weight);
                        userBean.setWeightOfUnit(weightOfUnit);
                        DataHelper.saveDeviceData(mApplication.getBaseContext(), "UserBean", userBean);
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

    public void post_img() {
        String token = DataHelper.getStringSF(MyApplication.getAppContext(), "token");
        SharedPreferences sharedPreferences = DataHelper.getSharedPerference(MyApplication.getAppContext());
        String path = sharedPreferences.getString(sharedPreferences.getString("username", ""), "");
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        LogUtils.debugInfo("数据是" + file.exists() + file.getName());
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "touxiang.jpg", requestFile);


        mModel.post_img(RequestBody.create(MediaType.parse("multipart/form-data"), token), filePart)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            //mRootView.showLoading();//显示上拉刷新的进度条
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String users) {
                        mRootView.hideLoading();
                        mRootView.showMessage(mApplication.getString(R.string.img_success));
                        UserBean userBean = DataHelper.getDeviceData(mApplication.getBaseContext(), "UserBean");
                        userBean.setAvatar(users);
                        DataHelper.saveDeviceData(mApplication.getBaseContext(), "UserBean", userBean);
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