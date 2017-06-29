package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter;

import android.app.Application;

import com.alibaba.fastjson.JSONObject;
import com.inuker.bluetooth.library.search.SearchResult;
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
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.Device;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.MessagePushAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;

import java.util.HashMap;
import java.util.List;


@ActivityScope
public class EquipmentPresenter extends BasePresenter<EquipmentContract.Model, EquipmentContract.View> {

    private static final String TAG = "[TAN][" + EquipmentPresenter.class.getSimpleName() + "]";

    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private EpSearchListAdapter epSearchListAdapter;

    private List<Device> eps = null;

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

    public void hasBound(String mac) {

        mModel.hasBoundObservable(mac)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            //mRootView.showLoading();//显示上拉刷新的进度条
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String baseJson) {
//                        mRootView.hideLoading();
                        LogUtils.debugInfo(TAG + "查询hasBound 成功");
                        LogUtils.debugInfo(TAG + baseJson);

                        mRootView.unBoundBracelet();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        mRootView.hideLoading();
//                        super.onError(e);
                        LogUtils.debugInfo(TAG + "查询hasBound失败");

                        String netMac = DataHelper.getStringSF(MyApplication.getAppContext(), "mac");
                        if(netMac.equals(mac)) {
                            mRootView.unBoundBracelet();
                            return;
                        }
                        mRootView.hasBound(e.getMessage());


                    }


                });
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

    public void bindBracelet(String token, String epMac) {
        HashMap hashMap = new HashMap();
        hashMap.put("braceletMac", epMac);
        hashMap.put("token", token);
        String jsonStr = JSONObject.toJSONString(hashMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonStr);

        mModel.getBraceletObservable(body)
                .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
            //mRootView.showLoading();//显示上拉刷新的进度条
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String baseJson) {
                        //mRootView.hideLoading();
                        LogUtils.debugInfo(TAG + baseJson);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        mRootView.hideLoading();
//                        super.onError(e);
                        LogUtils.debugInfo(TAG + "绑定手环失败");
                    }


                });
    }
}