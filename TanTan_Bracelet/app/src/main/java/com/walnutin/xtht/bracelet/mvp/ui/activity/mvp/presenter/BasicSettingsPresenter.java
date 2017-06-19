package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.BasicItemSupport;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicSettingsMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BasicSettingsContract;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.BasicSettingsAdapter;

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