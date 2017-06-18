package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter;

import android.app.Application;

import com.inuker.bluetooth.library.search.SearchResult;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;

import java.util.List;


@ActivityScope
public class EquipmentPresenter extends BasePresenter<EquipmentContract.Model, EquipmentContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private EpSearchListAdapter epSearchListAdapter;

    private List<SearchResult> eps = null;

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
        eps = mModel.searchEpList();
        mRootView.setAdapter(eps);
        mRootView.setStyle(eps.size());
    }

    public void onDataChanged(SearchResult searchResult) {
//        if (!mListAddress.contains(device.getAddress())) {
//            mListData.add(device);
//            mListAddress.add(device.getAddress());
//        }
//        Collections.sort(mListData, new DeviceCompare());
//        bleConnectAdatpter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public RxErrorHandler getmErrorHandler() {
        return mErrorHandler;
    }

}