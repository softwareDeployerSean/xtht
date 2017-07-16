package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MonthSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.MonthSelectedModel;


@Module
public class MonthSelectedModule {
    private MonthSelectedContract.View view;

    /**
     * 构建MonthSelectedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MonthSelectedModule(MonthSelectedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MonthSelectedContract.View provideMonthSelectedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MonthSelectedContract.Model provideMonthSelectedModel(MonthSelectedModel model) {
        return model;
    }
}