package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportWeekSelectedModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.SportWeekSelectedFragment;

@ActivityScope
@Component(modules = SportWeekSelectedModule.class, dependencies = AppComponent.class)
public interface SportWeekSelectedComponent {
    void inject(SportWeekSelectedFragment fragment);
}