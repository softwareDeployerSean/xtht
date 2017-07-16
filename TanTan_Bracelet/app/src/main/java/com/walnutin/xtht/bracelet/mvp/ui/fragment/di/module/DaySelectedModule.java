package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.DaySelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.DaySelectedModel;


@Module
public class DaySelectedModule {
    private DaySelectedContract.View view;

    /**
     * 构建DaySelectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DaySelectedModule(DaySelectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DaySelectedContract.View provideDaySelectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DaySelectedContract.Model provideDaySelectedModel(DaySelectedModel model) {
        return model;
    }
}