package com.walnutin.xtht.bracelet.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.walnutin.xtht.bracelet.di.module.UserModule;

import dagger.Component;

import com.walnutin.xtht.bracelet.mvp.ui.activity.UserActivity;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = UserModule.class,dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
}
