package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ExerciseListModel;


@Module
public class ExerciseListModule {
    private ExerciseListContract.View view;

    /**
     * 构建ExerciseListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ExerciseListModule(ExerciseListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ExerciseListContract.View provideExerciseListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ExerciseListContract.Model provideExerciseListModel(ExerciseListModel model) {
        return model;
    }
}