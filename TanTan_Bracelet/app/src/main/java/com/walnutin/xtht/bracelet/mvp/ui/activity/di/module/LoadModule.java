package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.LoadContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.LoadModel;


@Module
public class LoadModule {
    private LoadContract.View view;

    /**
     * 构建LoadModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LoadModule(LoadContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LoadContract.View provideLoadView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LoadContract.Model provideLoadModel(LoadModel model) {
        return model;
    }
}