package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RateDetailModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RateDetailActivity;

@ActivityScope
@Component(modules = RateDetailModule.class, dependencies = AppComponent.class)
public interface RateDetailComponent {
    void inject(RateDetailActivity activity);
}