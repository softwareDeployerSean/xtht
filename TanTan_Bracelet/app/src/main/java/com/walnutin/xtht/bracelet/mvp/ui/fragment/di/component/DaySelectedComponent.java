package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.DaySelectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.DaySelectedFragment;

@ActivityScope
@Component(modules = DaySelectedModule.class, dependencies = AppComponent.class)
public interface DaySelectedComponent {
    void inject(DaySelectedFragment fragment);
}