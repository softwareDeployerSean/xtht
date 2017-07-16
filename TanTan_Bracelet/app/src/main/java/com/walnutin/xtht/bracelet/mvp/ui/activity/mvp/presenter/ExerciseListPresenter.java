package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.ExerciserData;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;

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

    List<PathRecord> list = new ArrayList<>();

    public List<PathRecord> loadExerciseList(int type, int nextPage) {
        DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
        dbhelper.open();
        if (type == 1) {
            list.addAll(dbhelper.queryRecordBySign_andpage("running_out", nextPage));
        } else if (type == 2) {
            list.addAll(dbhelper.queryRecordBySign_andpage("running_indoor", nextPage));
        } else if (type == 3) {
            list.addAll(dbhelper.queryRecordBySign_andpage("mountaineering", nextPage));
        } else if (type == 4) {
            list.addAll(dbhelper.queryRecordBySign_andpage("riding", nextPage));
        }
        return list;
    }

    public String getAllByMonth(String firstDayOfMonth, String lastDayOfMonth) {
        DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
        dbhelper.open();
        String total = dbhelper.bytimegetdata(firstDayOfMonth, lastDayOfMonth);

        return total;
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