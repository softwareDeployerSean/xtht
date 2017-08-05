package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Leiht on 2017/8/2.
 */

public class SleepWeekPageItem {

    private Context mContext;

    private int position;

    View view;

    private String date;

    private List sleepModelList;

    private List<Date> weeks;

    HistogramView weekHv;

    public TextView timeSlotTv;

    public TextView deepSleepPerTv;

    public TextView simpleSleepPerTv;

    public TextView awakeSleepPerTv;

    public TextView deepSleepValueTv;
    public TextView simpleSleepValueTv;
    public TextView aweakSleepValueTv;

    public TextView sleepAllTimeTv;

    public TextView sleepLevelTv;

    public TextView sleepHTv;

    public SleepWeekPageItem(Context context) {
        this.mContext = context;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.sleep_week_selected_item, null);

        weekHv = (HistogramView) view.findViewById(R.id.week_hv);
        timeSlotTv = (TextView) view.findViewById(R.id.time_slot_tv);
        deepSleepPerTv = (TextView) view.findViewById(R.id.sleep_deep_per_tv);
        simpleSleepPerTv = (TextView) view.findViewById(R.id.sleep_simple_per_tv);
        awakeSleepPerTv = (TextView) view.findViewById(R.id.sleep_aweak_per_tv);
        deepSleepValueTv = (TextView) view.findViewById(R.id.sleep_deep_value_tv);
        simpleSleepValueTv = (TextView) view.findViewById(R.id.sleep_simple_value_tv);
        aweakSleepValueTv = (TextView) view.findViewById(R.id.sleep_aweak_value_tv);
        sleepAllTimeTv = (TextView) view.findViewById(R.id.sleep_all_time_tv);
        sleepLevelTv = (TextView) view.findViewById(R.id.sleep_level_tv);
        sleepHTv = (TextView) view.findViewById(R.id.sleep_h_tv);
        String[] xLables = new String[]{"一", "二", "三", "四", "五", "六", "日"};

        weekHv.setxLables(xLables);
        weekHv.setIntervalPercent(0.2f);
        Log.d("TAG", "Color.RED=" + Color.RED);
        weekHv.setStartColor(Color.parseColor("#6B289B"));
        weekHv.setEndColor(Color.parseColor("#D0B3EB"));
    }

    public void update(String date) {
        this.date = date;

        loadDatas();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int[] datas = new int[7];
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//            sleepModelList = new ArrayList();
//            SleepModel s1 = new SleepModel();
//            s1.setTotalTime(380);
//            s1.setDate(sdf1.format(weeks.get(0)));
//            s1.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s1.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s1.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            sleepModelList.add(s1);

//            SleepModel s2 = new SleepModel();
//            s2.setTotalTime(390);
//            s2.setDate(sdf1.format(weeks.get(1)));
//            s2.setDuraionTimeArray(new int[] {30,15,45,30,30,30,45,30,30,15,15,15,45,15,15});
//            s2.setTimePointArray(new int[] {1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388});
//            s2.setSleepStatusArray(new int[] {1,0,1,0,2,0,1,2,1,0,1,0,1,1,2});
//            sleepModelList.add(s2);

//            SleepModel s3 = new SleepModel();
//            s3.setTotalTime(400);
//            s3.setDuraionTimeArray(new int[] {30,15,45,30,30,30,45,30,30,15,15,15,45,15,15});
//            s3.setTimePointArray(new int[] {1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388});
//            s3.setSleepStatusArray(new int[] {1,0,1,0,2,0,1,2,1,0,1,0,1,1,2});
//            s3.setDate(sdf1.format(weeks.get(2)));
//            sleepModelList.add(s3);

//            SleepModel s4 = new SleepModel();
//            s4.setTotalTime(370);
//            s4.setDate(sdf1.format(weeks.get(3)));
//            s4.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s4.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s4.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            sleepModelList.add(s4);
//
//            SleepModel s5 = new SleepModel();
//            s5.setTotalTime(200);
//            s5.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s5.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s5.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            s5.setDate(sdf1.format(weeks.get(4)));
//            sleepModelList.add(s5);
//
//            SleepModel s6 = new SleepModel();
//            s6.setTotalTime(120);
//            s6.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s6.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s6.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            s6.setDate(sdf1.format(weeks.get(5)));
//            sleepModelList.add(s6);
//
//            SleepModel s7 = new SleepModel();
//            s7.setTotalTime(50);
//            s7.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s7.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s7.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            s7.setDate(sdf1.format(weeks.get(6)));
//            sleepModelList.add(s7);

            int deepSleepTotal = 0;
            int simpleSleepTotal = 0;
            int awakeSleepTotal = 0;
            int totalSleepTime = 0;
            if (sleepModelList != null && sleepModelList.size() > 0) {
                sleepHTv.setText("h");
                SleepModel sleepModel = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < sleepModelList.size(); i++) {
                    sleepModel = (SleepModel) sleepModelList.get(i);
                    totalSleepTime += sleepModel.getTotalTime();
                    String sleepDate = sleepModel.getDate();
                    String weekDate = sdf.format(weeks.get(i));
                    int a = -1;
                    for (int z = 0; i < weeks.size(); z++) {
                        if (sdf.format(weeks.get(z)).equals(sleepModel.getDate())) {
                            a = z;
                            break;
                        }
                    }
                    if (a != -1) {

                        datas[a] = sleepModel.getTotalTime();

                        int[] duraionTimeArray = sleepModel.getDuraionTimeArray();
                        int[] timePointArray = sleepModel.getTimePointArray();
                        int[] sleepStatusArray = sleepModel.getSleepStatusArray();

                        for (int j = 0; j < sleepStatusArray.length; j++) {
                            if (sleepStatusArray[i] == 2) {
                                awakeSleepTotal += duraionTimeArray[i];
                            } else if (sleepStatusArray[i] == 1) {
                                deepSleepTotal += duraionTimeArray[i];
                            } else if (sleepStatusArray[i] == 0) {
                                simpleSleepTotal += duraionTimeArray[i];
                            }
                        }
                    }
                }

                if ((deepSleepTotal + simpleSleepTotal + awakeSleepTotal) > 0) {
                    int a1 = (int) ((((float) deepSleepTotal / (deepSleepTotal + simpleSleepTotal + awakeSleepTotal))) * 100);
                    int b1 = (int) ((((float) simpleSleepTotal / (deepSleepTotal + simpleSleepTotal + awakeSleepTotal))) * 100);
                    String a = a1 + "%";
                    String b = b1 + "%";
                    String c = (100 - a1 - b1) + "%";
                    deepSleepPerTv.setText(a);
                    simpleSleepPerTv.setText(b);
                    awakeSleepPerTv.setText(c);
                } else {
                    deepSleepPerTv.setText("");
                    simpleSleepPerTv.setText("");
                    awakeSleepPerTv.setText("");
                }

                deepSleepValueTv.setText(deepSleepTotal > 60 ? (deepSleepTotal / 60 + "h" + (deepSleepTotal % 60 > 0 ? deepSleepTotal % 60 + "min" : "")) : deepSleepTotal + "min");
                simpleSleepValueTv.setText(simpleSleepTotal > 60 ? (simpleSleepTotal / 60 + "h" + (simpleSleepTotal % 60 > 0 ? simpleSleepTotal % 60 + "min" : "")) : simpleSleepTotal + "min");
                aweakSleepValueTv.setText(awakeSleepTotal + "min");

                DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p=decimalFormat.format(totalSleepTime / (float) 60);
                sleepAllTimeTv.setText(p);

                float t = Float.parseFloat(p);
                String level = "";
                if(t <= 10 && t >- 7) {
                    level = "优秀";
                }else if(t < 7 && t >= 5){
                    level = "良好";
                }else {
                    level = "较差";
                }
                sleepLevelTv.setText(level);

//                for (int i = 0; i < datas.length; i++) {
//                    int r = new Random().nextInt(100);
//                    datas[i] = r;
//                }

                weekHv.setDatas(datas);
            }else {
                for(int i = 0; i < datas.length; i++) {
                    datas[i] = 0;
                }
                weekHv.setDatas(datas);

                deepSleepPerTv.setText("");
                simpleSleepPerTv.setText("");
                awakeSleepPerTv.setText("");
                deepSleepValueTv.setText("-h-m");
                simpleSleepValueTv.setText("-h-m");
                aweakSleepValueTv.setText("-m");
                sleepAllTimeTv.setText("   "+ "--");
                sleepHTv.setText("h" + "  ");
                sleepLevelTv.setText(mContext.getResources().getString(R.string.no_data));
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            timeSlotTv.setText(sdf.format(weeks.get(0)) + "~" + sdf.format(weeks.get(weeks.size() - 1)));

        }
    };

    private void loadDatas() {
        new Thread() {
            @Override
            public void run() {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date mdate = sdf.parse(date);
                    weeks = dateToWeek(mdate);
                    LogUtils.debugInfo("ATG", ("今天的日期: " + sdf.format(mdate)));
                    for (Date date1 : weeks) {
                        LogUtils.debugInfo("TAG", sdf.format(date1));
                    }

                    String startTime = sdf.format(weeks.get(0)) + " 00:00:00";
                    String endTime = sdf.format(weeks.get(weeks.size() - 1)) + " 23:59:59";

                    sleepModelList = SqlHelper.instance().getWeekSleepListByTime(MyApplication.account, startTime, endTime);

                    mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

    public View getView() {
        return this.view;
    }

    public String getDate() {
        return this.date;
    }

}
