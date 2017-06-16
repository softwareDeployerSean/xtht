package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ResetbyPhoneModel;


@Module
public class ResetbyPhoneModule {
    private ResetbyPhoneContract.View view;

    /**
     * 构建ResetbyPhoneModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ResetbyPhoneModule(ResetbyPhoneContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ResetbyPhoneContract.View provideResetbyPhoneView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ResetbyPhoneContract.Model provideResetbyPhoneModel(ResetbyPhoneModel model) {
        return model;
    }
}