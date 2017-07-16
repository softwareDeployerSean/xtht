package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.MonthSelectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.MonthSelectedFragment;

@ActivityScope
@Component(modules = MonthSelectedModule.class, dependencies = AppComponent.class)
public interface MonthSelectedComponent {
    void inject(MonthSelectedFragment fragment);
}