package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportWeekSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.SportWeekSelectedModel;


@Module
public class SportWeekSelectedModule {
    private SportWeekSelectedContract.View view;

    /**
     * 构建SportWeekSelectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SportWeekSelectedModule(SportWeekSelectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SportWeekSelectedContract.View provideSportWeekSelectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SportWeekSelectedContract.Model provideSportWeekSelectedModel(SportWeekSelectedModel model) {
        return model;
    }
}