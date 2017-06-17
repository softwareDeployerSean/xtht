package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindEndModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BindEndActivity;

@ActivityScope
@Component(modules = BindEndModule.class, dependencies = AppComponent.class)
public interface BindEndComponent {
    void inject(BindEndActivity activity);
}