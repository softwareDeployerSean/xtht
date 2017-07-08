package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.os.Bundle;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.walnutin.xtht.bracelet.R;

/**
 * Created by suns on 2017-07-07.
 */

public class ExerciseListNodataActivity extends BaseActivity {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_exercise_nodata;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}
