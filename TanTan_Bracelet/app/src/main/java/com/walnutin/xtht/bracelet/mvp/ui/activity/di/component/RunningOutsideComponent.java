package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RunningOutsideModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningOutsideActivity;

@ActivityScope
@Component(modules = RunningOutsideModule.class, dependencies = AppComponent.class)
public interface RunningOutsideComponent {
    void inject(RunningOutsideActivity activity);
}