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
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerSportMonthSelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportMonthSelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportMonthSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.SportMonthSelectedPresenter;
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


public class SportMonthSelectedFragment extends BaseFragment<SportMonthSelectedPresenter> implements SportMonthSelectedContract.View {

    @BindView(R.id.sport_month_hv)
    public HistogramView histogramView;

//    private List<String> months;
    private List<String> lastMonths = null;

    private List<Date> allDates;
    private List<Date> lastMonthAllDates;

    private String date;

    private String lastDate;

    private Data_run dataRun;

    private String last2MonthDate;

    private List<StepInfos> stepInfosList;
    private List<StepInfos> lastMonthStepInfosList;

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

    public static SportMonthSelectedFragment newInstance() {
        SportMonthSelectedFragment fragment = new SportMonthSelectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerSportMonthSelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .sportMonthSelectedModule(new SportMonthSelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport_month_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            months = getMonthList(sdf.parse(date));
//            for (int i = 0; i < months.size(); i++) {
//                Log.d("TAG", months.get(i) + "============");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        Calendar lastMonth = getDateOfLastMonth(date);
        lastDate = sdf.format(lastMonth.getTime());

        Calendar last2Month = getDateOfLastMonth(lastDate);
        last2MonthDate = sdf.format(last2Month.getTime());

        allDates = getBetweenDates(lastDate, date);

        lastMonthAllDates = getBetweenDates(last2MonthDate, lastDate);

        String[] xLables = new String[allDates.size()];
        for (int i = 0; i < allDates.size(); i++) {
            Date d = allDates.get(i);
            int dy = d.getDate();
            xLables[i] = String.valueOf(dy);
        }

        histogramView.setxLables(xLables);
        histogramView.setIntervalPercent(0.7f);
        histogramView.setxDisplayInterval(7);
        histogramView.setStartColor(Color.parseColor("#72FF00"));
        histogramView.setEndColor(Color.parseColor("#72FF00"));


        dayTv.setText(lastDate + "~" + date);

        loadDatas();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int totalSteps = 0;
            int totalDistance = 0;
            int totalGol = 0;
            int totalCal = 0;
            if(stepInfosList != null && stepInfosList.size() > 0) {
                StepInfos stepInfos = null;
                int[] datas = new int[allDates.size()];
                for(int i = 0; i < stepInfosList.size(); i++) {
                    stepInfos = stepInfosList.get(i);
                    int a = -1;
                    for (int z = 0; i < allDates.size(); z++) {
                        if (allDates.get(z).equals(stepInfos.getDates())) {
                            a = z;
                            break;
                        }
                    }

                    if(a != -1) {
                        datas[a] = stepInfos.getStep();
                    }

                    totalSteps += stepInfos.getStep();
                    totalDistance += stepInfos.getDistance();
                    totalGol += stepInfos.getStepGoal();
                    totalCal += stepInfos.getCalories();
                }

                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                stepTv.setText(String.valueOf(totalSteps));
                calTv.setText(String.valueOf(totalCal/allDates.size()));
                distanceTv.setText(String.valueOf(decimalFormat.format((float)totalDistance / allDates.size())));

                if(totalGol != 0) {
                    float standard = (float) totalSteps / totalGol;
                    standardTv.setText(decimalFormat.format(standard * 100) + "%");
                }else {
                    standardTv.setText("0");
                }

                if(totalSteps != 0) {
                    stepByHour.setText(String.valueOf(totalSteps / allDates.size()));
                }else {
                    stepByHour.setText("0");
                }

                int lastMonthStepTatal = 0;
                if(lastMonthStepInfosList != null && lastMonthStepInfosList.size() > 0) {
                    StepInfos lastMonthStepInfos = null;
                    for(int i = 0; i < lastMonthStepInfosList.size(); i++) {
                        lastMonthStepInfos = lastMonthStepInfosList.get(i);
                        lastMonthStepTatal += lastMonthStepInfos.getStep();
                    }
                }

                String rate = "0%";
                statusIv.setVisibility(View.VISIBLE);
                if(lastMonthStepTatal == 0 && totalSteps == 0) {
                    statusIv.setVisibility(View.GONE);
                    rate = "0%";
                }else if(lastMonthStepTatal == 0 && totalSteps > 0) {
                    statusIv.setVisibility(View.VISIBLE);
                    statusIv.setImageResource(R.mipmap.jia);
                    rate = "100%";
                }else if(lastMonthStepTatal > 0 && totalSteps > 0 && lastMonthStepTatal >= totalSteps) {
                    statusIv.setVisibility(View.VISIBLE);
                    statusIv.setImageResource(R.mipmap.jian);

                    rate = decimalFormat.format(Math.abs(lastMonthStepTatal - totalSteps) / (float)lastMonthStepTatal * 100) + "%";
                }else if(lastMonthStepTatal > 0 && totalSteps > 0 && lastMonthStepTatal < totalSteps) {
                    statusIv.setVisibility(View.VISIBLE);
                    statusIv.setImageResource(R.mipmap.jia);
                    rate = decimalFormat.format(Math.abs(totalSteps - lastMonthStepTatal) / (float)lastMonthStepTatal * 100) + "%";
                }
                contrastTv.setText(rate);

                histogramView.setDatas(datas);
            }else {
                stepTv.setText("0");
                calTv.setText("0");
                distanceTv.setText("0");
                standardTv.setText("0");
                stepByHour.setText("0");
                statusIv.setVisibility(View.GONE);
                contrastTv.setText("0%");
            }

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
        new Thread(){
            @Override
            public void run() {
                stepInfosList = SqlHelper.instance().getLastDateStep(MyApplication.account, lastDate, date);
                lastMonthStepInfosList = SqlHelper.instance().getLastDateStep(MyApplication.account, last2MonthDate, lastDate);

                DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
                dbhelper.open();
                dataRun = dbhelper.getmonth_data();

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private List<Date> getBetweenDates(String beginStr, String endStr) {
        List<Date> result = new ArrayList<Date>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date begin = sdf.parse(beginStr);
            Date end = sdf.parse(endStr);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(begin);
            /* Calendar tempEnd = Calendar.getInstance();
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            tempEnd.setTime(end);
            while (tempStart.before(tempEnd)) {
                result.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }*/
            while (begin.getTime() <= end.getTime()) {
                result.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
                begin = tempStart.getTime();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + smdate);
        }
    }

    public Calendar getDateOfLastMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return getDateOfLastMonth(c);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);
        }
    }

    public Calendar getDateOfLastMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -1);
        return lastDate;
    }

    private List<String> getMonthList(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        List<String> list = new ArrayList<>();
        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
            list.add(df);
        }
        return list;
    }

    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public void setDate(String date) {
        this.date = date;
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