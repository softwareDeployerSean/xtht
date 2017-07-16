package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportMonthSelectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.SportMonthSelectedFragment;

@ActivityScope
@Component(modules = SportMonthSelectedModule.class, dependencies = AppComponent.class)
public interface SportMonthSelectedComponent {
    void inject(SportMonthSelectedFragment fragment);
}