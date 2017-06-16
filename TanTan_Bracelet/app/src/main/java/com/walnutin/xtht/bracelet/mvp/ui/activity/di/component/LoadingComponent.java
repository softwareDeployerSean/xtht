package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.LoadingModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.LoadingActivity;

@ActivityScope
@Component(modules = LoadingModule.class, dependencies = AppComponent.class)
public interface LoadingComponent {
    void inject(LoadingActivity activity);
}