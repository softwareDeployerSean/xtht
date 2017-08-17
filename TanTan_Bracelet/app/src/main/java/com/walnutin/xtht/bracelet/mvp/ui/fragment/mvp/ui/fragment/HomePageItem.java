package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.HardSdk;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHardSdkCallback;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.BloodPressure;
import com.walnutin.xtht.bracelet.ProductList.entity.HeartRateModel;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfo;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.HealthPageData;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.DataShowActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RateDetailActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.HomePageAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CylinderView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;
import com.walnutin.xtht.bracelet.mvp.ui.widget.StepArcView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Leiht on 2017/7/2.
 */

public class HomePageItem implements IHardSdkCallback {

    private Context mContext;

    private int position;

    View view;

    private TextView currentDateTv;

    public TextView dayTv;

    private ImageView sleepIconIv;

    private StepArcView stepArcView;

    private CylinderView cylinderView;

    private RecyclerView healthRv;

    private ListView healhListView;

    private TextView calorieTv;

    private TextView distanceTv;

    private TextView heartRateTv;

    private List<HealthPageData> healthDatas;

    private HomePageAdapter homePagerAdapter;

    private CustomLinearLayoutManager mLayoutManager;

    private MainFragment mainFragment;

    private String date = "";

    private SqlHelper sqlHelper;

    public String getDate() {
        return this.date;
    }

    private StepInfos stepInfos;

    private List heartRateList;

    private HeartRateModel latelyHeartRateModel;

    private List bloodPressureList;

    private BloodPressure latelyBloodPressure;

    private SleepModel sleepModel;

    private HeartRateModel todayHeartRateModel;

    private int count = 0;

    private int minRateSP = -1;
    private int maxRateSP = -1;
    private int minDisPlayRate = -1;
    private int maxDisplayRate = -1;

    private int minBlood = -1;
    private int maxBlood = -1;
    private int minBloodTemp = -1;
    private int maxBloodTemp = -1;

    //手环交互
    HardSdk hardSdk;
    private int todayStep = 0;
    private float todayDistance = 0;
    private int todayCalories = 0;

    public HomePageItem(Context context, MainFragment mainFragment) {
        this.mContext = context;
        this.mainFragment = mainFragment;

        sqlHelper = SqlHelper.instance();

        this.view = LayoutInflater.from(mContext).inflate(R.layout.fragment_main_item, null);

        currentDateTv = (TextView) view.findViewById(R.id.current_date_tv);

        dayTv = (TextView) view.findViewById(R.id.tv_day);

        dayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainFragment.toDateActivity(date);
            }
        });

        sleepIconIv = (ImageView) view.findViewById(R.id.iv_data);

        sleepIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DataShowActivity.class);
                intent.putExtra("date", date);
                mContext.startActivity(intent);
            }
        });

        stepArcView = (StepArcView) view.findViewById(R.id.arc_view);

        stepArcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepArcView.setVisibility(View.GONE);
                cylinderView.setVisibility(View.VISIBLE);

                mainFragment.setArcOrLinder(false);
            }
        });

        cylinderView = (CylinderView) view.findViewById(R.id.cylinder_view);

        cylinderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cylinderView.setVisibility(View.GONE);
                stepArcView.setVisibility(View.VISIBLE);

                mainFragment.setArcOrLinder(true);
            }
        });

        healthRv = (RecyclerView) view.findViewById(R.id.health_rv);

        healhListView = (ListView) view.findViewById(R.id.health_listview);

        calorieTv = (TextView) view.findViewById(R.id.tv_calorie);

        distanceTv = (TextView) view.findViewById(R.id.tv_kilometre);

        heartRateTv = (TextView) view.findViewById(R.id.tv_heart_rate);

        if (healthDatas == null) {
            healthDatas = new ArrayList<>();
        }
        healthDatas.clear();
        //模拟从数据库查询当天对应的数据
        HealthPageData data1 = new HealthPageData(1, "14:25", "心率", "", "62-78", "78", "", true, 1, "");
        HealthPageData data2 = new HealthPageData(2, "14:25", "血压", "", "90", "110", "", true, 2, "");
        HealthPageData data3 = new HealthPageData(3, "14:25", "血氧", "", "90%", "", "", false, 3, "");
        HealthPageData data4 = new HealthPageData(4, "14:25", "低运动量", "12", "48", "42", "100", false, 4, "");
        HealthPageData data5 = new HealthPageData(5, "14:25", "散步", "12", "12", "42", "100", false, 5, "");
        HealthPageData data6 = new HealthPageData(6, "14:25", "跑步", "12", "48", "42", "100", false, 6, "");
        HealthPageData data7 = new HealthPageData(7, "14:25", "睡眠", "", "5h 42min", "良好", "", false, 7, "");
        HealthPageData data8 = new HealthPageData(8, "14:25", "摘下", "", "5h42min", "", "", false, 8, "");

        healthDatas.add(data1);
        healthDatas.add(data2);
        healthDatas.add(data3);
//        healthDatas.add(data4);
//        healthDatas.add(data5);
//        healthDatas.add(data6);
//        healthDatas.add(data7);
//        healthDatas.add(data8);


        homePagerAdapter = new HomePageAdapter(mContext, healthDatas, latelyHeartRateModel, latelyBloodPressure);

        mLayoutManager = new CustomLinearLayoutManager(mContext);

        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setAutoMeasureEnabled(true);

        mLayoutManager.setScrollEnabled(false);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        healthRv.setLayoutManager(new LinearLayoutManager(mContext));

        healthRv.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, R.drawable.divider_mileage));

        healthRv.setAdapter(homePagerAdapter);

        //手环
        hardSdk = HardSdk.getInstance();
    }

    private boolean isLast(Date date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //获取今天的日期
        String nowDay = sf.format(now);

        //对比的时间
        String day = sf.format(date);

        try {
            Date nowDate = sf.parse(nowDay);
            Date comDate = sf.parse(day);
            if ((nowDate.getTime() - comDate.getTime()) / 1000 / 60 / 60 / 24 == 1) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    private boolean isNow(Date date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //获取今天的日期
        String nowDay = sf.format(now);

        String comDate = sf.format(date);
        //对比的时间
        String day = sf.format(date);

        LogUtils.debugInfo("nowDay=" + nowDay + ", comDate=" + comDate);
        LogUtils.debugInfo("day.equals(nowDay)=" + day.equals(nowDay));

        return comDate.equals(nowDay);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            if (what == 0) {
                if (stepInfos != null) {
                    int stepGoal = stepInfos.getStepGoal();
                    if (stepGoal <= 0) {
                        stepGoal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                    }
                    int actualStep = stepInfos.getStep();
                    LogUtils.debugInfo("TAG", "stepGoal=" + stepGoal + ", actualStep=" + actualStep);
//            if (stepGoal == 0) {
//                stepGoal = 7000;
//                actualStep = new Random().nextInt(6000);
//            }
                    stepArcView.setCurrentCount(stepGoal, actualStep);

                    Map<Integer, Integer> stepOneHourInfo = stepInfos.getStepOneHourInfo();
                    int[] stepDatas = new int[24];
                    if (stepOneHourInfo != null) {
                        for (int i = 0; i < stepDatas.length; i++) {
                            if (stepOneHourInfo.containsKey(60 * (i + 1))) {
                                stepDatas[i] = stepOneHourInfo.get(60 * (i + 1));
                            } else {
                                stepDatas[i] = 0;
                            }
                        }
                    } else {
                        for (int i = 0; i < stepDatas.length; i++) {
//                    stepDatas[i] = 0;
//                    int r = new Random().nextInt(100);
                            stepDatas[i] = 0;
                        }
                    }
                    cylinderView.setDatas(stepDatas);

                    int calories = stepInfos.getCalories();
                    calorieTv.setText(calories == 0 ? "--" : String.valueOf(calories));

                    float distance = stepInfos.getDistance();
                    String distanceStr = decimalFormat.format(distance);
                    distanceTv.setText(distance == 0 ? "--" : distanceStr);

                    int rate = 0;
//                    if (heartRateList != null && heartRateList.size() > 0) {
//                        int totalRate = 0;
//                        HeartRateModel heartRateModel = null;
//                        for (int i = 0; i < heartRateList.size(); i++) {
//                            heartRateModel = (HeartRateModel) heartRateList.get(i);
//                            totalRate += heartRateModel.getCurrentRate();
//                        }
//                        rate = totalRate / heartRateList.size();
//
//                        latelyHeartRateModel = (HeartRateModel) heartRateList.get(heartRateList.size() - 1);
//                    } else {
//                        rate = 0;
//                    }
                    if (todayHeartRateModel != null) {
                        rate = todayHeartRateModel.getCurrentRate();
                    }
                    heartRateTv.setText(rate == 0 ? "--" : String.valueOf(rate));

                    if (bloodPressureList != null && bloodPressureList.size() > 0) {
                        latelyBloodPressure = (BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1);
                    }

                    //清除除心率，血压，血氧之外其它的数据，后面再动态添加
                    if (healthDatas != null && healthDatas.size() > 3) {
                        for (int i = 3; i < healthDatas.size(); i++) {
                            healthDatas.remove(i);
                        }
                    }

                    if (sleepModel != null) {
                        HealthPageData sleepPageData = new HealthPageData(7, "14:25", "睡眠", "", "5h 42min", "良好", "", false, 7, date);
                        String topText = "";
                        int allDurationTime = sleepModel.getAllDurationTime();
                        if (allDurationTime > 1 * 60 * 60) {
                            topText = (allDurationTime / 1 * 60 * 60) + "h " + ((allDurationTime % (1 * 60 * 60)) / 60) + "min";
                        } else {
                            topText = (allDurationTime / 60) + "min";
                        }
                        sleepPageData.setRightTop(topText);
                        String sleepStatusText = "";
                        if (allDurationTime >= 7 * 60 * 60 && allDurationTime <= 10 * 60 * 60) {
                            sleepStatusText = "优秀";
                        } else if (allDurationTime >= 5 * 60 * 60 && allDurationTime < 7 * 60 * 60) {
                            sleepStatusText = "良好";
                        } else {
                            sleepStatusText = "一般";
                        }

                        sleepPageData.setRightButtom(sleepStatusText);
                        healthDatas.add(sleepPageData);
                    }

                    //设置心率数据
                    if (todayHeartRateModel != null) {
                        int displayLow = 0;
                        int displayHight = 0;
                        if (minRateSP != -1 && minRateSP < todayHeartRateModel.getLowRate()) {
                            displayLow = minRateSP;
                        } else {
                            displayLow = todayHeartRateModel.getLowRate();
                        }

                        if (maxRateSP != -1 && maxRateSP > todayHeartRateModel.getHighRate()) {
                            displayHight = maxRateSP;
                        } else {
                            displayHight = todayHeartRateModel.getHighRate();
                        }
                        healthDatas.get(0).setRightTop(displayLow + "-" + displayHight);
                        healthDatas.get(0).setDate(date);

                        minDisPlayRate = displayLow;
                        maxDisplayRate = displayHight;
                    } else if (minRateSP != -1 && maxRateSP != -1) {
                        healthDatas.get(0).setRightTop(minRateSP + "-" + maxRateSP);
                        healthDatas.get(0).setDate(date);
                        minDisPlayRate = minRateSP;
                        maxDisplayRate = maxRateSP;
                    } else {
                        healthDatas.get(0).setRightTop("-- ");
                        healthDatas.get(0).setDate(date);
                    }

                    //设置血压数据
                    healthDatas.get(1).setRightTop(minBlood == -1 ? "--" : minBlood + "");
                    healthDatas.get(1).setRightButtom(maxBlood == -1 ? "--" : maxBlood + "");
                    healthDatas.get(1).setDate(date);

                    //设置血氧数据
                    healthDatas.get(2).setRightTop("--");
                    healthDatas.get(2).setDate(date);

                    homePagerAdapter.notifyDataSetChanged();
                }

            } else if (what == 1) {
                int stepGoal = stepInfos.getStepGoal();
                if (stepGoal <= 0) {
                    stepGoal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                }

                int actualStep = 0;
                float distance = 0;
                int calories = 0;
                if (stepInfos != null) {
                    actualStep = todayStep + stepInfos.getStep();
                    distance = todayDistance + stepInfos.getDistance();
                    calories = todayCalories + stepInfos.getCalories();
                } else {
                    actualStep = todayStep;
                    distance = todayDistance;
                    calories = todayCalories;
                }
                LogUtils.debugInfo("TAG", "stepGoal=" + stepGoal + ", actualStep=" + actualStep);
                stepArcView.setCurrentCount(stepGoal, actualStep);

                String distanceStr = decimalFormat.format(distance);
                distanceTv.setText(distance == 0 ? "--" : distanceStr);
                calorieTv.setText(calories == 0 ? "--" : String.valueOf(calories));
            }else if(what == 2) {
                RateSP rateSPTemp = DataHelper.getDeviceData(mContext, "rateSP");
                int minNow = 0;
                int maxNow = 0;
                if (rateSPTemp != null) {
                    String rateString = rateSPTemp.getRate();
                    if (rateString != null && rateString.length() > 0) {
                        String[] rates = rateString.split(",");
                        if (rates != null && rates.length > 0) {
                            minNow = maxNow = Integer.parseInt(rates[0]);
                            for (int i = 0; i < rates.length; i++) {
                                if (Integer.parseInt(rates[i]) > maxRateSP)   // 判断最大值
                                    maxNow = Integer.parseInt(rates[i]);
                                if (Integer.parseInt(rates[i]) < minRateSP)   // 判断最小值
                                    minNow = Integer.parseInt(rates[i]);
                            }
                        }
                    }
                }

                if(minNow < minDisPlayRate || maxNow > maxDisplayRate) {
                    healthDatas.get(0).setRightTop(minNow + "-" + maxDisplayRate);
                    healthDatas.get(0).setDate(date);
                    minDisPlayRate = minNow;
                    maxDisplayRate = maxNow;

                    homePagerAdapter.notifyDataSetChanged();
                }
            }else if(what == 3) {
                if(minBloodTemp != -1 && maxBloodTemp != -1) {
                    if(minBloodTemp < minBlood || maxBloodTemp > maxBlood)  {
                        healthDatas.get(1).setRightTop(minBloodTemp == -1 ? "--" : minBloodTemp + "");
                        healthDatas.get(1).setRightButtom(maxBloodTemp == -1 ? "--" : maxBloodTemp + "");
                        healthDatas.get(1).setDate(date);
                    }
                }
                minBloodTemp = -1;
                maxBloodTemp = -1;
                homePagerAdapter.notifyDataSetChanged();
            }
        }
    };

    private void clearUi() {
        int stepGoal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
        stepArcView.setCurrentCount(stepGoal, 0);
        int[] datas = new int[24];
        cylinderView.setDatas(datas);
        distanceTv.setText("--");
        heartRateTv.setText("--");
        calorieTv.setText("--");

        //清除除心率，血压，血氧之外其它的数据，后面再动态添加
        if (healthDatas != null && healthDatas.size() > 3) {
            for (int i = 3; i < healthDatas.size(); i++) {
                healthDatas.remove(i);
            }
        }
        healthDatas.get(0).setRightTop("-- ");
        healthDatas.get(0).setDate(date);

        healthDatas.get(1).setRightTop("--");
        healthDatas.get(1).setRightButtom("--");
        healthDatas.get(1).setDate(date);

        //设置心氧数据
        healthDatas.get(2).setRightTop("--");
        healthDatas.get(2).setDate(date);

        homePagerAdapter.notifyDataSetChanged();
    }

    private void loadDatas() {
        clearUi();//清除UI
        stepInfos = null;
        heartRateList = null;
        latelyHeartRateModel = null;
        bloodPressureList = null;
        latelyBloodPressure = null;
        sleepModel = null;
        count = 0;
        todayStep = 0;
        todayCalories = 0;
        todayDistance = 0;
        minRateSP = -1;
        maxRateSP = -1;
        minBlood = -1;
        maxBlood = -1;
        minDisPlayRate = -1;
        maxDisplayRate = -1;
        minBloodTemp = -1;
        maxBloodTemp = -1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isNow(d)) {
            hardSdk.setHardSdkCallback(this); //加入回调
            new Thread() {
                @Override
                public void run() {
                    StepInfos stepInfosHistory = sqlHelper.getOneDateStep(MyApplication.account, date);

                    StepInfo stepInfoHardSDK = null;
                    if (MyApplication.isDevConnected) {
                        stepInfoHardSDK = hardSdk.queryOneDayStepInfo(date);


                        sleepModel = hardSdk.querySleepInfo(date, date);
                    }

                    //DataHelper.saveDeviceData()
                    String key = "stepinfos";
                    StepInfos stepInfosSP = DataHelper.getDeviceData(mContext, key);

                    if (stepInfosHistory != null || stepInfoHardSDK != null || stepInfosSP != null) {
                        stepInfos = new StepInfos();
                        int stepGoal = ((UserBean) DataHelper.getDeviceData(mContext, "UserBean")).getDailyGoals();
                        stepInfos.setStepGoal(stepGoal);
                        if (stepInfosHistory != null && stepInfosHistory.getStepOneHourInfo() != null) {
                            stepInfos.setStepOneHourInfo(stepInfosHistory.getStepOneHourInfo());
                        }
                        stepInfos.setDates(date);

                        int step = 0;
                        int calories = 0;
                        float distance = 0f;

                        int stepHistory = 0;
                        int caloriesHistory = 0;
                        float distanceHistory = 0;
                        if (stepInfosHistory != null) {
                            stepHistory = stepInfosHistory.getStep();
                            caloriesHistory = stepInfosHistory.getCalories();
                            distanceHistory = stepInfosHistory.getDistance();
                        }
                        int stepHardSDK = 0;
                        int caloriesHardSDK = 0;
                        float distanceHardSDK = 0;
                        if (stepInfoHardSDK != null) {
                            stepHardSDK = stepInfoHardSDK.getStep();
                            caloriesHardSDK = stepInfoHardSDK.getCalories();
                            distanceHardSDK = stepInfoHardSDK.getDistance();
                        }
                        int stepSP = 0;
                        int caloriesSP = 0;
                        float distanceSP = 0;
                        if (stepInfosSP != null) {
                            stepSP = stepInfosSP.getStep();
                            caloriesSP = stepInfosSP.getCalories();
                            distanceSP = stepInfosSP.getDistance();
                        }

                        step = stepHistory + stepHardSDK + stepSP;
                        calories = caloriesHistory + caloriesHardSDK + caloriesSP;
                        distance = distanceHistory + distanceHardSDK + distanceSP;
                        stepInfos.setStep(step);
                        stepInfos.setCalories(calories);
                        stepInfos.setDistance(distance);
                        stepInfos.setAccount(MyApplication.account);
                    }

                    List heartRateListHistory = sqlHelper.getOneDayHeartRateInfo(MyApplication.account, date);

                    if (heartRateListHistory != null && heartRateListHistory.size() > 0) {
                        todayHeartRateModel = (HeartRateModel) heartRateListHistory.get(heartRateListHistory.size() - 1);
                    }

                    List heartRateListHardSDK = null;
                    if (MyApplication.isDevConnected) {
                        heartRateListHardSDK = hardSdk.queryRateOneDayDetailInfo(date);
                    }
                    if (heartRateListHardSDK != null && heartRateListHardSDK.size() > 0) {
                        if (todayHeartRateModel == null) {
                            todayHeartRateModel = (HeartRateModel) heartRateListHardSDK.get(0);
                        }
                        HeartRateModel heartRateModelTemp = null;
                        for (int i = 0; i < heartRateListHardSDK.size(); i++) {
                            heartRateModelTemp = (HeartRateModel) heartRateListHardSDK.get(i);
                            if (compare_date(heartRateModelTemp.getTestMomentTime(), todayHeartRateModel.getTestMomentTime()) > 0) {
                                todayHeartRateModel = heartRateModelTemp;
                            }
                        }
                    }

                    RateSP rateSPTemp = DataHelper.getDeviceData(mContext, "rateSP");
                    if (rateSPTemp != null && todayHeartRateModel == null) {
                        String rateString = rateSPTemp.getRate();
                        if (rateString != null && rateString.length() > 0) {
                            String[] rates = rateString.split(",");
                            if (rates != null && rates.length > 0) {
                                todayHeartRateModel = new HeartRateModel();
                                todayHeartRateModel.setCurrentRate(Integer.parseInt(rates[rates.length - 1]));
                                todayHeartRateModel.setTestMomentTime(rateSPTemp.getDate());
                                todayHeartRateModel.setAccount(MyApplication.account);
                            }
                        }
                    } else if (rateSPTemp != null && todayHeartRateModel != null) {
                        if (compare_date(rateSPTemp.getDate(), todayHeartRateModel.getTestMomentTime()) > 0) {
                            String rateString = rateSPTemp.getRate();
                            if (rateString != null && rateString.length() > 0) {
                                String[] rates = rateString.split(",");
                                if (rates != null && rates.length > 0) {
                                    todayHeartRateModel.setCurrentRate(Integer.parseInt(rates[rates.length - 1]));
                                }
                            }
                        }
                    }

                    if (rateSPTemp != null) {
                        String rateString = rateSPTemp.getRate();
                        if (rateString != null && rateString.length() > 0) {
                            String[] rates = rateString.split(",");
                            if (rates != null && rates.length > 0) {
                                minRateSP = maxRateSP = Integer.parseInt(rates[0]);
                                for (int i = 0; i < rates.length; i++) {
                                    if (Integer.parseInt(rates[i]) > maxRateSP)   // 判断最大值
                                        maxRateSP = Integer.parseInt(rates[i]);
                                    if (Integer.parseInt(rates[i]) < minRateSP)   // 判断最小值
                                        minRateSP = Integer.parseInt(rates[i]);
                                }
                            }
                        }
                    }

                    bloodPressureList = sqlHelper.getOneDayBloodPressureInfo(MyApplication.account, date);
                    BloodSP bloodSP = DataHelper.getDeviceData(mContext, "bloodSP");

                    if (bloodPressureList != null && bloodPressureList.size() > 0) {
                        if (bloodSP != null) {
                            if (compare_date(((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getTestMomentTime(), bloodSP.getDate()) > 0) {
                                minBlood = ((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getSystolicPressure();
                                maxBlood = ((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getDiastolicPressure();
                            } else {
                                String diastolicStr = bloodSP.getDiastolicStr();
                                String systolicStr = bloodSP.getSystolicStr();
                                if (diastolicStr != null && diastolicStr.length() > 0) {
                                    String[] diastolics = diastolicStr.split(",");
                                    if (diastolics != null && diastolics.length > 0) {
                                        minBlood = Integer.parseInt(diastolics[diastolics.length - 1]);
                                    }
                                }
                                if (systolicStr != null && systolicStr.length() > 0) {
                                    String[] systolics = systolicStr.split(",");
                                    if (systolics != null && systolics.length > 0) {
                                        maxBlood = Integer.parseInt(systolics[systolics.length - 1]);
                                    }
                                }
                            }
                        } else {
                            minBlood = ((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getSystolicPressure();
                            maxBlood = ((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getDiastolicPressure();
                        }
                    } else {
                        if (bloodSP != null) {
                            if (compare_date(((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getTestMomentTime(), bloodSP.getDate()) > 0) {
                                minBlood = ((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getSystolicPressure();
                                maxBlood = ((BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1)).getDiastolicPressure();
                            } else {
                                String diastolicStr = bloodSP.getDiastolicStr();
                                String systolicStr = bloodSP.getSystolicStr();
                                if (diastolicStr != null && diastolicStr.length() > 0) {
                                    String[] diastolics = diastolicStr.split(",");
                                    if (diastolics != null && diastolics.length > 0) {
                                        minBlood = Integer.parseInt(diastolics[diastolics.length - 1]);
                                    }
                                }
                                if (systolicStr != null && systolicStr.length() > 0) {
                                    String[] systolics = systolicStr.split(",");
                                    if (systolics != null && systolics.length > 0) {
                                        maxBlood = Integer.parseInt(systolics[systolics.length - 1]);
                                    }
                                }
                            }
                        }
                    }

                    mHandler.sendEmptyMessage(0);
                }
            }.start();
        } else {
            hardSdk.removeHardSdkCallback(this); //移除回调
            new Thread() {
                @Override
                public void run() {
                    stepInfos = sqlHelper.getOneDateStep(MyApplication.account, date);

                    heartRateList = sqlHelper.getOneDayHeartRateInfo(MyApplication.account, date);
                    if (heartRateList != null && heartRateList.size() > 0) {
                        todayHeartRateModel = (HeartRateModel) heartRateList.get(heartRateList.size() - 1);
                    }

                    bloodPressureList = sqlHelper.getOneDayBloodPressureInfo(MyApplication.account, date);
                    if(bloodPressureList != null && bloodPressureList.size() > 0) {
                        maxBlood = ((BloodPressure)bloodPressureList.get(bloodPressureList.size() - 1)).getDiastolicPressure();
                        minBlood = ((BloodPressure)bloodPressureList.get(bloodPressureList.size() - 1)).getSystolicPressure();
                    }

                    sleepModel = sqlHelper.getOneDaySleepListTime(MyApplication.account, date);

                    mHandler.sendEmptyMessage(0);
                }
            }.start();
        }
    }

    private void removeSpData() {
        DataHelper.removeSF(mContext, "stepinfos");
    }

    @Override
    public void onCallbackResult(int flag, boolean state, Object obj) {

    }

    @Override
    public void onStepChanged(int step, float distance, int calories, boolean finish_status) {
        todayStep += step;
        todayDistance += distance;
        todayCalories += calories;

        String key = "stepinfos";
        StepInfos sfs = null;
        sfs = DataHelper.getDeviceData(mContext, key);
        if (sfs == null || (sfs != null && !sfs.getDates().equals(date))) {
            sfs = new StepInfos();
            sfs.setStep(step);
            sfs.setDistance(distance);
            sfs.setCalories(calories);
            sfs.setDates(date);
        } else {
            int stepTemp = sfs.getStep();
            float distanceTemp = sfs.getDistance();
            int caloriesTemp = sfs.getCalories();

            sfs.setStep(stepTemp + step);
            sfs.setDistance(distanceTemp + distance);
            sfs.setCalories(caloriesTemp + calories);
        }

        DataHelper.saveDeviceData(mContext, key, sfs);
        mHandler.sendEmptyMessage(1);
        count++;
    }

    @Override
    public void onSleepChanged(int lightTime, int deepTime, int sleepAllTime, int[] sleepStatusArray, int[] timePointArray, int[] duraionTimeArray) {

    }

    @Override
    public void onHeartRateChanged(int rate, int status) {
        RateSP rateSp = DataHelper.getDeviceData(mContext, "rateSP");
        if (rateSp != null) {
            String rateStr = rateSp.getRate();
            int count = rateSp.getCount();

            rateSp.setRate(rateStr + "," + rate);
            rateSp.setCount(count + 1);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间
            //保存最后一个的时间，即最新的一次返回数据的时间
            rateSp.setDate(date);
        } else {
            rateSp = new RateSP();
            rateSp.setRate(String.valueOf(rate));
            rateSp.setCount(1);
        }
        DataHelper.saveDeviceData(mContext, "rateSP", rateSp);

        mHandler.sendEmptyMessage(2);
    }

    @Override
    public void bloodPressureChange(int hightPressure, int lowPressure, int status) {
        BloodSP bloodSP = DataHelper.getDeviceData(mContext, "bloodSP");
        if (bloodSP != null) {
            String diastolicStr = bloodSP.getDiastolicStr();
            String systolicStr = bloodSP.getSystolicStr();

            bloodSP.setDiastolicStr(diastolicStr + "," + hightPressure);
            bloodSP.setSystolicStr(systolicStr + "," + lowPressure);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间
            //保存最后一个的时间，即最新的一次返回数据的时间
            bloodSP.setDate(date);
        } else {
            bloodSP = new BloodSP();
            bloodSP.setDiastolicStr(hightPressure + "");
            bloodSP.setSystolicStr(lowPressure + "");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间
            //保存最后一个的时间，即最新的一次返回数据的时间
            bloodSP.setDate(date);
        }

        DataHelper.saveDeviceData(mContext, "bloodSP", bloodSP);
        minBloodTemp = lowPressure;
        maxBloodTemp = hightPressure;
        mHandler.sendEmptyMessage(3);
    }

    public void update(String date) {
        this.date = date;

        LogUtils.debugInfo("TAG", "update date = " + date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟

        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LogUtils.debugInfo("TAG", "update d = " + d);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        //今天之前的数据
        if (isNow(d)) {
            currentDateTv.setText("今天");
        } else if (isLast(d)) {
            currentDateTv.setText("昨天");
        } else {
            currentDateTv.setText(date);
        }

        if (mainFragment.isArcOrLinder()) {
            cylinderView.setVisibility(View.GONE);
            stepArcView.setVisibility(View.VISIBLE);
        } else {
            stepArcView.setVisibility(View.GONE);
            cylinderView.setVisibility(View.VISIBLE);
        }

        //更新左上角当前的日
        String tempDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        dayTv.setText(tempDate);

        loadDatas();
    }

    public View getView() {
        return this.view;
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distance 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    private String getDistanceDate(int distance) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distance);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    public int compare_date(String DATE1, String DATE2) {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    class MyListViewAdapter extends BaseAdapter {
        private List<HealthPageData> healthDatas;

        public MyListViewAdapter(List<HealthPageData> healthDatas) {
            this.healthDatas = healthDatas;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return healthDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        boolean isAllItemEnable = true;

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(
                        mContext).inflate(R.layout.home_page_recyclerview_item, viewGroup,
                        false);

                holder.timeIconTv = (TextView) convertView.findViewById(R.id.tv_time_icon);
//                holder.leftTopTv = (TextView) convertView.findViewById(R.id.tv_left_top);
//                holder.leftButtomTv = (TextView) convertView.findViewById(R.id.tv_left_buttom);
//                holder.rightTopTv = (TextView) convertView.findViewById(R.id.tv_right_top);
//                holder.rightButtomTv = (TextView) convertView.findViewById(R.id.tv_right_buttom);
//                holder.rightIconTv = (TextView) convertView.findViewById(R.id.tv_right_icon_text);
//
//                holder.parent = (RelativeLayout) convertView.findViewById(R.id.rl_parent);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            HealthPageData data = healthDatas.get(position);

       /* holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onImteClick(data.getType());
                }
            }
        });*/
//            if(position==0||position==1){
//                holder.parent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, RateDetailActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                });
//            }
//
//            holder.timeIconTv.setText(data.getTime());
//            if (data.getType() == 1) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.xinlv);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//
//                holder.rightTopTv.setTextColor(mContext.getResources().getColor(R.color.red_FF6466));
//                holder.rightIconTv.setTextColor(mContext.getResources().getColor(R.color.red_FF6466));
//            } else if (data.getType() == 2) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.xueya);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            } else if (data.getType() == 3) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.xueyang);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            } else if (data.getType() == 4) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.jiuzuo);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            } else if (data.getType() == 5) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.walk);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            } else if (data.getType() == 6) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.run);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            } else if (data.getType() == 7) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.sleep);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            } else if (data.getType() == 8) {
//                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.zhaixia);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
//            }
//
//
//            holder.leftTopTv.setText(data.getLeftTopName());
//
//            holder.leftButtomTv.setText(data.getLeftButtom());
//
//            if (data.getType() == 1) {
//                String str = data.getRightTop() + "-" + data.getRightButtom() + "BPM";
//                holder.rightTopTv.setText(str);
//                holder.rightButtomTv.setText("");
//                holder.rightIconTv.setText("查看");
//                holder.rightIconTv.setTextSize(px2dip(mContext, 8));
//            } else if (data.getType() == 2) {
//                holder.rightTopTv.setText("高压  " + data.getRightTop() + " mmhg");
//                holder.rightButtomTv.setText("低压  " + data.getRightButtom() + " mmhg");
//                holder.rightIconTv.setText(data.getRightText());
//                holder.rightIconTv.setTextSize(px2dip(mContext, 16));
//            } else if (data.getType() == 4 || data.getType() == 5 || data.getType() == 6) {
//                holder.rightTopTv.setText(data.getRightTop() + " min");
//                holder.rightButtomTv.setText(data.getRightTop() + "大卡");
//
//                holder.rightIconTv.setText(data.getRightText() + "公里");
//                holder.rightIconTv.setTextSize(px2dip(mContext, 16));
//            } else {
//                holder.rightTopTv.setText(data.getRightTop());
//                holder.rightButtomTv.setText(data.getRightButtom());
//                holder.rightIconTv.setText(data.getRightText());
//                holder.rightIconTv.setTextSize(px2dip(mContext, 16));
//            }
//
//            if (data.isRightIcon()) {
//                Drawable drawable = null;
//                if (data.getType() == 1) {
//                    drawable = mContext.getResources().getDrawable(R.mipmap.fanhuijianhs);
//                } else {
//                    drawable = mContext.getResources().getDrawable(R.mipmap.fanhuijian_right);
//                }
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.rightIconTv.setCompoundDrawables(null, null, drawable, null);
//            } else {
//                holder.rightIconTv.setCompoundDrawables(null, null, null, null);
//            }

            return convertView;
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        public int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        private class ViewHolder {
            TextView timeIconTv;

//            TextView leftTopTv;
//
//            TextView leftButtomTv;
//
//            TextView rightTopTv;
//
//            TextView rightButtomTv;
//
//            TextView rightIconTv;
//
//            RelativeLayout parent;
        }
    }

}
