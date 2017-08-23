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

import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Data_run;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Leiht on 2017/8/4.
 */

public class SportMonthPageItem {

    private Context mContext;

    private String date;

    private View view;

    private String lastDate;

    private Data_run dataRun;

    private List<String> months;
    private List<String> lastMonths;

    private List<StepInfos> stepInfosList;
    private List<StepInfos> lastMonthStepInfosList;

    @BindView(R.id.sport_month_hv)
    public HistogramView histogramView;

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

    public SportMonthPageItem(Context context) {
        this.mContext = context;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.sport_month_selected_item, null);

        histogramView = (HistogramView) view.findViewById(R.id.sport_month_hv);
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

        loadDatas();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int totalSteps = 0;
            int totalDistance = 0;
            int totalGol = 0;
            int totalCal = 0;

            int standardDays = 0;

            if (stepInfosList != null && stepInfosList.size() > 0) {
                StepInfos stepInfos = null;
                int[] datas = new int[months.size()];
                for (int i = 0; i < stepInfosList.size(); i++) {
                    stepInfos = stepInfosList.get(i);
                    int a = -1;
                    for (int z = 0; i < months.size(); z++) {
                        if (months.get(z).equals(stepInfos.getDates())) {
                            a = z;
                            break;
                        }
                    }

                    if (a != -1) {
                        datas[a] = stepInfos.getStep();
                    }

                    totalSteps += stepInfos.getStep();
                    totalDistance += stepInfos.getDistance();
                    int tempGoal = stepInfos.getStepGoal();
                    if (tempGoal <= 0) {
                        tempGoal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                    }
                    totalGol += tempGoal;
                    totalCal += stepInfos.getCalories();

                    if (stepInfos.getStep() >= tempGoal) {
                        standardDays++;
                    }
                }

                DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                stepTv.setText(String.valueOf(totalSteps));
                calTv.setText(String.valueOf(totalCal / months.size()));
                distanceTv.setText(String.valueOf(decimalFormat.format((float) totalDistance / months.size())));

                float standard = (float) standardDays / months.size();
                standardTv.setText(decimalFormat.format(standard * 100) + "%");

                if (totalSteps != 0) {
                    stepByHour.setText(String.valueOf(totalSteps / months.size()));
                } else {
                    stepByHour.setText("0");
                }

                int lastMonthStepTatal = 0;
                if (lastMonthStepInfosList != null && lastMonthStepInfosList.size() > 0) {
                    StepInfos lastMonthStepInfos = null;
                    for (int i = 0; i < lastMonthStepInfosList.size(); i++) {
                        lastMonthStepInfos = lastMonthStepInfosList.get(i);
                        lastMonthStepTatal += lastMonthStepInfos.getStep();
                    }
                }

                String rate = "0%";
                statusIv.setVisibility(View.VISIBLE);
                if (lastMonthStepTatal == 0 && totalSteps == 0) {
                    statusIv.setVisibility(View.GONE);
                    rate = "0%";
                } else if (lastMonthStepTatal == 0 && totalSteps > 0) {
                    statusIv.setVisibility(View.VISIBLE);
                    statusIv.setImageResource(R.mipmap.jia);
                    rate = "100%";
                } else if (lastMonthStepTatal > 0 && totalSteps > 0 && lastMonthStepTatal >= totalSteps) {
                    statusIv.setVisibility(View.VISIBLE);
                    statusIv.setImageResource(R.mipmap.jian);

                    rate = decimalFormat.format(Math.abs(lastMonthStepTatal - totalSteps) / (float) lastMonthStepTatal * 100) + "%";
                } else if (lastMonthStepTatal > 0 && totalSteps > 0 && lastMonthStepTatal < totalSteps) {
                    statusIv.setVisibility(View.VISIBLE);
                    statusIv.setImageResource(R.mipmap.jia);
                    rate = decimalFormat.format(Math.abs(totalSteps - lastMonthStepTatal) / (float) lastMonthStepTatal * 100) + "%";
                }
                contrastTv.setText(rate);

                histogramView.setDatas(datas);
            } else {
                stepTv.setText("0");
                calTv.setText("0");
                distanceTv.setText("0");
                standardTv.setText("0");
                stepByHour.setText("0");
                statusIv.setVisibility(View.GONE);
                contrastTv.setText("0%");
                int[] datas = new int[months.size()];
                histogramView.setDatas(datas);
            }

            if (dataRun != null) {
                sportCountTv.setText(String.valueOf(dataRun.getCishu()));
                sportCountTime.setText(String.valueOf(dataRun.getTime()));
                sportCountDistance.setText(String.valueOf(dataRun.getDistances()));
            } else {
                sportCountTv.setText("0");
                sportCountTime.setText("0");
                sportCountDistance.setText("0");
            }
        }
    };

    private void loadDatas() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        lastDate = getLastMonthToday(date);

//        try {
//            allDates = getMonthList(sdf.parse(date));
//            lastMonthAllDates = getMonthList(sdf.parse(lastDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//        String[] xLables = new String[allDates.size()];
//        for (int i = 0; i < allDates.size(); i++) {
//            Date d = allDates.get(i);
//            int dy = d.getDate();
//            xLables[i] = String.valueOf(dy);
//        }

        try {
            months = getMonthList(sdf.parse(date));
            lastMonths = getMonthList(sdf.parse(lastDate));
            for (int i = 0; i < months.size(); i++) {
                Log.d("TAG", months.get(i) + "============");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] xLables = new String[months.size()];
        for (int i = 0; i < months.size(); i++) {
            xLables[i] = String.valueOf(i + 1);
        }

        histogramView.setxLables(xLables);
        histogramView.setIntervalPercent(0.7f);
        histogramView.setxDisplayInterval(7);
        histogramView.setStartColor(Color.parseColor("#72FF00"));
        histogramView.setEndColor(Color.parseColor("#72FF00"));


        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dayTv.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1));

        stepInfosList = null;
        lastMonthStepInfosList = null;
        new Thread() {
            @Override
            public void run() {
                if (months != null && months.size() > 0 && lastMonths != null && lastMonths.size() > 0) {
                    stepInfosList = SqlHelper.instance().getLastDateStep(MyApplication.account, months.get(0), months.get(months.size() - 1));
                    lastMonthStepInfosList = SqlHelper.instance().getLastDateStep(MyApplication.account, lastMonths.get(0), lastMonths.get(lastMonths.size() - 1));
                    LogUtils.debugInfo("我是数据一" + stepInfosList.toString());
                    LogUtils.debugInfo("我是数据二" + lastMonthStepInfosList.toString());


                    DbAdapter dbhelper = new DbAdapter(MyApplication.getAppContext());
                    dbhelper.open();
                    dataRun = dbhelper.getmonth_data();
                    dbhelper.close();
                    mHandler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private String getLastMonthToday(String da) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(da);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("当前时间是：" + format.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        System.out.println("day=" + day);
        System.out.println("month=" + month);

        if (month == 3) {
            if (day == 31 || day == 30 || day == 29) {
                calendar.set(Calendar.DAY_OF_MONTH, 28);
            }
        }
        if (month == 5 || month == 7 || month == 10 || month == 12) {
            if (day == 31) {
                calendar.set(Calendar.DAY_OF_MONTH, 30);
            }
        }
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();

        return format.format(date);
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
        } catch (Exception e) {
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

    public String getDate() {
        return this.date;
    }

    public View getView() {
        return this.view;
    }
}
