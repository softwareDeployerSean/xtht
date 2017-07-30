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
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerMonthSelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.MonthSelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MonthSelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.MonthSelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.HistogramView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MonthSelectedFragment extends BaseFragment<MonthSelectedPresenter> implements MonthSelectedContract.View {
    @BindView(R.id.month_hv)
    HistogramView monthHv;

    private String date;

    private List<String> months;

    List<SleepModel> sleepModelList = null;

    @BindView(R.id.time_slot_tv)
    public TextView timeSlotTv;

    @BindView(R.id.sleep_deep_per_tv)
    public TextView deepSleepPerTv;

    @BindView(R.id.sleep_simple_per_tv)
    public TextView simpleSleepPerTv;

    @BindView(R.id.sleep_aweak_per_tv)
    public TextView awakeSleepPerTv;

    @BindView(R.id.sleep_deep_value_tv)
    public TextView deepSleepValueTv;
    @BindView(R.id.sleep_simple_value_tv)
    public TextView simpleSleepValueTv;
    @BindView(R.id.sleep_aweak_value_tv)
    public TextView aweakSleepValueTv;

    @BindView(R.id.sleep_all_time_tv)
    public TextView sleepAllTimeTv;

    @BindView(R.id.sleep_level_tv)
    public TextView sleepLevelTv;

    public static MonthSelectedFragment newInstance() {
        MonthSelectedFragment fragment = new MonthSelectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMonthSelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .monthSelectedModule(new MonthSelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

//        Calendar a = Calendar.getInstance();
//        a.set(Calendar.DATE, 1);
//        a.roll(Calendar.DATE, -1);
//        int maxDate = a.get(Calendar.DATE);
//
//        String[] xLables = new String[maxDate];
//        for(int i = 0; i < maxDate; i++) {
//            xLables[i] = String.valueOf(i + 1);
//        }
//
//        int[] datas = new int[xLables.length];
//        for (int i = 0; i < datas.length; i++) {
//            int r = new Random().nextInt(100);
//            datas[i] = r;
//        }
//
//        monthHv.setxLables(xLables);
//        monthHv.setDatas(datas);
//        monthHv.setxDisplayType(1);
//        monthHv.setIntervalPercent(0.7f);
//        Log.d("TAG", "Color.RED=" + Color.RED);
//        monthHv.setStartColor(Color.parseColor("#6B289B"));
//        monthHv.setEndColor(Color.parseColor("#D0B3EB"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            months = getMonthList(sdf.parse(date));
            for(int i = 0; i < months.size(); i++) {
                Log.d("TAG", months.get(i) + "============");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        String[] xLables = new String[months.size()];
        for(int i = 0; i < months.size(); i++) {
            xLables[i] = String.valueOf(i + 1);
        }
        monthHv.setxLables(xLables);
        monthHv.setxDisplayType(1);
        monthHv.setIntervalPercent(0.7f);
        Log.d("TAG", "Color.RED=" + Color.RED);
        monthHv.setStartColor(Color.parseColor("#6B289B"));
        monthHv.setEndColor(Color.parseColor("#D0B3EB"));
        loadDatas();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int[] datas = new int[months.size()];
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//            sleepModelList = new ArrayList();
//            SleepModel s1 = new SleepModel();
//            s1.setTotalTime(380);
//            s1.setDate(months.get(0));
//            s1.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s1.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s1.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            sleepModelList.add(s1);

//            SleepModel s2 = new SleepModel();
//            s2.setTotalTime(390);
//            s2.setDate(months.get(1));
//            s2.setDuraionTimeArray(new int[] {30,15,45,30,30,30,45,30,30,15,15,15,45,15,15});
//            s2.setTimePointArray(new int[] {1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388});
//            s2.setSleepStatusArray(new int[] {1,0,1,0,2,0,1,2,1,0,1,0,1,1,2});
//            sleepModelList.add(s2);

//            SleepModel s3 = new SleepModel();
//            s3.setTotalTime(400);
//            s3.setDuraionTimeArray(new int[] {30,15,45,30,30,30,45,30,30,15,15,15,45,15,15});
//            s3.setTimePointArray(new int[] {1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388});
//            s3.setSleepStatusArray(new int[] {1,0,1,0,2,0,1,2,1,0,1,0,1,1,2});
//            s3.setDate(months.get(2));
//            sleepModelList.add(s3);

//            SleepModel s4 = new SleepModel();
//            s4.setTotalTime(370);
//            s4.setDate(months.get(3));
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
//            s5.setDate(months.get(4));
//            sleepModelList.add(s5);
//
//            SleepModel s6 = new SleepModel();
//            s6.setTotalTime(120);
//            s6.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s6.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s6.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            s6.setDate(months.get(5));
//            sleepModelList.add(s6);
//
//            SleepModel s7 = new SleepModel();
//            s7.setTotalTime(50);
//            s7.setDuraionTimeArray(new int[]{30, 15, 45, 30, 30, 30, 45, 30, 30, 15, 15, 15, 45, 15, 15});
//            s7.setTimePointArray(new int[]{1438, 13, 58, 88, 118, 148, 193, 223, 253, 268, 283, 298, 343, 373, 388});
//            s7.setSleepStatusArray(new int[]{1, 0, 1, 0, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2});
//            s7.setDate(months.get(6));
//            sleepModelList.add(s7);

            int deepSleepTotal = 0;
            int simpleSleepTotal = 0;
            int awakeSleepTotal = 0;
            int totalSleepTime = 0;
            if (sleepModelList != null && sleepModelList.size() > 0) {
                SleepModel sleepModel = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < sleepModelList.size(); i++) {
                    sleepModel = (SleepModel) sleepModelList.get(i);
                    totalSleepTime += sleepModel.getTotalTime();
                    String sleepDate = sleepModel.getDate();
                    String weekDate = months.get(i);
                    int a = -1;
                    for (int z = 0; i < months.size(); z++) {
                        if (months.get(z).equals(sleepModel.getDate())) {
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
                aweakSleepValueTv.setText(awakeSleepTotal > 60 ? (awakeSleepTotal / 60 + "h" + (awakeSleepTotal % 60 > 0 ? awakeSleepTotal % 60 + "min" : "")) : awakeSleepTotal + "min");

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

//                Calendar a = Calendar.getInstance();
//                a.set(Calendar.DATE, 1);
//                a.roll(Calendar.DATE, -1);
//                int maxDate = a.get(Calendar.DATE);



//                int[] datas = new int[xLables.length];
//                for (int i = 0; i < datas.length; i++) {
//                    int r = new Random().nextInt(100);
//                    datas[i] = r;
//                }


                monthHv.setDatas(datas);

            }else {
                for(int i = 0; i < datas.length; i++) {
                    datas[i] = 0;
                }
                monthHv.setDatas(datas);

                deepSleepPerTv.setText("");
                simpleSleepPerTv.setText("");
                awakeSleepPerTv.setText("");
                deepSleepValueTv.setText("");
                simpleSleepValueTv.setText("");
                aweakSleepValueTv.setText("");
                sleepAllTimeTv.setText("");
                sleepLevelTv.setText("");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            timeSlotTv.setText(months.get(0) + "~" + months.get(months.size() - 1));

        }
    };

    private void loadDatas() {

        new Thread(){
            @Override
            public void run() {
                try {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cal.setTime(sdf.parse(date));//month 为指定月份任意日期
                    int year = cal.get(Calendar.YEAR);
                    int m = cal.get(Calendar.MONTH) + 1;
                    sleepModelList = SqlHelper.instance().getMonthSleepListByMonth(MyApplication.account, year + "-" + m);

                    mHandler.sendEmptyMessage(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
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