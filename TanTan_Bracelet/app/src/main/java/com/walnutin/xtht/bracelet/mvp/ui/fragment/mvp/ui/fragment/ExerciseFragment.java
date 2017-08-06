package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ExerciseListActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ExerciseListNodataActivity;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerExerciseComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.ExerciseModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.ExerciseContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.ExercisePresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ExerciseFragment extends BaseFragment<ExercisePresenter> implements ExerciseContract.View {

    @BindView(R.id.item_1_parent)
    public RelativeLayout itemRl1;

    @BindView(R.id.item_2_parent)
    public RelativeLayout itemRl2;

    @BindView(R.id.item_3_parent)
    public RelativeLayout itemRl3;

    @BindView(R.id.item_4_parent)
    public RelativeLayout itemRl4;
    //运动数据==
    List<PathRecord> run_record = new ArrayList<PathRecord>();
    List<PathRecord> indoor_record = new ArrayList<PathRecord>();
    List<PathRecord> mountain_record = new ArrayList<PathRecord>();
    List<PathRecord> ride_record = new ArrayList<PathRecord>();
    @BindView(R.id.all_run_distance_tv)
    TextView allRunDistanceTv;
    @BindView(R.id.all_run_count_tv)
    TextView allRunCountTv;
    @BindView(R.id.inner_run_distance_tv)
    TextView innerRunDistanceTv;
    @BindView(R.id.item_2_run_count)
    TextView item2RunCount;
    @BindView(R.id.inner_run_count_tv)
    TextView innerRunCountTv;
    @BindView(R.id.dengshan_run_distance_tv)
    TextView dengshanRunDistanceTv;
    @BindView(R.id.dengshan_run_count_tv)
    TextView item3RunCount;
    @BindView(R.id.qixing_run_distance_tv)
    TextView qixingRunDistanceTv;
    @BindView(R.id.item_4_run_count)
    TextView item4RunCount;
    @BindView(R.id.qixing_run_count_tv)
    TextView qixingRunCountTv;
    Unbinder unbinder;


    public static ExerciseFragment newInstance() {
        ExerciseFragment fragment = new ExerciseFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerExerciseComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .exerciseModule(new ExerciseModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    /**
     * @param savedInstanceState 1.跑步
     *                           2.室内
     *                           3.登山
     *                           4.骑行
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        setItemClick(itemRl1, 1);
        setItemClick(itemRl2, 2);
        setItemClick(itemRl3, 3);
        setItemClick(itemRl4, 4);
    }

    @Override
    public void onResume() {
        super.onResume();
        get_Exercisedata();
    }

    double run_distance = 0;
    int run_number = 0;
    double indoor_distance = 0;
    int indoor_number = 0;
    double mountain_distance = 0;
    int mountain_number = 0;
    double riding_distance = 0;
    int riding_number = 0;

    public void initdata() {
        run_distance = 0;
        run_number = 0;
        indoor_distance = 0;
        indoor_number = 0;
        mountain_distance = 0;
        mountain_number = 0;
        riding_distance = 0;
        riding_number = 0;
    }


    public void get_Exercisedata() {
        initdata();
        DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
        dbhelper.open();
        //LogUtils.debugInfo(dbhelper.queryRecordAll().size()+"大小");
        run_record = dbhelper.queryRecordBySign("running_out");
        indoor_record = dbhelper.queryRecordBySign("running_indoor");
        mountain_record = dbhelper.queryRecordBySign("mountaineering");
        ride_record = dbhelper.queryRecordBySign("riding");
        dbhelper.close();
        if (run_record.size() > 0) {
            for (PathRecord p : run_record) {
                run_distance += Double.parseDouble(p.getDistance());
                LogUtils.debugInfo("距离=" + p.getDistance() + p.getDate() + "标记=" + p.getSign());
            }
            run_number = run_record.size();
            allRunDistanceTv.setText(ConmonUtils.shuzi(run_distance) + "");
            allRunCountTv.setText(run_number + "");
        }
        if (indoor_record.size() > 0) {
            for (PathRecord p : indoor_record) {
                indoor_distance += Double.parseDouble(p.getDistance());
            }
            indoor_number = indoor_record.size();
            innerRunDistanceTv.setText(ConmonUtils.shuzi(indoor_distance) + "");
            innerRunCountTv.setText(indoor_number + "");
        }
        if (mountain_record.size() > 0) {
            for (PathRecord p : mountain_record) {
                mountain_distance += Double.parseDouble(p.getDistance());
            }
            mountain_number = mountain_record.size();
            dengshanRunDistanceTv.setText(ConmonUtils.shuzi(mountain_distance) + "");
            item3RunCount.setText(mountain_number + "");
        }
        if (ride_record.size() > 0) {
            for (PathRecord p : ride_record) {
                riding_distance += Double.parseDouble(p.getDistance());
            }
            riding_number = ride_record.size();
            qixingRunDistanceTv.setText(ConmonUtils.shuzi(riding_distance) + "");
            qixingRunCountTv.setText(riding_number + "");
        }
        LogUtils.debugInfo("到底执行没有呢");

    }


    private void setItemClick(RelativeLayout itemRl, int type) {
        itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case 1:
                        if (run_record.size() == 0) {
                            launchActivity(new Intent(getActivity(), ExerciseListNodataActivity.class));
                        } else {
                            Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (indoor_record.size() == 0) {
                            launchActivity(new Intent(getActivity(), ExerciseListNodataActivity.class));
                        } else {
                            Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (mountain_record.size() == 0) {
                            launchActivity(new Intent(getActivity(), ExerciseListNodataActivity.class));
                        } else {
                            Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        if (ride_record.size() == 0) {
                            launchActivity(new Intent(getActivity(), ExerciseListNodataActivity.class));
                        } else {
                            Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}