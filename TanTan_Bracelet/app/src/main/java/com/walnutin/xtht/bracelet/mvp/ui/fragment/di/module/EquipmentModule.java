package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.EquipmentModel;


@Module
public class EquipmentModule {
    private EquipmentContract.View view;

    /**
     * 构建EquipmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public EquipmentModule(EquipmentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    EquipmentContract.View provideEquipmentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    EquipmentContract.Model provideEquipmentModel(EquipmentModel model) {
        return model;
    }
}