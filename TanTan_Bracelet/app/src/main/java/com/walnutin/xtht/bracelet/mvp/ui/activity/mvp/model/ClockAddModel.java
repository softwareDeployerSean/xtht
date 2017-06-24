package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ClockAddContract;


@ActivityScope
public class ClockAddModel extends BaseModel implements ClockAddContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ClockAddModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public String[] loadWeeks() {
        String[] weeks = new String[7];
        for (int i = 0; i < 7; i++) {
            weeks[i] = "星期" + changeChineesWeek(i);
        }
        return  weeks;
    }


    private String changeChineesWeek(int i) {
        String result = "";

        switch (i) {
            case 0:
                result = "一";
                break;
            case 1:
                result = "二";
                break;
            case 2:
                result = "三";
                break;
            case 3:
                result = "四";
                break;
            case 4:
                result = "五";
                break;
            case 5:
                result = "六";
                break;
            case 6:
                result = "日";
                break;
        }
        return result;
    }
}