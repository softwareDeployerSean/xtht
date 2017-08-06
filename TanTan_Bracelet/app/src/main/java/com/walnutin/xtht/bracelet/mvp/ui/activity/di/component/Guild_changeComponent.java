package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.Guild_changeModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.Guild_changeActivity;

@ActivityScope
@Component(modules = Guild_changeModule.class, dependencies = AppComponent.class)
public interface Guild_changeComponent {
    void inject(Guild_changeActivity activity);
}