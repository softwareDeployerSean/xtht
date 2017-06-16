package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.ExerciseModule;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.ExerciseFragment;

@ActivityScope
@Component(modules = ExerciseModule.class, dependencies = AppComponent.class)
public interface ExerciseComponent {
    void inject(ExerciseFragment fragment);
}