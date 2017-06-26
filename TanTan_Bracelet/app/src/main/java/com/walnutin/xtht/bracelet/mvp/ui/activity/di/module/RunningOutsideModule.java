package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningOutsideContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.RunningOutsideModel;


@Module
public class RunningOutsideModule {
    private RunningOutsideContract.View view;

    /**
     * 构建RunningOutsideModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RunningOutsideModule(RunningOutsideContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RunningOutsideContract.View provideRunningOutsideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RunningOutsideContract.Model provideRunningOutsideModel(RunningOutsideModel model) {
        return model;
    }
}