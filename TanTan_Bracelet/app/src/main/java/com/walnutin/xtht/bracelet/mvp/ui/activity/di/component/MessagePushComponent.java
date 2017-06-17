package com.walnutin.xtht.bracelet.mvp.ui.activity.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.MessagePushModule;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.MessagePushActivity;

@ActivityScope
@Component(modules = MessagePushModule.class, dependencies = AppComponent.class)
public interface MessagePushComponent {
    void inject(MessagePushActivity activity);
}