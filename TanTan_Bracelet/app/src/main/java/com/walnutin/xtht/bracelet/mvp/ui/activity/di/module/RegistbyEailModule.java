package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RegistbyEailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.RegistbyEailModel;


@Module
public class RegistbyEailModule {
    private RegistbyEailContract.View view;

    /**
     * 构建RegistbyEailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RegistbyEailModule(RegistbyEailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RegistbyEailContract.View provideRegistbyEailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RegistbyEailContract.Model provideRegistbyEailModel(RegistbyEailModel model) {
        return model;
    }
}