package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Data_run;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by Leiht on 2017/8/3.
 */

public class SportDayPageItem {
    private Context mContext;

    private int position;

    View view;

    public HistogramView histogramView;

    private String date;

    private String lastDate;

    private StepInfos stepinfos;

    private Data_run dataRun;

    private StepInfos lastDayStepInfos;

    public TextView dayTv;

    public TextView stepTv;

    public TextView calTv;

    public TextView distanceTv;

    public TextView standardTv;

    public TextView stepByHour;

    public TextView sportCountTv;

    public TextView sportCountTime;

    public TextView sportCountDistance;

    public TextView contrastTv;

    public ImageView statusIv;

    public SportDayPageItem(Context context) {

        this.mContext = context;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.sport_day_selected_item, null);

        histogramView = (HistogramView) view.findViewById(R.id.sport_day_hv);
        dayTv = (TextView) view.findViewById(R.id.tv_day);
        stepTv = (TextView) view.findViewById(R.id.tv_step);
        calTv = (TextView) view.findViewById(R.id.tv_cal);
        distanceTv = (TextView) view.findViewById(R.id.tv_distance);
        standardTv = (TextView) view.findViewById(R.id.tv_rate);
        stepByHour = (TextView) view.findViewById(R.id.tv_stepbyhour);
        sportCountTv = (TextView) view.findViewById(R.id.tv_cishu);
        sportCountTime = (TextView) view.findViewById(R.id.tv_time);
        sportCountDistance = (TextView) view.findViewById(R.id.tv_juli);
        contrastTv = (TextView) view.findViewById(R.id.tv_contrast);
        statusIv = (ImageView) view.findViewById(R.id.iv_status);


    }

    public void update(String date) {
        this.date = date;
        lastDate = getLastDateaa(date);
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

    public String getDate() {
        return this.date;
    }

    public View getView() {
        return this.view;
    }
}
