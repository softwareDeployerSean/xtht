package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.ExerciserData;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerExerciseListComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ExerciseListModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseListContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ExerciseListPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.ExerciseListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ExerciseListActivity extends BaseActivity<ExerciseListPresenter> implements ExerciseListContract.View {

    @BindView(R.id.exercise_list_rv)
    public RecyclerView exerciseListRl;

    private ExerciseListAdapter adapter;
    private List<ExerciserData> exerciserDataList;

    private boolean isLoading = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

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
        exerciserDataList = mPresenter.loadExerciseList();

        sort(exerciserDataList);

        showMonthTitle(exerciserDataList);

        setAdapter(exerciserDataList);
    }

    private void showMonthTitle(List<ExerciserData> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setDisplayMonthTitle(false);
        }

        LogUtils.debugInfo(list.toString());

        list.get(0).setDisplayMonthTitle(true);
        for (int i = 0; i < list.size() - 1; i++) {
            if (!equalsMonth(list.get(i).getDate(), list.get(i + 1).getDate())) {
                list.get(i + 1).setDisplayMonthTitle(true);
            }
        }
        LogUtils.debugInfo(list.toString());
    }

    private void sort(List<ExerciserData> list) {
        Collections.sort(list, new Comparator<ExerciserData>() {
            @Override
            public int compare(ExerciserData data1, ExerciserData data2) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                    Date dt1 = df.parse(data1.getDate());
                    Date dt2 = df.parse(data2.getDate());

                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
    }


    @Override
    public void setAdapter(List<ExerciserData> list) {
        adapter = new ExerciseListAdapter(this, list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        exerciseListRl.setLayoutManager(layoutManager);
        exerciseListRl.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, R.drawable.divider_mileage));

        adapter.setmOnItemClickListener(new ExerciseListAdapter.onItemClickListener() {
            @Override
            public void onIemClick(int position) {
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemDel(int position) {
                exerciserDataList.remove(position);
                sort(exerciserDataList);
                showMonthTitle(exerciserDataList);
                adapter.notifyDataSetChanged();
            }
        });

        exerciseListRl.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.d("test", "loading executed");

                    if (!isLoading) {
                        isLoading = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadMore();
                                Log.d("test", "load more completed");
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
        exerciseListRl.setAdapter(adapter);
    }

    private void loadMore() {

    }

    public boolean equalsMonth(String d1, String d2) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

            Date date1 = df.parse(d1);
            Date date2 = df.parse(d2);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            int year1 = calendar1.get(Calendar.YEAR);
            int year2 = calendar2.get(Calendar.YEAR);
            int month1 = calendar1.get(Calendar.MONTH) + 1;
            int month2 = calendar2.get(Calendar.MONTH) + 1;
            return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
        } catch (Exception e) {
            return false;
        }

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