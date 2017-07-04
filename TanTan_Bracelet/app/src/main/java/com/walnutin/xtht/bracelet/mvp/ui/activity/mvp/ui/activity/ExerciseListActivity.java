package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerExerciseListComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ExerciseListModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ExerciseListPresenter;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ExerciseListActivity extends BaseActivity<ExerciseListPresenter> implements ExerciseListContract.View {


    @BindView(R.id.test)
    Button test;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerExerciseListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .exerciseListModule(new ExerciseListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_exercise_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseDetailActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


}