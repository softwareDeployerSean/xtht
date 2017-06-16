package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.LoadModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.LoadActivity;

@ActivityScope
@Component(modules = LoadModule.class, dependencies = AppComponent.class)
public interface LoadComponent {
    void inject(LoadActivity activity);
}