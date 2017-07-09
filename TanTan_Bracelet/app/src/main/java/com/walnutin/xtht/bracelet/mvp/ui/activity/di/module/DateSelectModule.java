package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.DateSelectContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.DateSelectModel;


@Module
public class DateSelectModule {
    private DateSelectContract.View view;

    /**
     * 构建DateSelectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DateSelectModule(DateSelectContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DateSelectContract.View provideDateSelectView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DateSelectContract.Model provideDateSelectModel(DateSelectModel model) {
        return model;
    }
}