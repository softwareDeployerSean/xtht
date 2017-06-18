package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.MessagePushContract;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.MessagePushAdapter;


@ActivityScope
public class MessagePushPresenter extends BasePresenter<MessagePushContract.Model, MessagePushContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MessagePushPresenter(MessagePushContract.Model model, MessagePushContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void loadMenue(FunctionSocailMsgData socailMsgData) {
        MessagePushAdapter adapter = new MessagePushAdapter(mRootView.getContext(), mModel.getMenueData(socailMsgData));
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