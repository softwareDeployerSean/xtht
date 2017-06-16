package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.EquipmentModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.EquipmentFragment;

@ActivityScope
@Component(modules = EquipmentModule.class, dependencies = AppComponent.class)
public interface EquipmentComponent {
    void inject(EquipmentFragment fragment);
}