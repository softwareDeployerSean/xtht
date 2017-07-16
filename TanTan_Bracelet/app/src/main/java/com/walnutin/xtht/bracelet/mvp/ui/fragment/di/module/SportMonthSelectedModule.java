package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportMonthSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.SportMonthSelectedModel;


@Module
public class SportMonthSelectedModule {
    private SportMonthSelectedContract.View view;

    /**
     * 构建SportMonthSelectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SportMonthSelectedModule(SportMonthSelectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SportMonthSelectedContract.View provideSportMonthSelectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SportMonthSelectedContract.Model provideSportMonthSelectedModel(SportMonthSelectedModel model) {
        return model;
    }
}