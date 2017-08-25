package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SleepLinearlayout;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SleepTagLinearlayout;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by Leiht on 2017/7/31.
 */

public class SleepDayPageItem {

    private Context mContext;

    private int position;

    View view;

    public SwitchView clockSwitchView;

    public TextView dayTv;

    public TextView sleepStartTime;

    public TextView sleepEndTime;

    public TextView planSleepTime;

    public TextView actualSleepTime;

    public TextView lineStartTime;

    public TextView lineEndTime;

    public TextView deepSleepPerTv;

    public TextView simpleSleepPerTv;

    public TextView awakeSleepPerTv;

    public TextView deepSleepValueTv;

    public TextView simpleSleepValueTv;

    public TextView aweakSleepValueTv;

    public TextView sleepAllTimeTv;

    public TextView sleepLevelTv;

    public TextView sleepHTv;

    public RelativeLayout clockRelativeLayout;

    public RelativeLayout dayTimeRelativeLayout;

    public RelativeLayout dayTimeRelativeLayout2;

    public RelativeLayout dayTimeRelativeLayout3;

    public RelativeLayout dayTimeRelativeLayout4;

    public LinearLayout sleepAnilayLinearlayout;

    public ImageView otherCode;

    public SleepLinearlayout sleepLinearlayout;
    public SleepTagLinearlayout sleepTagLinearlayout;
    public LinearLayout noDataLinearLayout;

    private String date;

    private SleepModel sleepModel = null;

    public SleepDayPageItem(Context context) {
        this.mContext = context;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.sleep_day_selected_item, null);

        clockSwitchView = (SwitchView) view.findViewById(R.id.day_sleep_sw);
        sleepStartTime = (TextView) view.findViewById(R.id.sleep_start_time);
        sleepEndTime = (TextView) view.findViewById(R.id.sleep_end_time);
        planSleepTime = (TextView) view.findViewById(R.id.plan_sleep_time);
        actualSleepTime = (TextView) view.findViewById(R.id.actual_sleep_time);
        lineStartTime = (TextView) view.findViewById(R.id.line_start_time);
        lineEndTime = (TextView) view.findViewById(R.id.line_end_time);
        deepSleepPerTv = (TextView) view.findViewById(R.id.sleep_deep_per_tv);
        simpleSleepPerTv = (TextView) view.findViewById(R.id.sleep_simple_per_tv);
        awakeSleepPerTv = (TextView) view.findViewById(R.id.sleep_aweak_per_tv);
        deepSleepValueTv = (TextView) view.findViewById(R.id.sleep_deep_value_tv);
        simpleSleepValueTv = (TextView) view.findViewById(R.id.sleep_simple_value_tv);
        aweakSleepValueTv = (TextView) view.findViewById(R.id.sleep_aweak_value_tv);
        sleepAllTimeTv = (TextView) view.findViewById(R.id.sleep_all_time_tv);
        sleepLevelTv = (TextView) view.findViewById(R.id.sleep_level_tv);
        sleepHTv = (TextView) view.findViewById(R.id.sleep_h_tv);
        dayTv = (TextView) view.findViewById(R.id.day_tv);

        clockRelativeLayout = (RelativeLayout) view.findViewById(R.id.clock_relativelayout);
        dayTimeRelativeLayout = (RelativeLayout) view.findViewById(R.id.day_time_relativelayout);
        dayTimeRelativeLayout2 = (RelativeLayout) view.findViewById(R.id.day_time_relativelayout2);
        dayTimeRelativeLayout3 = (RelativeLayout) view.findViewById(R.id.day_time_relativelayout3);
        dayTimeRelativeLayout4 = (RelativeLayout) view.findViewById(R.id.day_time_relativelayout4);
        sleepAnilayLinearlayout = (LinearLayout) view.findViewById(R.id.sleep_anilay_linearlayout);
        otherCode = (ImageView) view.findViewById(R.id.other_code);

        sleepLinearlayout = (SleepLinearlayout) view.findViewById(R.id.sleep_data_linearlayout);
        sleepTagLinearlayout = (SleepTagLinearlayout) view.findViewById(R.id.sleep_data_linearlayout2);
        noDataLinearLayout = (LinearLayout) view.findViewById(R.id.no_data_linearlayout);

    }

    public void update(String date) {
        this.date = date;
        showOrDismissLayout();
        loadDatas();
        sleepLinearlayout.setdatas(date);
        sleepTagLinearlayout.setdatas(date);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            sleepModel = new SleepModel();
//            sleepModel.setDate("2017-07-29");
//            sleepModel.setDuraionTimeArray(new int[] {30,15,45,30,30,30,45,30,30,15,15,15,45,15,15});
//            sleepModel.setTimePointArray(new int[] {1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388});
//            sleepModel.setSleepStatusArray(new int[] {1,0,1,0,2,0,1,2,1,0,1,0,1,1,2});
            if (sleepModel != null) {
                sleepHTv.setText("h");
                sleepLinearlayout.setVisibility(View.VISIBLE);
                sleepTagLinearlayout.setVisibility(View.VISIBLE);
                noDataLinearLayout.setVisibility(View.GONE);

                int[] duraionTimeArray = sleepModel.getDuraionTimeArray();
                int[] timePointArray = sleepModel.getTimePointArray();
                int[] sleepStatusArray = sleepModel.getSleepStatusArray();
                sleepStartTime.setText((timePointArray[0] - duraionTimeArray[0]) / 60 + ":" + (timePointArray[0] - duraionTimeArray[0]) % 60);
                sleepEndTime.setText((timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) / 60 + ":" + (timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) % 60);
                lineEndTime.setText((timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) / 60 + ":" + (timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) % 60);
                lineStartTime.setText((timePointArray[0] - duraionTimeArray[0]) / 60 + ":" + (timePointArray[0] - duraionTimeArray[0]) % 60);

                int totalSleepTime = 0;
//                if(duraionTimeArray != null && duraionTimeArray.length > 0) {
//                    for (int i = 0; i < duraionTimeArray.length; i++) {
//                        totalSleepTime += duraionTimeArray[i];
//                    }
//                }
                totalSleepTime = sleepModel.getTotalTime();
                actualSleepTime.setText(totalSleepTime > 60 ? (totalSleepTime / 60 + "小时" + (totalSleepTime % 60 > 0 ? totalSleepTime % 60 + "分" : "")) : totalSleepTime + "分");

                int deepSleepTotal = 0;
                int simpleSleepTotal = 0;
                int awakeSleepTotal = 0;
//                if(sleepStatusArray != null && sleepStatusArray.length > 0) {
//                    for(int i = 0; i < sleepStatusArray.length; i++) {
//                        if(sleepStatusArray[i] == 2) {
//                            awakeSleepTotal += duraionTimeArray[i];
//                        }else if(sleepStatusArray[i] == 1) {
//                            deepSleepTotal += duraionTimeArray[i];
//                        }else if(sleepStatusArray[i] == 0) {
//                            simpleSleepTotal += duraionTimeArray[i];
//                        }
//                    }
//                }
                deepSleepTotal = sleepModel.getDeepTime();
                simpleSleepTotal = sleepModel.getLightTime();
                awakeSleepTotal = sleepModel.getTotalTime() - deepSleepTotal - simpleSleepTotal;
//                awakeSleepTotal = sleepModel.getSoberTime();
                if ((deepSleepTotal + simpleSleepTotal + awakeSleepTotal) > 0) {
                    float a1 = ((((float) deepSleepTotal / (deepSleepTotal + simpleSleepTotal + awakeSleepTotal))) * 100);
                    float b1 = ((((float) simpleSleepTotal / (deepSleepTotal + simpleSleepTotal + awakeSleepTotal))) * 100);
                    String a2 = new BigDecimal(a1).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                    String b2 = new BigDecimal(b1).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                    String a = a2 + "%";
                    String b = b2 + "%";
                    String c = (100 - Integer.parseInt(a2) - Integer.parseInt(b2)) + "%";
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

                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p = decimalFormat.format(totalSleepTime / (float) 60);
                sleepAllTimeTv.setText(p);

                float t = Float.parseFloat(p);
                String level = "";
                if (t <= 10 && t > -7) {
                    level = "优秀";
                } else if (t < 7 && t >= 5) {
                    level = "良好";
                } else {
                    level = "较差";
                }
                sleepLevelTv.setText(level);
            } else {
                sleepLinearlayout.setVisibility(View.GONE);
                sleepTagLinearlayout.setVisibility(View.GONE);
                noDataLinearLayout.setVisibility(View.VISIBLE);

                sleepStartTime.setText("");
                sleepEndTime.setText("");
                lineEndTime.setText("");
                lineStartTime.setText("");
                actualSleepTime.setText("");
                deepSleepPerTv.setText("");
                simpleSleepPerTv.setText("");
                awakeSleepPerTv.setText("");
                deepSleepValueTv.setText("--h--m");
                simpleSleepValueTv.setText("--h--m");
                aweakSleepValueTv.setText("--m");
                sleepAllTimeTv.setText("   " + "--");
                sleepHTv.setText("h" + "  ");
                sleepLevelTv.setText(mContext.getResources().getString(R.string.no_data));
            }
        }
    };

    private void loadDatas() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            dayTv.setText((c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new Thread() {
            @Override
            public void run() {
                sleepModel = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, date);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void showOrDismissLayout() {
        if (isNow()) {
            clockRelativeLayout.setVisibility(View.VISIBLE);
            dayTimeRelativeLayout.setVisibility(View.VISIBLE);
            dayTimeRelativeLayout2.setVisibility(View.VISIBLE);
            dayTimeRelativeLayout3.setVisibility(View.VISIBLE);
            dayTimeRelativeLayout4.setVisibility(View.VISIBLE);
            sleepAnilayLinearlayout.setVisibility(View.VISIBLE);
            otherCode.setVisibility(View.VISIBLE);
        } else {
            clockRelativeLayout.setVisibility(View.GONE);
            dayTimeRelativeLayout.setVisibility(View.GONE);
            dayTimeRelativeLayout2.setVisibility(View.GONE);
            dayTimeRelativeLayout3.setVisibility(View.GONE);
            dayTimeRelativeLayout4.setVisibility(View.GONE);
            sleepAnilayLinearlayout.setVisibility(View.GONE);
            otherCode.setVisibility(View.GONE);
        }
    }

    private boolean isNow() {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //获取今天的日期
        String nowDay = sf.format(now);
        Date d = null;
        try {
            d = sf.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String comDate = sf.format(d);
        //对比的时间
        String day = sf.format(d);

        LogUtils.debugInfo("nowDay=" + nowDay + ", comDate=" + comDate);
        LogUtils.debugInfo("day.equals(nowDay)=" + day.equals(nowDay));

        return comDate.equals(nowDay);

    }

    public String getDate() {
        return this.date;
    }

    public View getView() {
        return this.view;
    }
}
