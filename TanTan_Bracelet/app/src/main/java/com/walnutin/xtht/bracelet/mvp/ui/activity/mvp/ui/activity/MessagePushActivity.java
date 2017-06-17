package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerMessagePushComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.MessagePushModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.MessagePushContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.MessagePushPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.MessagePushAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MessagePushActivity extends BaseActivity<MessagePushPresenter> implements MessagePushContract.View {

    @BindView(R.id.ep_messagepush_recyclerview)
    RecyclerView messagePushRecyclerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMessagePushComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .messagePushModule(new MessagePushModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_message_push; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }



    @Override
    public void setAdapter(MessagePushAdapter adapter) {
        CustomLinearLayoutManager mCustomLinearLayoutManager = new CustomLinearLayoutManager(this);
        mCustomLinearLayoutManager.setScrollEnabled(false);
        messagePushRecyclerView.setLayoutManager(mCustomLinearLayoutManager);
        messagePushRecyclerView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.loadMenue();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

}