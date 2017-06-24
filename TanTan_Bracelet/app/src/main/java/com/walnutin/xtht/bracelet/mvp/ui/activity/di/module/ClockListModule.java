package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ClockListModel;


@Module
public class ClockListModule {
    private ClockListContract.View view;

    /**
     * 构建ClockListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ClockListModule(ClockListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ClockListContract.View provideClockListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ClockListContract.Model provideClockListModel(ClockListModel model) {
        return model;
    }
}