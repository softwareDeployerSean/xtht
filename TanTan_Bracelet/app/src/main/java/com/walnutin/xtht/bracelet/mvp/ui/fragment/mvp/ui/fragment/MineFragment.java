package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.BitmapHandler;
import com.walnutin.xtht.bracelet.app.utils.BitmapUtil;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Data_run;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BindbyPhoneActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.LoadActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.Personal_dataActivity;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerMineComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.MineModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MineContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.MinePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomProgressDialog;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnDismissListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View, OnItemClickListener, OnDismissListener {


    @BindView(R.id.check_head_photo)
    ImageView check_head_photo;
    SharedPreferences sharedPreferences = DataHelper.getSharedPerference(MyApplication.getAppContext());
    @BindView(R.id.tv_bind_account)
    TextView tvBindAccount;
    Unbinder unbinder;
    AlertView alertView_exit;
    @BindView(R.id.iv_day)
    ImageView ivDay;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_best_day)
    TextView tvBestDay;
    @BindView(R.id.tv_best_day_step)
    TextView tvBestDayStep;
    @BindView(R.id.iv_week)
    ImageView ivWeek;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_best_week_begin)
    TextView tvBestWeekBegin;
    @BindView(R.id.tv_best_week_end)
    TextView tvBestWeekEnd;
    @BindView(R.id.tv_bestweek_step)
    TextView tvBestweekStep;
    @BindView(R.id.iv_month)
    ImageView ivMonth;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_best_month)
    TextView tvBestMonth;
    @BindView(R.id.tv_best_month_step)
    TextView tvBestMonthStep;
    @BindView(R.id.iv_goin)
    ImageView ivGoin;
    @BindView(R.id.tv_goin)
    TextView tvGoin;
    @BindView(R.id.tv_rate_begin)
    TextView tvRateBegin;
    @BindView(R.id.tv_rate_end)
    TextView tvRateEnd;
    @BindView(R.id.tv_weekbygoin)
    TextView tvWeekbygoin;
    @BindView(R.id.tv_rate_day)
    TextView tvRateDay;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.linear_out)
    AutoLinearLayout linearOut;
    Unbinder unbinder1;
    @BindView(R.id.tv_active_day)
    TextView tvActiveDay;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_day_week)
    TextView tvDayWeek;
    @BindView(R.id.refresh)
    AutoLinearLayout refresh;
    Unbinder unbinder2;

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
        UserBean userBean = DataHelper.getDeviceData(MyApplication.getAppContext(), "UserBean");
        if (!user_photo_url.equals("")) {
            bitmap = BitmapUtil.getScaleBitmap(user_photo_url, 100, 100);//图片压缩
            if (bitmap != null) {
                check_head_photo.setImageBitmap(BitmapHandler.createCircleBitmap(bitmap));
            } else {
                mPresenter.download_img(userBean.getAvatar());
                LogUtils.debugInfo("下载1");
            }
        } else if (!TextUtils.isEmpty(userBean.getAvatar())) {
            LogUtils.debugInfo("下载2");
            mPresenter.download_img(userBean.getAvatar());
        } else {
            check_head_photo.setImageResource(R.mipmap.touxaing);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        initData(bundle);
        setdata();
    }

    StepInfos stepInfosday;//最佳日
    StepInfos stepInfosweek;//最佳周
    StepInfos stepInfosmonth;//最佳月
    List<StepInfos> continuous = new ArrayList<>();

    public void setdata() {

        UserBean userBean = DataHelper.getDeviceData(MyApplication.getContext(), "UserBean");
        int dairy = userBean.getDailyGoals();
        if (dairy == 0) {
            dairy = 7000;
        }
        dairy = dairy * 7;
        tvDayWeek.setText(0 + "/" + dairy);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tody = sdf.format(d);
        String time = ConmonUtils.getweek_day(tody);
        String begin_end[] = time.split(",");

        int sum_stepbyweek = 0;
        int all_day = 0;
        int standard = 0;
        all_day = SqlHelper.instance().getAllstep(MyApplication.account).size();
        standard = SqlHelper.instance().getStandardstep().size();
        DbAdapter dbHepler = new DbAdapter(getActivity());
        dbHepler.open();
        stepInfosday = SqlHelper.instance().getBestDayStep(MyApplication.account);
        stepInfosweek = SqlHelper.instance().getBestWeekStep(MyApplication.account);
        stepInfosmonth = SqlHelper.instance().getBestMonthStep(MyApplication.account);
        continuous = SqlHelper.instance().getMaxContinuousNumber();
        int active = dbHepler.getweek_active();
        sum_stepbyweek = SqlHelper.instance().getweekstep(begin_end[0], begin_end[1]);
        if (stepInfosday.getStep() > 0) {
            tvBestDay.setText(stepInfosday.getDates());
            tvBestDayStep.setText(stepInfosday.getStep() + "");
        }
        if (stepInfosweek.getStep() > 0) {
            String data[] = stepInfosweek.getDates().split("-");
            tvBestWeekBegin.setText(ConmonUtils.getFirstDayOfWeek(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
            tvBestWeekEnd.setText(ConmonUtils.getLastDayOfWeek(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
            tvBestweekStep.setText(stepInfosweek.getStep() + "");
        }
        if (stepInfosmonth.getStep() > 0) {
            tvBestMonth.setText(stepInfosmonth.getDates());
            tvBestMonthStep.setText(stepInfosmonth.getStep() + "");
        }
        if (continuous.size() > 0) {
            tvWeekbygoin.setText(continuous.size() + "");
            tvRateBegin.setText(continuous.get(0).getDates());
            tvRateEnd.setText(continuous.get(continuous.size() - 1).getDates());
        }
        if (sum_stepbyweek > 0) {
            tvDayWeek.setText(sum_stepbyweek + "/" + dairy);
            progress.setMax(dairy);
            progress.setProgress(sum_stepbyweek);
        }
        if (all_day > 0 && standard > 0) {
            String rate = (standard / all_day * 100) + "%";
            tvRate.setText("达标率" + rate);
        }
        if (active > 0) {
            tvActiveDay.setText("活跃天数" + active);
        }
        dbHepler.close();


    }

/*
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
    }*/


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
        CustomProgressDialog.show(getActivity());
    }

    @Override
    public void hideLoading() {
        CustomProgressDialog.dissmiss();
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

    public void exit() {
        alertView_exit = new AlertView(null, getString(R.string.exit_ok), getString(R.string.canecl), new String[]{getString(R.string.confirm)}, null, getActivity(), AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView_exit.show();
    }

    @OnClick({R.id.tv_bind_account, R.id.check_head_photo, R.id.tv_exit})
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
            case R.id.tv_exit:
                exit();
                break;

        }
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:
                launchActivity(new Intent(getActivity(), LoadActivity.class));
                getActivity().finish();
                DataHelper.setStringSF(MyApplication.getAppContext(), "isload", "default");
                break;
        }
    }

    @Override
    public void down_success() {
        onResume();
    }

}