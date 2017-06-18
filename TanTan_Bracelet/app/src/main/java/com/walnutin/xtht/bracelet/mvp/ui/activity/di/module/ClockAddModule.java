package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockAddContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ClockAddModel;


@Module
public class ClockAddModule {
    private ClockAddContract.View view;

    /**
     * 构建ClockAddModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ClockAddModule(ClockAddContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ClockAddContract.View provideClockAddView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ClockAddContract.Model provideClockAddModel(ClockAddModel model) {
        return model;
    }
}