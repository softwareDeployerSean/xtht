package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.OperationbyRunmapContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.OperationbyRunmapModel;


@Module
public class OperationbyRunmapModule {
    private OperationbyRunmapContract.View view;

    /**
     * 构建OperationbyRunmapModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public OperationbyRunmapModule(OperationbyRunmapContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    OperationbyRunmapContract.View provideOperationbyRunmapView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    OperationbyRunmapContract.Model provideOperationbyRunmapModel(OperationbyRunmapModel model) {
        return model;
    }
}