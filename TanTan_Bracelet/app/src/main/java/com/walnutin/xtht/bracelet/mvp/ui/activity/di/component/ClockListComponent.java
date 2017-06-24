package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ClockListModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ClockListActivity;

@ActivityScope
@Component(modules = ClockListModule.class, dependencies = AppComponent.class)
public interface ClockListComponent {
    void inject(ClockListActivity activity);
}