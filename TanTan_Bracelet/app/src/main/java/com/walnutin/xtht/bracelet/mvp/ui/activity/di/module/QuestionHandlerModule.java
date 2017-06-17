package com.walnutin.xtht.bracelet.mvp.ui.activity.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.QuestionHandlerContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model.QuestionHandlerModel;


@Module
public class QuestionHandlerModule {
    private QuestionHandlerContract.View view;

    /**
     * 构建QuestionHandlerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public QuestionHandlerModule(QuestionHandlerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    QuestionHandlerContract.View provideQuestionHandlerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    QuestionHandlerContract.Model provideQuestionHandlerModel(QuestionHandlerModel model) {
        return model;
    }
}