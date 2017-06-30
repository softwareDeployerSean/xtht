package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningIndoorContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.RunningIndoorModel;


@Module
public class RunningIndoorModule {
    private RunningIndoorContract.View view;

    /**
     * 构建RunningIndoorModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RunningIndoorModule(RunningIndoorContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RunningIndoorContract.View provideRunningIndoorView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RunningIndoorContract.Model provideRunningIndoorModel(RunningIndoorModel model) {
        return model;
    }
}