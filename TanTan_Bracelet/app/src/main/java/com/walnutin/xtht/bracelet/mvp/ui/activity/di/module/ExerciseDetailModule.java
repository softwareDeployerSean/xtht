package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseDetailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.ExerciseDetailModel;


@Module
public class ExerciseDetailModule {
    private ExerciseDetailContract.View view;

    /**
     * 构建ExerciseDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ExerciseDetailModule(ExerciseDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ExerciseDetailContract.View provideExerciseDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ExerciseDetailContract.Model provideExerciseDetailModel(ExerciseDetailModel model) {
        return model;
    }
}