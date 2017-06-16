package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;

import java.util.List;


@ActivityScope
public class EquipmentPresenter extends BasePresenter<EquipmentContract.Model, EquipmentContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public EquipmentPresenter(EquipmentContract.Model model, EquipmentContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void searchEpList() {
        List<Epuipment> eps = mModel.searchEpList();
        mRootView.setdApter(new EpSearchListAdapter(eps, mRootView.getContext()));
        mRootView.setStype(eps.size());
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