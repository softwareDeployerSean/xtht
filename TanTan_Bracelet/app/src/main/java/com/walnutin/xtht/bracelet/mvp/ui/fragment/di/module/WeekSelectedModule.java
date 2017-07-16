package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.WeekSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.WeekSelectedModel;


@Module
public class WeekSelectedModule {
    private WeekSelectedContract.View view;

    /**
     * 构建WeekSelectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WeekSelectedModule(WeekSelectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WeekSelectedContract.View provideWeekSelectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WeekSelectedContract.Model provideWeekSelectedModel(WeekSelectedModel model) {
        return model;
    }
}