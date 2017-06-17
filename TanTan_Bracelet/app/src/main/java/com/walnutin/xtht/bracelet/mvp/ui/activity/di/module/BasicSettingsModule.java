package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BasicSettingsContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.BasicSettingsModel;


@Module
public class BasicSettingsModule {
    private BasicSettingsContract.View view;

    /**
     * 构建BasicSettingsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BasicSettingsModule(BasicSettingsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BasicSettingsContract.View provideBasicSettingsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BasicSettingsContract.Model provideBasicSettingsModel(BasicSettingsModel model) {
        return model;
    }
}