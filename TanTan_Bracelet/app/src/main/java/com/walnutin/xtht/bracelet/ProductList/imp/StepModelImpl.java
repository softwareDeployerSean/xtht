package com.walnutin.xtht.bracelet.ProductList.imp;

import android.content.Context;


import com.walnutin.xtht.bracelet.ProductList.Conversion;
import com.walnutin.xtht.bracelet.ProductList.DeviceHomeDataSp;
import com.walnutin.xtht.bracelet.ProductList.DeviceSharedPf;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfos;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：MrJiang
 */
public class StepModelImpl {
    public StepInfos stepModel;
    Context context;
    private DeviceHomeDataSp mPedometerSettings;
    SqlHelper sqlHelper;


    public StepModelImpl(Context context) {
        this.context = context;
        sqlHelper = SqlHelper.instance();
        mPedometerSettings = DeviceHomeDataSp.getInstance(context);
    }

    public StepInfos loadTodayStepModel() {
        long laststampTime = mPedometerSettings.getLast_Seen();
        String lastDate = TimeUtil.timeStamp2YMDDate(laststampTime);
        boolean today = lastDate.equals(TimeUtil.nowDate());
        //     stepModel.stepGoal = mPedometerSettings.getInt("goals", 10000);
        if (today || laststampTime == 0) {
         //   stepModel = (StepInfos) Conversion.stringToObject(mPedometerSettings.getString(MyApplication.account + "_devStep", null));
            if (stepModel == null) {
                stepModel = SqlHelper.instance().getOneDateStep(MyApplication.account, TimeUtil.getCurrentDate());  // 得到今天的数据
            }
            if (stepModel == null) {
                stepModel = new StepInfos();
            }
        } else {
            stepModel = new StepInfos();

        }
        stepModel.stepGoal = DeviceSharedPf.getInstance(context).getInt("target", 10000);
        //   stepModel.setDates(TimeUtil.getCurrentDate()); //设置今天的数据
        return stepModel;
    }

    public void saveTodayStepModel() {
        mPedometerSettings.setSteps(stepModel.step);
        mPedometerSettings.setDistance(stepModel.distance);
        mPedometerSettings.setCalories(stepModel.calories);
        mPedometerSettings.setStepGoal(stepModel.stepGoal);
        mPedometerSettings.setString(MyApplication.account + "_devStep", Conversion.objectToString(stepModel));
    }

    public void setStepGoal(int stepGoal) {
        stepModel.stepGoal = stepGoal;
        mPedometerSettings.setStepGoal(stepGoal);
    }

    public int getStepGoal() {
        return stepModel.stepGoal;
    }

    public void setCalories(int calories) {
        stepModel.setCalories(calories);
    }

    public void setDistance(float distance) {
        stepModel.setDistance(distance);
    }

    public int getStep() {
        return stepModel.getStep();
    }

    public int getCalories() {
        return stepModel.getCalories();
    }

    public float getDistance() {
        return stepModel.getDistance();
    }

    public void setStep(int step) {
        stepModel.setStep(step);
    }

    public StepInfos getStepModel() {
        return stepModel;
    }

    public void setIsUpLoad(int isUpLoad) {
        stepModel.setUpLoad(isUpLoad);
    }

    public int getIsUpLoad() {
        return stepModel.getIsUpLoad();
    }

    public Map<Integer, Integer> getStepOneHourInfo() {
        return stepModel.stepOneHourInfo;
    }

    public void setStepOneHourInfo(Map<Integer, Integer> stepOneHourInfo) {
        this.stepModel.stepOneHourInfo = stepOneHourInfo;
    }

    public void upLoadTodayData() {

        stepModel.setDates(TimeUtil.getCurrentDate()); // 设置今天的日期

        stepModel.setAccount(MyApplication.account);

        sqlHelper.insertOrUpdateTodayStep(stepModel);  // 添加到数据库

        // HttpImpl.getInstance().upLoadStep(stepModel); // 上传数据

    }


    public int getStepScore() {
        int score = stepModel.step / 100;
        score = score > 100 ? 100 : score;
        return score;
    }

    /*
    *
    * 2016 11.17  增加一天24小时详情步数
    *
    * */

    List<Integer> dayHourKey;
    List<Integer> dayHourValue;

    public void resolveStepInfo() {  //计算指定日期的步数与时间关系 横纵坐标系
        if (stepModel != null) {
            StepInfos stepInfos = SqlHelper.instance().getOneDateStep(MyApplication.account, TimeUtil.getCurrentDate());
            stepModel.setStepOneHourInfo(stepInfos.getStepOneHourInfo());
            if (stepModel.getStepOneHourInfo() == null) {
                return;
            }
        } else {
            return;
        }
        dayHourKey = new ArrayList<>();
        dayHourValue = new ArrayList<>();
        Set<Map.Entry<Integer, Integer>> set = stepModel.getStepOneHourInfo().entrySet();
        Iterator<Map.Entry<Integer, Integer>> itor = set.iterator();

        while (itor.hasNext()) {
            Map.Entry<Integer, Integer> entry = itor.next();
            //System.out.println(entry.getKey() + " : " + entry.getValue());
            dayHourKey.add(entry.getKey() / 60);      //key 是保存的分钟数，  得到小时数
            dayHourValue.add(entry.getValue());
        }
    }


    public List<Integer> getOneDayStepKeyDetails() {
        return dayHourKey;
    }

    public List<Integer> getOneDayStepHourValueDetails() {
        return dayHourValue;

    }


}
