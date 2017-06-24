package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindbyPhoneModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BindbyPhoneActivity;

@ActivityScope
@Component(modules = BindbyPhoneModule.class, dependencies = AppComponent.class)
public interface BindbyPhoneComponent {
    void inject(BindbyPhoneActivity activity);
}