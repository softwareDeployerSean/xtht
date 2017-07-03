package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RateDetailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.RateDetailModel;


@Module
public class RateDetailModule {
    private RateDetailContract.View view;

    /**
     * 构建RateDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RateDetailModule(RateDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RateDetailContract.View provideRateDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RateDetailContract.Model provideRateDetailModel(RateDetailModel model) {
        return model;
    }
}