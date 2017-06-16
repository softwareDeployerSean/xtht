package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetbyEmailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ResetbyEmailModel;


@Module
public class ResetbyEmailModule {
    private ResetbyEmailContract.View view;

    /**
     * 构建ResetbyEmailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ResetbyEmailModule(ResetbyEmailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ResetbyEmailContract.View provideResetbyEmailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ResetbyEmailContract.Model provideResetbyEmailModel(ResetbyEmailModel model) {
        return model;
    }
}