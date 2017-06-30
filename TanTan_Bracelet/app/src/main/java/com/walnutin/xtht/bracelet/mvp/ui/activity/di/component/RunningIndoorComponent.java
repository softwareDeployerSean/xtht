package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RunningIndoorModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningIndoorActivity;

@ActivityScope
@Component(modules = RunningIndoorModule.class, dependencies = AppComponent.class)
public interface RunningIndoorComponent {
    void inject(RunningIndoorActivity activity);
}