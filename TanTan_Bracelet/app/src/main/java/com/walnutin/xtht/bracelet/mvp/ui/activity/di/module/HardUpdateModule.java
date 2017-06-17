package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.HardUpdateContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.HardUpdateModel;


@Module
public class HardUpdateModule {
    private HardUpdateContract.View view;

    /**
     * 构建HardUpdateModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HardUpdateModule(HardUpdateContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HardUpdateContract.View provideHardUpdateView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HardUpdateContract.Model provideHardUpdateModel(HardUpdateModel model) {
        return model;
    }
}