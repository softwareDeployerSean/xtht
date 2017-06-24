package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BasicSettingsModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BasicSettingsActivity;

@ActivityScope
@Component(modules = BasicSettingsModule.class, dependencies = AppComponent.class)
public interface BasicSettingsComponent {
    void inject(BasicSettingsActivity activity);
}