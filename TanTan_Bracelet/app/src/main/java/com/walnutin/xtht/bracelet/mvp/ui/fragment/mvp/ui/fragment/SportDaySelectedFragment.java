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
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerSportDaySelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.SportDaySelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.SportDaySelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.SportDaySelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SportDaySelectedFragment extends BaseFragment<SportDaySelectedPresenter> implements SportDaySelectedContract.View {

    @BindView(R.id.sport_day_hv)
    public HistogramView histogramView;

    private String date;

    private String lastDate;

    private StepInfos stepinfos;

    private Data_run dataRun;

    private StepInfos lastDayStepInfos;

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


    public static SportDaySelectedFragment newInstance() {
        SportDaySelectedFragment fragment = new SportDaySelectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerSportDaySelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .sportDaySelectedModule(new SportDaySelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport_day_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String[] xLables = new String[24];
        for (int i = 0; i < xLables.length; i++) {
            xLables[i] = String.valueOf(i);
        }
        histogramView.setxLables(xLables);
        histogramView.setIntervalPercent(0.7f);
        histogramView.setxDisplayType(1);
        Log.d("TAG", "Color.RED=" + Color.RED);
        histogramView.setStartColor(Color.parseColor("#72FF00"));
        histogramView.setEndColor(Color.parseColor("#72FF00"));
        histogramView.setxDisplayInterval(6);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            dayTv.setText(c.get(Calendar.MONTH) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        loadDatas();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (stepTv != null) {
                int totalStep = stepinfos.getStep();
                stepTv.setText(String.valueOf(totalStep));

                int[] datas = new int[24];
                Map<Integer, Integer> step4Hours = stepinfos.getStepOneHourInfo();

//                if (step4Hours == null || step4Hours.size() <= 0) {
//                    step4Hours = new HashMap<Integer, Integer>();
//                    step4Hours.put(17, 100);
//                    step4Hours.put(18, 100);
//                    step4Hours.put(19, 100);
//                    step4Hours.put(20, 100);
//                    step4Hours.put(21, 100);
//                    step4Hours.put(23, 100);
//                }

                int averageStepHour = 0;
                int allStep = 0;
                int count = 0;
                if (step4Hours != null && step4Hours.size() > 0) {
                    Set<Integer> set = step4Hours.keySet();
                    for (Integer in : set) {
                        allStep += step4Hours.get(in);
                        count++;
                        datas[in] = step4Hours.get(in);
                    }
                }
                if (count > 0) {
                    averageStepHour = allStep / count;
                }
                histogramView.setDatas(datas);

                Map<Integer, Integer> step4HoursLast = lastDayStepInfos.getStepOneHourInfo();
                int averageStepHourLast = 0;
                if (step4HoursLast != null && step4HoursLast.size() > 0) {
                    int allStepLast = 0;
                    int countLast = 0;
                    Set<Integer> set = step4HoursLast.keySet();
                    for (Integer in : set) {
                        allStepLast += step4HoursLast.get(in);
                        allStepLast++;
                    }
                    if (allStepLast > 0) {
                        averageStepHourLast = allStepLast / allStepLast;
                    }
                }
                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                float upOrDownRate = 0;
                boolean upOrDown = false;
                if((averageStepHour - averageStepHourLast) > 0) {
                    upOrDown = true;
                }
                if(averageStepHour > 0 && averageStepHourLast > 0) {
                    upOrDownRate = (averageStepHour - averageStepHourLast) / averageStepHourLast;
                }
                String rate = "0%";
                if(upOrDownRate != 0) {
                    rate =decimalFormat.format(upOrDownRate * 100) + "%";
                }
                contrastTv.setText(rate);
                statusIv.setVisibility(View.VISIBLE);
                if(upOrDown && (averageStepHour > 0 && averageStepHourLast > 0) ) {
                    statusIv.setImageResource(R.mipmap.jia);
                }else if(!upOrDown && (averageStepHour > 0 && averageStepHourLast > 0)){
                    statusIv.setImageResource(R.mipmap.jian);
                }else {
                    statusIv.setVisibility(View.GONE);
                }

                int calories = stepinfos.getCalories();
                calTv.setText(String.valueOf(calories));

                float distance = stepinfos.getDistance();
                String p = "";

                if (distance != 0) {
                    p = decimalFormat.format(distance);
                } else {
                    p = "0";
                }
                distanceTv.setText(p);

                if(stepinfos.getStepGoal() != 0) {
                float standard = (float) stepinfos.getStep() / stepinfos.getStepGoal();
                    standardTv.setText(decimalFormat.format(standard * 100) + "%");
                }else {
                    standardTv.setText("0%");
                }

                stepByHour.setText(String.valueOf(averageStepHour));

                sportCountTv.setText("10");
                sportCountTime.setText("0h30min");

                sportCountDistance.setText(p);


            } else {
                stepTv.setText("");
                calTv.setText("");
                calTv.setText("");
                stepByHour.setText("");
                sportCountDistance.setText("");
                standardTv.setText("0%");
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
        new Thread() {
            @Override
            public void run() {
                stepinfos = SqlHelper.instance().getOneDateStep(MyApplication.account, date);
                lastDayStepInfos = SqlHelper.instance().getOneDateStep(MyApplication.account, lastDate);

                DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
                dbhelper.open();
                dataRun = dbhelper.gettody_data();

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    public void setDate(String date) {
        this.date = date;
        lastDate = getLastDateaa(date);
    }

    private static String getLastDateaa(String d) {
        String ret = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date de = sdf.parse(d);
            Calendar c = Calendar.getInstance();
            c.setTime(de);
            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DATE) - 1);
            ret = sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
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