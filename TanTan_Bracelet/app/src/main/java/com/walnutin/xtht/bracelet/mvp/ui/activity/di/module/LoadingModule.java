package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.LoadingContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.LoadingModel;


@Module
public class LoadingModule {
    private LoadingContract.View view;

    /**
     * 构建LoadingModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LoadingModule(LoadingContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LoadingContract.View provideLoadingView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LoadingContract.Model provideLoadingModel(LoadingModel model) {
        return model;
    }
}