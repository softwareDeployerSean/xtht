package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.EpConnectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpConnectedMenueAdapter;


@ActivityScope
public class EpConnectedPresenter extends BasePresenter<EpConnectedContract.Model, EpConnectedContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public EpConnectedPresenter(EpConnectedContract.Model model, EpConnectedContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void loadMenue() {
//        EpConnectedMenueAdapter adapter = new EpConnectedMenueAdapter(mModel.getMenues(), mRootView.getContext());
//        mRootView.setAdapter(adapter);

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