package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RegistbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.RegistbyPhoneModel;


@Module
public class RegistbyPhoneModule {
    private RegistbyPhoneContract.View view;

    /**
     * 构建RegistbyPhoneModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RegistbyPhoneModule(RegistbyPhoneContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RegistbyPhoneContract.View provideRegistbyPhoneView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RegistbyPhoneContract.Model provideRegistbyPhoneModel(RegistbyPhoneModel model) {
        return model;
    }
}