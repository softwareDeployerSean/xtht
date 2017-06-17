package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.Clock;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockListContract;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.ClockListAdapter;

import java.util.List;


@ActivityScope
public class ClockListPresenter extends BasePresenter<ClockListContract.Model, ClockListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public ClockListPresenter(ClockListContract.Model model, ClockListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void loadClockList() {
        List<Clock> clockList = mModel.getClockList();
        ClockListAdapter adapter = new ClockListAdapter(mRootView.getContext(), clockList);
        mRootView.setAdapter(adapter);
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