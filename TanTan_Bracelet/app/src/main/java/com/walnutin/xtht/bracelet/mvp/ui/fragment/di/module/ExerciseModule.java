package com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.ExerciseContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.model.ExerciseModel;


@Module
public class ExerciseModule {
    private ExerciseContract.View view;

    /**
     * 构建ExerciseModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ExerciseModule(ExerciseContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ExerciseContract.View provideExerciseView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ExerciseContract.Model provideExerciseModel(ExerciseModel model) {
        return model;
    }
}