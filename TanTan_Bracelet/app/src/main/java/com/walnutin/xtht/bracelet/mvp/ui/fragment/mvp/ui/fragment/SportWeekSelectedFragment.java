package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Data_run;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerSportWeekSelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportWeekSelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportWeekSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.SportWeekSelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SportWeekSelectedFragment extends BaseFragment<SportWeekSelectedPresenter> implements SportWeekSelectedContract.View {

    @BindView(R.id.sport_week_hv)
    public HistogramView histogramView;

    private String date;

    private String monday;
    private String sunday;

    private Data_run dataRun;

    private List<Date> weeks;
    private List<String> lastWeeks = null;

    private List<StepInfos> stepInfosList;
    private List<StepInfos> lastWeekStepInfosList;

    @BindView(R.id.tv_day)
    public TextView dayTv;

    @BindView(R.id.tv_step)
    public TextView stepTv;

    @BindView(R.id.tv_cal)
    public TextView calTv;

    @BindView(R.id.tv_distance)
    public TextView distanceTv;

    @BindView(R.id.tv_rate)
    public TextView standardTv;

    @BindView(R.id.tv_stepbyhour)
    public TextView stepByHour;

    @BindView(R.id.tv_cishu)
    public TextView sportCountTv;

    @BindView(R.id.tv_time)
    public TextView sportCountTime;

    @BindView(R.id.tv_juli)
    public TextView sportCountDistance;

    @BindView(R.id.tv_contrast)
    public TextView contrastTv;

    @BindView(R.id.iv_status)
    public ImageView statusIv;

    public static SportWeekSelectedFragment newInstance() {
        SportWeekSelectedFragment fragment = new SportWeekSelectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerSportWeekSelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .sportWeekSelectedModule(new SportWeekSelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport_week_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        String[] xLables = new String[]{"一", "二", "三", "四", "五", "六", "日"};

        histogramView.setxLables(xLables);
        histogramView.setIntervalPercent(0.2f);
        Log.d("TAG", "Color.RED=" + Color.RED);
        histogramView.setStartColor(Color.parseColor("#72FF00"));
        histogramView.setEndColor(Color.parseColor("#72FF00"));


        monday = getMonday(date);
        sunday = getSunday(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date mdate = null;
        try {
            mdate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weeks = dateToWeek(mdate);
        lastWeeks = getLastWeek(date);
        dayTv.setText(monday + "~" + sunday);
        loadDatas();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int[] datas = new int[7];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int totalStep = 0;
            int totalGol = 0;
            int totalCal = 0;
            int totalDistance = 0;
            if(stepInfosList != null && stepInfosList.size() > 0) {
                StepInfos stepInfos = null;
                for(int i = 0; i < stepInfosList.size(); i++) {
                    stepInfos = stepInfosList.get(i);
                    int a = -1;
                    for (int z = 0; i < weeks.size(); z++) {
                        if (sdf.format(weeks.get(z)).equals(stepInfos.getDates())) {
                            a = z;
                            break;
                        }
                    }
                    if(a != -1) {
                        datas[a] = stepInfos.getStep();

                        totalCal += stepInfos.getCalories();
                        totalDistance += stepInfos.getDistance();
                        totalStep += stepInfos.getStep();
                        totalGol += stepInfos.getStepGoal();
                    }
                }
            }
            histogramView.setDatas(datas);

            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            calTv.setText(String.valueOf(totalCal/7));
            distanceTv.setText(totalDistance > 0? String.valueOf(decimalFormat.format((float)totalDistance / 7)) : "0");
            stepTv.setText(String.valueOf(totalStep));


            if(totalGol != 0) {
                float standard = (float) totalStep / totalGol;
                standardTv.setText(decimalFormat.format(standard * 100) + "%");
            }else {
                standardTv.setText("0");
            }
            if(totalStep != 0) {
                stepByHour.setText(String.valueOf(totalStep / 7));
            }else {
                stepByHour.setText("0");
            }

            int lastWeekSteps = 0;
            if(lastWeekStepInfosList != null && lastWeekStepInfosList.size() > 0) {
                StepInfos lastStepInfos = null;
                for (int i = 0; i < lastWeekStepInfosList.size(); i++) {
                    lastStepInfos = lastWeekStepInfosList.get(i);
                    lastWeekSteps += lastStepInfos.getStep();
                }
            }
            String rate = "0%";
            boolean upOrDown = false;
            if(lastWeekSteps > 0) {
                if((totalStep - lastWeekSteps) > 0) {
                    upOrDown = true;
                }else {
                    upOrDown = false;
                }
                rate =decimalFormat.format(Math.abs(totalStep - lastWeekSteps) / (float)lastWeekSteps * 100) + "%";
            }
            statusIv.setVisibility(View.VISIBLE);
            if(upOrDown && lastWeekSteps > 0) {
                statusIv.setImageResource(R.mipmap.jia);
            }else if(!upOrDown && lastWeekSteps > 0){
                statusIv.setImageResource(R.mipmap.jian);
            }else{
                statusIv.setVisibility(View.GONE);
            }
            contrastTv.setText(rate);

            if(dataRun != null) {
                sportCountTv.setText(String.valueOf(dataRun.getCishu()));
                sportCountTime.setText(String.valueOf(dataRun.getTime()));
                sportCountDistance.setText(String.valueOf(dataRun.getDistances()));
            }else {
                sportCountTv.setText("0");
                sportCountTime.setText("0");
                sportCountDistance.setText("0");
            }
        }
    };

    private void loadDatas() {
        new Thread() {
            @Override
            public void run() {
                stepInfosList = SqlHelper.instance().getWeekLastDateStep(MyApplication.account, monday, sunday);
                lastWeekStepInfosList = SqlHelper.instance().getWeekLastDateStep(MyApplication.account, lastWeeks.get(0), lastWeeks.get(lastWeeks.size() - 1));

                DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
                dbhelper.open();
                dataRun = dbhelper.getweek_data();

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    public void setDate(String date) {
        this.date = date;
    }

    private List<String> getLastWeek(String dateStr) {
        List<String> lastWeeks = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 当前日期
        Calendar instance = Calendar.getInstance();

        instance.setTime(d);
        instance.add(Calendar.WEEK_OF_YEAR, -1);

        // 调整到上周1
        instance.set(Calendar.DAY_OF_WEEK, 2);
        //循环打印
        for (int i = 1; i <= 7; i++) {
            lastWeeks.add(format.format(instance.getTime()));
            instance.add(Calendar.DAY_OF_WEEK, 1);
        }
        return lastWeeks;
    }

    public List<Date> dateToWeek(Date mdate) {
        List<Date> datas = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mdate);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        for (int i = 0; i < 7; i++) {
            datas.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return datas;
    }

    private String getMonday(String d) {
        String monday = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dat = sdf.parse(d);
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setTimeInMillis(dat.getTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            monday = sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monday;
    }

    private String getSunday(String d) {
        String sunday = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dat = sdf.parse(d);
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setTimeInMillis(dat.getTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            sunday = sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sunday;
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