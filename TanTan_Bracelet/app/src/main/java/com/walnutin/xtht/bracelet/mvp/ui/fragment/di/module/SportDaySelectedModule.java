package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportDaySelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.SportDaySelectedModel;


@Module
public class SportDaySelectedModule {
    private SportDaySelectedContract.View view;

    /**
     * 构建SportDaySelectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SportDaySelectedModule(SportDaySelectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SportDaySelectedContract.View provideSportDaySelectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SportDaySelectedContract.Model provideSportDaySelectedModel(SportDaySelectedModel model) {
        return model;
    }
}