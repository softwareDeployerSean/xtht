package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.os.Message;
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

import com.jess.arms.utils.LogUtils;
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
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.DataShowActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RateDetailActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.HomePageAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomLinearLayoutManager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CylinderView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RecycleViewDivider;
import com.walnutin.xtht.bracelet.mvp.ui.widget.StepArcView;

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

public class HomePageItem {

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
            int stepGoal = stepInfos.getStepGoal();
            int actualStep = stepInfos.getStep();
            LogUtils.debugInfo("TAG", "stepGoal=" + stepGoal + ", actualStep=" + actualStep);
            if (stepGoal == 0) {
                stepGoal = 7000;
                actualStep = new Random().nextInt(6000);
            }
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
                    int r = new Random().nextInt(100);
                    stepDatas[i] = r;
                }
            }
            cylinderView.setDatas(stepDatas);

            int calories = stepInfos.getCalories();
            calorieTv.setText(calories == 0 ? "--" : String.valueOf(calories));

            float distance = stepInfos.getDistance();
            distanceTv.setText(distance == 0 ? "--" : String.valueOf(distance));

            int rate = 0;
            if (heartRateList != null && heartRateList.size() > 0) {
                int totalRate = 0;
                HeartRateModel heartRateModel = null;
                for (int i = 0; i < heartRateList.size(); i++) {
                    heartRateModel = (HeartRateModel) heartRateList.get(i);
                    totalRate += heartRateModel.getCurrentRate();
                }
                rate = totalRate / heartRateList.size();

                latelyHeartRateModel = (HeartRateModel) heartRateList.get(heartRateList.size() - 1);
            } else {
                rate = 0;
            }
            heartRateTv.setText(rate == 0 ? "--" : String.valueOf(rate));

            if (bloodPressureList != null && bloodPressureList.size() > 0) {
                latelyBloodPressure = (BloodPressure) bloodPressureList.get(bloodPressureList.size() - 1);
            }

            //清除除心率，血压，血氧之外其它的数据，后面再动态添加
            if(healthDatas != null && healthDatas.size() > 3) {
                for (int i = 3; i < healthDatas.size(); i++) {
                    healthDatas.remove(i);
                }
            }

            if (sleepModel != null) {
                HealthPageData sleepPageData = new HealthPageData(7, "14:25", "睡眠", "", "5h 42min", "良好", "", false, 7, date);
                String topText = "";
                int allDurationTime = sleepModel.getAllDurationTime();
                if(allDurationTime > 1 * 60 * 60) {
                    topText = (allDurationTime/1 * 60 * 60) + "h " +  ((allDurationTime % (1 * 60 * 60)) / 60) + "min";
                }else {
                    topText = (allDurationTime / 60) + "min";
                }
                sleepPageData.setRightTop(topText);
                String sleepStatusText = "";
                if(allDurationTime >= 7 * 60 * 60 && allDurationTime <= 10 * 60 * 60) {
                    sleepStatusText = "优秀";
                }else if(allDurationTime >=5 * 60 * 60 && allDurationTime < 7 * 60 * 60) {
                    sleepStatusText = "良好";
                }else {
                    sleepStatusText = "一般";
                }

                sleepPageData.setRightButtom(sleepStatusText);
                healthDatas.add(sleepPageData);
            }

            //设置心率数据
            if (latelyHeartRateModel != null) {
                healthDatas.get(0).setRightTop(latelyHeartRateModel.getLowRate() + "-" + latelyHeartRateModel.getHighRate());
                healthDatas.get(0).setDate(date);
            } else {
                healthDatas.get(0).setRightTop("-- ");
                healthDatas.get(0).setDate(date);
            }

            //设置血压数据
            if (latelyBloodPressure != null) {
                healthDatas.get(1).setRightTop(String.valueOf(latelyBloodPressure.getSystolicPressure()));
                healthDatas.get(1).setRightButtom(String.valueOf(latelyBloodPressure.getDiastolicPressure()));
                healthDatas.get(1).setDate(date);
            } else {
                healthDatas.get(1).setRightTop("--");
                healthDatas.get(1).setRightButtom("--");
                healthDatas.get(1).setDate(date);
            }

            //设置心氧数据
            healthDatas.get(2).setRightTop("--");
            healthDatas.get(2).setDate(date);

            homePagerAdapter.notifyDataSetChanged();

        }
    };

    private void loadDatas() {
        stepInfos = null;
        heartRateList = null;
        latelyHeartRateModel = null;
        bloodPressureList = null;
        latelyBloodPressure = null;
        sleepModel = null;

        new Thread() {
            @Override
            public void run() {
                stepInfos = sqlHelper.getOneDateStep(MyApplication.account, TimeUtil.getCurrentDate());

                heartRateList = sqlHelper.getOneDayHeartRateInfo(MyApplication.account, TimeUtil.getCurrentDate());

                bloodPressureList = sqlHelper.getOneDayBloodPressureInfo(MyApplication.account, TimeUtil.getCurrentDate());

                sleepModel = sqlHelper.getOneDaySleepListTime(MyApplication.account, TimeUtil.getCurrentDate());

                mHandler.sendEmptyMessage(0);
            }
        }.start();
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
