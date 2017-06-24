package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Personal_dataContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.Personal_dataModel;


@Module
public class Personal_dataModule {
    private Personal_dataContract.View view;

    /**
     * 构建Personal_dataModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public Personal_dataModule(Personal_dataContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    Personal_dataContract.View providePersonal_dataView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    Personal_dataContract.Model providePersonal_dataModel(Personal_dataModel model) {
        return model;
    }
}