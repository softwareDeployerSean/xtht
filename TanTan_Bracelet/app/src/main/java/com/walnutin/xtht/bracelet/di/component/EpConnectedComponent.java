package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.EpConnectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.EpConnectedActivity;

@ActivityScope
@Component(modules = EpConnectedModule.class, dependencies = AppComponent.class)
public interface EpConnectedComponent {
    void inject(EpConnectedActivity activity);
}