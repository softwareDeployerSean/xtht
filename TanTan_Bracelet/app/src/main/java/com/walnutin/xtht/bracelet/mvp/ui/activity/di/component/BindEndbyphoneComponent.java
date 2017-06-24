package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindEndbyphoneModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BindEndbyphoneActivity;

@ActivityScope
@Component(modules = BindEndbyphoneModule.class, dependencies = AppComponent.class)
public interface BindEndbyphoneComponent {
    void inject(BindEndbyphoneActivity activity);
}