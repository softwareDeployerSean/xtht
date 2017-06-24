package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindEndContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.BindEndModel;


@Module
public class BindEndModule {
    private BindEndContract.View view;

    /**
     * 构建BindEndModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BindEndModule(BindEndContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BindEndContract.View provideBindEndView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BindEndContract.Model provideBindEndModel(BindEndModel model) {
        return model;
    }
}