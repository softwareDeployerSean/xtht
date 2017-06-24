package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindEndbyphoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.BindEndbyphoneModel;


@Module
public class BindEndbyphoneModule {
    private BindEndbyphoneContract.View view;

    /**
     * 构建BindEndbyphoneModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BindEndbyphoneModule(BindEndbyphoneContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BindEndbyphoneContract.View provideBindEndbyphoneView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BindEndbyphoneContract.Model provideBindEndbyphoneModel(BindEndbyphoneModel model) {
        return model;
    }
}