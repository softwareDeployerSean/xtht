package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.KnownCallModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.KnownCallActivity;

@ActivityScope
@Component(modules = KnownCallModule.class, dependencies = AppComponent.class)
public interface KnownCallComponent {
    void inject(KnownCallActivity activity);
}