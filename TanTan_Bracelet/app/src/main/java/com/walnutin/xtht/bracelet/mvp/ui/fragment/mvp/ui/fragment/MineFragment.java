package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.BitmapHandler;
import com.walnutin.xtht.bracelet.app.utils.BitmapUtil;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BindbyPhoneActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.Personal_dataActivity;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerMineComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.MineModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MineContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.MinePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {


    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.check_head_photo)
    ImageView check_head_photo;
    SharedPreferences sharedPreferences = DataHelper.getSharedPerference(MyApplication.getAppContext());
    @BindView(R.id.tv_bind_account)
    TextView tvBindAccount;
    Unbinder unbinder;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMineComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mineModule(new MineModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    private String user_photo_url;
    private Bitmap bitmap;

    String load_tag = DataHelper.getStringSF(MyApplication.getAppContext(), "load_tag");

    @Override
    public void initData(Bundle savedInstanceState) {
        user_photo_url = sharedPreferences.getString(sharedPreferences.getString("username", ""), "");
        if (!user_photo_url.equals("")) {
            bitmap = BitmapUtil.getScaleBitmap(user_photo_url, 100, 100);//图片压缩
            if (bitmap != null) {
                check_head_photo.setImageBitmap(BitmapHandler.createCircleBitmap(bitmap));
            } else
                check_head_photo.setImageResource(R.mipmap.touxaing);
        }
    }

    private void init_refresh() {
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
            }

            @Override
            public void onfinish() {

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
            }
        });
        refresh.autoRefresh();
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


    @OnClick({R.id.tv_bind_account, R.id.check_head_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_bind_account:
                if (load_tag.equals("phone")) {
                    Intent intent = new Intent(getActivity(), BindbyPhoneActivity.class);
                    intent.putExtra("tag", "email");
                    launchActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), BindbyPhoneActivity.class);
                    intent.putExtra("tag", "phone");
                    launchActivity(intent);
                }
                break;
            case R.id.check_head_photo:
                launchActivity(new Intent(getActivity(), Personal_dataActivity.class));
                break;

        }
    }


}