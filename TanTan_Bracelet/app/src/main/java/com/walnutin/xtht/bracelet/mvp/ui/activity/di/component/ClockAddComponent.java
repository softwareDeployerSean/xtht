package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ClockAddModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ClockAddActivity;

@ActivityScope
@Component(modules = ClockAddModule.class, dependencies = AppComponent.class)
public interface ClockAddComponent {
    void inject(ClockAddActivity activity);
}