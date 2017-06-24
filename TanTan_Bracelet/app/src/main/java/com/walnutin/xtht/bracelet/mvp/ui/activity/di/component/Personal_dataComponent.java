package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.Personal_dataModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.Personal_dataActivity;

@ActivityScope
@Component(modules = Personal_dataModule.class, dependencies = AppComponent.class)
public interface Personal_dataComponent {
    void inject(Personal_dataActivity activity);
}