package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetpwdContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ResetpwdModel;


@Module
public class ResetpwdModule {
    private ResetpwdContract.View view;

    /**
     * 构建ResetpwdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ResetpwdModule(ResetpwdContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ResetpwdContract.View provideResetpwdView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ResetpwdContract.Model provideResetpwdModel(ResetpwdModel model) {
        return model;
    }
}