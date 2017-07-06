package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.entity.ExerciserData;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseListContract;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class ExerciseListPresenter extends BasePresenter<ExerciseListContract.Model, ExerciseListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public ExerciseListPresenter(ExerciseListContract.Model model, ExerciseListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public List<ExerciserData> loadExerciseList(){
        List<ExerciserData> list = new ArrayList<>();

        ExerciserData data1 = new ExerciserData("2017-04-08 11:20");
        ExerciserData data2 = new ExerciserData("2017-04-03 11:20");
        ExerciserData data3 = new ExerciserData("2017-04-06 11:20");
        ExerciserData data4 = new ExerciserData("2017-03-01 11:20");
        ExerciserData data5 = new ExerciserData("2017-03-03 11:20");
        ExerciserData data6 = new ExerciserData("2017-03-02 11:20");

        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);

        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}