package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportDaySelectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.SportDaySelectedFragment;

@ActivityScope
@Component(modules = SportDaySelectedModule.class, dependencies = AppComponent.class)
public interface SportDaySelectedComponent {
    void inject(SportDaySelectedFragment fragment);
}