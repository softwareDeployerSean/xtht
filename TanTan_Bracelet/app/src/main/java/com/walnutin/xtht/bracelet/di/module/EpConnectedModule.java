package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.EpConnectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.EpConnectedModel;


@Module
public class EpConnectedModule {
    private EpConnectedContract.View view;

    /**
     * 构建EpConnectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public EpConnectedModule(EpConnectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    EpConnectedContract.View provideEpConnectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    EpConnectedContract.Model provideEpConnectedModel(EpConnectedModel model) {
        return model;
    }
}