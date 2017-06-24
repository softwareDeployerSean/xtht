package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.KnownCallContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.KnownCallModel;


@Module
public class KnownCallModule {
    private KnownCallContract.View view;

    /**
     * 构建KnownCallModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public KnownCallModule(KnownCallContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    KnownCallContract.View provideKnownCallView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    KnownCallContract.Model provideKnownCallModel(KnownCallModel model) {
        return model;
    }
}