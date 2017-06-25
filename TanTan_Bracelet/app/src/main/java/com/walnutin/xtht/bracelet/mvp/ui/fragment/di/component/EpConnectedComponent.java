package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.EpConnectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.EpConnectedFragment;

@ActivityScope
@Component(modules = EpConnectedModule.class, dependencies = AppComponent.class)
public interface EpConnectedComponent {
    void inject(EpConnectedFragment fragment);
}