package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RegistbyEailModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RegistbyEailActivity;

@ActivityScope
@Component(modules = RegistbyEailModule.class, dependencies = AppComponent.class)
public interface RegistbyEailComponent {
    void inject(RegistbyEailActivity activity);
}