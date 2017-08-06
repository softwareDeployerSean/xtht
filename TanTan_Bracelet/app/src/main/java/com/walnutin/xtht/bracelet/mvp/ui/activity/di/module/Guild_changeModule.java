package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.Guild_changeContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.Guild_changeModel;


@Module
public class Guild_changeModule {
    private Guild_changeContract.View view;

    /**
     * 构建Guild_changeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public Guild_changeModule(Guild_changeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    Guild_changeContract.View provideGuild_changeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    Guild_changeContract.Model provideGuild_changeModel(Guild_changeModel model) {
        return model;
    }
}