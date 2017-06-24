package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.QuestionHandlerModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.QuestionHandlerActivity;

@ActivityScope
@Component(modules = QuestionHandlerModule.class, dependencies = AppComponent.class)
public interface QuestionHandlerComponent {
    void inject(QuestionHandlerActivity activity);
}