package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.BloodPressure;
import com.walnutin.xtht.bracelet.ProductList.entity.HeartRateModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRateDetailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RateDetailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RateDetailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RateDetailPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.HomePageAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.SleepAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CanotSlidingViewpager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RateView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RateDetailActivity extends BaseActivity<RateDetailPresenter> implements RateDetailContract.View {


    @BindView(R.id.rate_detail_recycle)
    RecyclerView rateDetailRecyclerView;

    private String date;

    private String type;

    private SqlHelper sqlHelper;

    List heartRateList = null;

    List bloodPressureList = null;

    RateDetailAdapter adapter = null;

    Context context;

    @BindView(R.id.heart_rate_detail_view)
    RateView heartRateView;

    @BindView(R.id.text_left)
    TextView leftTextView;

    @BindView(R.id.text_middle)
    TextView middleTextView;

    @BindView(R.id.text_right)
    TextView rightTextView;

    @BindView(R.id.rate_avg_tv)
    TextView rateAvgTv;

    @BindView(R.id.rate_low_tv)
    TextView rateLowTv;

    @BindView(R.id.rate_hight_tv)
    TextView rateHightTv;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRateDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .rateDetailModule(new RateDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_rate_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        context=this;
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");
        sqlHelper = SqlHelper.instance();
        loadDates();

        if(type.equals("0")) {
            leftTextView.setText(getResources().getString(R.string.rate_avg));
            middleTextView.setText(getResources().getString(R.string.rate_low));
            rightTextView.setText(getResources().getString(R.string.rate_hight));

            rateAvgTv.setVisibility(View.VISIBLE);
            rateLowTv.setVisibility(View.VISIBLE);
            rateHightTv.setVisibility(View.VISIBLE);
        }else if(type.equals("1")) {
            leftTextView.setText("  " + "时" + "  " + "间" + "  ");
            middleTextView.setText(getResources().getString(R.string.blood_low));
            rightTextView.setText(getResources().getString(R.string.blood_hight) + "  ");

            rateAvgTv.setVisibility(View.GONE);
            rateLowTv.setVisibility(View.GONE);
            rateHightTv.setVisibility(View.GONE);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(type.equals("0")) {
                if(heartRateList != null && heartRateList.size() > 0) {
                    int totalRate = 0;
                    int lowestRate = 0;
                    int highestRate = 0;
                    for (int i = 0; i < heartRateList.size(); i++) {
                        HeartRateModel heartRateModel = (HeartRateModel) heartRateList.get(i);
                        totalRate += heartRateModel.getCurrentRate();
                        if (i == 0) {
                            lowestRate = heartRateModel.getLowRate();
                            highestRate = heartRateModel.getHighRate();
                        } else {
                            if (heartRateModel.getLowRate() < lowestRate) {
                                lowestRate = heartRateModel.getLowRate();
                            }
                            if (heartRateModel.getHighRate() > highestRate) {
                                highestRate = heartRateModel.getHighRate();
                            }
                        }
                    }
                    rateAvgTv.setText(String.valueOf(totalRate / heartRateList.size()));
                    rateLowTv.setText(String.valueOf(lowestRate));
                    rateHightTv.setText(String.valueOf(highestRate));
                }else {
                    rateAvgTv.setText("--");
                    rateLowTv.setText("--");
                    rateHightTv.setText("--");
                }
            }else if(type.equals("1")) {

            }
            setRateView();
            adapter = new RateDetailAdapter(RateDetailActivity.this, heartRateList, bloodPressureList, type);
            rateDetailRecyclerView.setLayoutManager(new LinearLayoutManager(RateDetailActivity.this));
//            rateDetailRecyclerView.addItemDecoration(new RecycleViewDivider(
//                    RateDetailActivity.this, LinearLayoutManager.HORIZONTAL, R.drawable.divider_mileage));
            rateDetailRecyclerView.setAdapter(adapter);
        }
    };

    private void loadDates() {
        new Thread() {
            @Override
            public void run() {
                if(type.equals("0")) {
                    heartRateList = sqlHelper.getOneDayHeartRateInfo(MyApplication.account, date);
                }else if(type.equals("1")) {
                    bloodPressureList = sqlHelper.getOneDayBloodPressureInfo(MyApplication.account, date);
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void setRateView() {
        if(type.equals("0")) {
            heartRateView.setLineColor(Color.parseColor("#6AD489"));
            heartRateView.setBrokenLineColor(Color.parseColor("#D23B3B"));

            String[] yLables = new String[150];
            for (int i = 0; i < yLables.length; i++) {
                yLables[i] = String.valueOf(i);
            }

            heartRateView.setYlabel(yLables);

            String[] xLabels = new String[24];
            int[] datas = new int[24];

            for (int i = 0; i < xLabels.length; i++) {
                xLabels[i] = String.valueOf(i);
            }

//            heartRateList = new ArrayList();
//            HeartRateModel heartRateModel1 = new HeartRateModel();
//            heartRateModel1.setCurrentRate(100);
//            heartRateModel1.setTestMomentTime("2017-07-10 15:30:30");
//            heartRateList.add(heartRateModel1);
//
//            HeartRateModel heartRateModel2 = new HeartRateModel();
//            heartRateModel2.setCurrentRate(90);
//            heartRateModel2.setTestMomentTime("2017-07-10 15:20:30");
//            heartRateList.add(heartRateModel2);
//
//            HeartRateModel heartRateModel3 = new HeartRateModel();
//            heartRateModel3.setCurrentRate(100);
//            heartRateModel3.setTestMomentTime("2017-07-10 16:30:30");
//            heartRateList.add(heartRateModel3);
//
//            HeartRateModel heartRateModel4 = new HeartRateModel();
//            heartRateModel4.setCurrentRate(90);
//            heartRateModel4.setTestMomentTime("2017-07-10 16:20:30");
//            heartRateList.add(heartRateModel4);

            if(heartRateList != null && heartRateList.size() > 0) {
                HeartRateModel heartRateModel = null;
                int hourOfTotal = 0;
                int hour = 0;
                int oneHourCount = 0;
                for(int i = 0; i < heartRateList.size(); i++) {
                    heartRateModel = (HeartRateModel) heartRateList.get(i);
                    if(i == 0) {
                        hourOfTotal = heartRateModel.getCurrentRate();
                        hour = getHourByDate(heartRateModel.getTestMomentTime());
                        oneHourCount++;
                        if(heartRateList.size() == 1) {
                            datas[hour] = hourOfTotal;
                        }
                    }else {
                        if(hour == getHourByDate(heartRateModel.getTestMomentTime())) {
                            hour = getHourByDate(heartRateModel.getTestMomentTime());
                            hourOfTotal += heartRateModel.getCurrentRate();
                            oneHourCount ++;
                            if(i == heartRateList.size() - 1){
                                datas[hour] = hourOfTotal / oneHourCount;
                            }
                        }else {
                            datas[hour] = hourOfTotal / oneHourCount;

                            hourOfTotal = heartRateModel.getCurrentRate();
                            hour = getHourByDate(heartRateModel.getTestMomentTime());
                            oneHourCount = 1;

                            if(i == heartRateList.size() - 1) {
                                datas[hour] = hourOfTotal / oneHourCount;
                            }
                        }
                    }
                }
            }

            heartRateView.setDatas(datas);
            heartRateView.setXlabel(xLabels);

            int max = getMaxArray(datas);
            int min = getMinArray(datas);

            int linMax = ((int) ((max + 20) / 5)) * 5 + 5;

            String[] yLabels = new String[linMax + linMax / 5];
            for (int i = 0; i < yLabels.length; i++) {
                yLabels[i] = String.valueOf(i);
            }
            heartRateView.setYlabel(yLabels);
            heartRateView.setxDisplayInterval(2);

            int a = linMax / 4;

            int total = 0;
            int countLength = 0;
            for (int i = 0; i < datas.length; i++) {
                total += datas[i];
                if(datas[i] != 0) {
                    countLength += 1;
                }
            }
            if(countLength > 0) {
                int[] brokenLineDisplay = new int[]{total / countLength};
                heartRateView.setBrolenLineDisplay(brokenLineDisplay);
            }

            int[] yDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
            heartRateView.setyDisPlay(yDisplay);
        }else if(type.equals("1")) {
            heartRateView.setLineColor(Color.parseColor("#6AD489"));
            heartRateView.setBrokenLineColor(Color.parseColor("#D23B3B"));

            String[] yLables = new String[150];
            for (int i = 0; i < yLables.length; i++) {
                yLables[i] = String.valueOf(i);
            }

            heartRateView.setYlabel(yLables);

            String[] xLabels = new String[24];
            int[] datas = new int[24];
            int[] datas2 = new int[24];

            for (int i = 0; i < xLabels.length; i++) {
                xLabels[i] = String.valueOf(i);
            }

//            bloodPressureList = new ArrayList();
//            BloodPressure bloodPressure1 = new BloodPressure();
//            bloodPressure1.setTestMomentTime("2017--7-10 15:30:30");
//            bloodPressure1.setDiastolicPressure(120);
//            bloodPressure1.setSystolicPressure(80);
//            bloodPressureList.add(bloodPressure1);
//
//            BloodPressure bloodPressure2 = new BloodPressure();
//            bloodPressure2.setTestMomentTime("2017--7-10 15:20:30");
//            bloodPressure2.setDiastolicPressure(110);
//            bloodPressure2.setSystolicPressure(70);
//            bloodPressureList.add(bloodPressure2);
//
//            BloodPressure bloodPressure3 = new BloodPressure();
//            bloodPressure3.setTestMomentTime("2017--7-10 16:20:30");
//            bloodPressure3.setDiastolicPressure(110);
//            bloodPressure3.setSystolicPressure(70);
//            bloodPressureList.add(bloodPressure3);
//
//            BloodPressure bloodPressure4 = new BloodPressure();
//            bloodPressure4.setTestMomentTime("2017--7-10 16:30:30");
//            bloodPressure4.setDiastolicPressure(100);
//            bloodPressure4.setSystolicPressure(90);
//            bloodPressureList.add(bloodPressure4);

            if(bloodPressureList != null && bloodPressureList.size() > 0) {
                BloodPressure bloodPressure = null;
                int diaHourOfBlood = 0;
                int sysHourOfBlood = 0;
                int hour = 0;
                int oneHourCount = 0;
                for(int i = 0; i < bloodPressureList.size(); i++) {
                    bloodPressure = (BloodPressure) bloodPressureList.get(i);
                    if(i == 0) {
                        diaHourOfBlood = bloodPressure.getDiastolicPressure();
                        sysHourOfBlood = bloodPressure.getSystolicPressure();
                        hour = getHourByDate(bloodPressure.getTestMomentTime());
                        oneHourCount++;
                        if(bloodPressureList.size() == 1) {
                            datas[hour] = diaHourOfBlood;
                            datas2[hour] = sysHourOfBlood;
                        }
                    }else {
                        if(hour == getHourByDate(bloodPressure.getTestMomentTime())) {
                            hour = getHourByDate(bloodPressure.getTestMomentTime());
                            diaHourOfBlood += bloodPressure.getDiastolicPressure();
                            sysHourOfBlood += bloodPressure.getSystolicPressure();
                            oneHourCount ++;
                            if(i == bloodPressureList.size() - 1){
                                datas[hour] = diaHourOfBlood / oneHourCount;
                                datas2[hour] = sysHourOfBlood / oneHourCount;
                            }
                        }else {
                            datas[hour] = diaHourOfBlood / oneHourCount;
                            datas2[hour] = sysHourOfBlood / oneHourCount;

                            hour = getHourByDate(bloodPressure.getTestMomentTime());
                            diaHourOfBlood += bloodPressure.getDiastolicPressure();
                            sysHourOfBlood += bloodPressure.getSystolicPressure();
                            oneHourCount = 1;

                            if(i == bloodPressureList.size() - 1){
                                datas[hour] = diaHourOfBlood / oneHourCount;
                                datas2[hour] = sysHourOfBlood / oneHourCount;
                            }
                        }
                    }
                }
            }

            heartRateView.setDatas(datas);
            heartRateView.setDatas2(datas2);
            heartRateView.setXlabel(xLabels);

            int max = getMaxArray(datas);
            int min = getMinArray(datas);

            int linMax = ((int) ((max + 20) / 5)) * 5 + 5;

            String[] yLabels = new String[linMax + linMax / 5];
            for (int i = 0; i < yLabels.length; i++) {
                yLabels[i] = String.valueOf(i);
            }
            heartRateView.setYlabel(yLabels);
            heartRateView.setxDisplayInterval(2);

            int a = linMax / 4;

            int total = 0;
            int countTotal = 0;
            for (int i = 0; i < datas.length; i++) {
                total += datas[i];
                if(datas[i] != 0) {
                    countTotal += 1;
                }
            }
            if(countTotal > 0) {
                int[] brokenLineDisplay = new int[]{total / countTotal};
                heartRateView.setBrolenLineDisplay(brokenLineDisplay);
            }

            int[] yDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
            heartRateView.setyDisPlay(yDisplay);
            heartRateView.setBrokenLineColor2(Color.parseColor("#8BE1D0"));

            heartRateView.setDrawVLine(true);
        }
    }

    private int getHourByDate(String date) {
        int hour = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            Date d = sdf.parse(date);
            calendar.setTime(d);

            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return hour;
    }

    private int getMaxArray(int[] array) {
        int i, min, max;

        min = max = array[0];
        System.out.print("数组A的元素包括：");
        for (i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if (array[i] > max)   // 判断最大值
                max = array[i];
            if (array[i] < min)   // 判断最小值
                min = array[i];
        }
        return max;
    }

    private int getMinArray(int[] array) {
        int i, min, max;

        min = max = array[0];
        System.out.print("数组A的元素包括：");
        for (i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if (array[i] > max)   // 判断最大值
                max = array[i];
            if (array[i] < min)   // 判断最小值
                min = array[i];
        }
        return min;
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
        finish();
    }


    class RateDetailAdapter extends RecyclerView.Adapter<RateDetailAdapter.MyViewHolder> {

        private List heartRateList = null;

        private List bloodPressureList = null;

        private Context mContext;

        private String type;

        public RateDetailAdapter(Context context, List heartRateList, List bloodPressureList, String type) {
            this.mContext = context;
            this.heartRateList = heartRateList;
            this.bloodPressureList = bloodPressureList;
            this.type = type;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RateDetailAdapter.MyViewHolder holder = new RateDetailAdapter.MyViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.rate_detail_rv_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if(type.equals("0")) {
                holder.middleTv.setTextColor(Color.WHITE);
                HeartRateModel heartRateModel = (HeartRateModel) this.heartRateList.get(position);
                holder.leftTv.setText(getHourAndMin(heartRateModel.getTestMomentTime()));
                holder.rightTv.setText(heartRateModel.getCurrentRate() + " bpm");
            }else if(type.equals("1")) {
                holder.middleTv.setTextColor(getResources().getColor(R.color.black_444444));

                BloodPressure bloodPressure = (BloodPressure) bloodPressureList.get(position);
                holder.leftTv.setText(getHourAndMin(bloodPressure.getTestMomentTime()));
                holder.middleTv.setText(String.valueOf(bloodPressure.getSystolicPressure()));
                holder.rightTv.setText(String.valueOf(bloodPressure.getDiastolicPressure()));
            }


        }

        private String getHourAndMin(String data) {
            String ret = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                Date d = sdf.parse(data);
                calendar.setTime(d);

                ret = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            }catch (ParseException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public int getItemCount() {
            return type.equals("0") ? heartRateList.size() : bloodPressureList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView leftTv;
            TextView middleTv;
            TextView rightTv;

            public MyViewHolder(View view) {
                super(view);
                leftTv = (TextView) view.findViewById(R.id.left_tv);
                middleTv = (TextView) view.findViewById(R.id.middle_tv);
                rightTv = (TextView) view.findViewById(R.id.right_tv);
            }
        }
    }

}