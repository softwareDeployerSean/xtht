package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.BindbyPhoneModel;


@Module
public class BindbyPhoneModule {
    private BindbyPhoneContract.View view;

    /**
     * 构建BindbyPhoneModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BindbyPhoneModule(BindbyPhoneContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BindbyPhoneContract.View provideBindbyPhoneView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BindbyPhoneContract.Model provideBindbyPhoneModel(BindbyPhoneModel model) {
        return model;
    }
}