package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.WeekSelectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.WeekSelectedFragment;

@ActivityScope
@Component(modules = WeekSelectedModule.class, dependencies = AppComponent.class)
public interface WeekSelectedComponent {
    void inject(WeekSelectedFragment fragment);
}