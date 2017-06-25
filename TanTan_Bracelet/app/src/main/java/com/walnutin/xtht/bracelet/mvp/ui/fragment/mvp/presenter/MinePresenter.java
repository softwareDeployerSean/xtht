package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

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
import okhttp3.ResponseBody;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MineContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


@ActivityScope
public class MinePresenter extends BasePresenter<MineContract.Model, MineContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MinePresenter(MineContract.Model model, MineContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void download_img(String path) {
        String PICTURE_URI = Environment.getExternalStorageDirectory() + "/TanTan/picture/" + System.currentTimeMillis() + "head.jpg";
        mModel.download(path)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            mRootView.showLoading();//显示上拉刷新的进度条
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseBody users) {
                        try {
                            byte2image(users.bytes(), PICTURE_URI);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mRootView.hideLoading();
                        LogUtils.debugInfo("下载成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.hideLoading();
                        super.onError(e);
                    }


                });

    }

    //byte数组到图片
    public void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        try {
            FileOutputStream imageOutput = new FileOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            SharedPreferences sharedPreferences = DataHelper.getSharedPerference(MyApplication.getAppContext());
            String user_photo = sharedPreferences.getString("username", "");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(user_photo, path);
            editor.commit();
            mRootView.down_success();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
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