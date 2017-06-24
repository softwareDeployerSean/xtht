package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ResetbyPhoneModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ResetbyPhoneActivity;

@ActivityScope
@Component(modules = ResetbyPhoneModule.class, dependencies = AppComponent.class)
public interface ResetbyPhoneComponent {
    void inject(ResetbyPhoneActivity activity);
}