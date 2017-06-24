package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RegistbyPhoneModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RegistbyPhoneActivity;

@ActivityScope
@Component(modules = RegistbyPhoneModule.class, dependencies = AppComponent.class)
public interface RegistbyPhoneComponent {
    void inject(RegistbyPhoneActivity activity);
}