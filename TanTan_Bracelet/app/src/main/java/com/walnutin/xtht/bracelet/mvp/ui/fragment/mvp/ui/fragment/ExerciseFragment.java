package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ExerciseListActivity;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerExerciseComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.ExerciseModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.ExerciseContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.ExercisePresenter;

import butterknife.BindView;

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

    @Override
    public void initData(Bundle savedInstanceState) {
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        setItemClick(itemRl1, 1);
        setItemClick(itemRl2, 2);
        setItemClick(itemRl3, 3);
        setItemClick(itemRl4, 4);

    }

    private void setItemClick(RelativeLayout itemRl, int type) {
        itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
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

}