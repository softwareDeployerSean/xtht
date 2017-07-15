package com.walnutin.xtht.bracelet.ProductList.imp;

import android.content.Context;
import android.os.CountDownTimer;


import com.walnutin.xtht.bracelet.ProductList.Conversion;
import com.walnutin.xtht.bracelet.ProductList.DeviceHomeDataSp;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.HeartRateModel;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：MrJiang
 */
public class HeartRateModelImpl {
    List<HeartRateModel> heartRateModelList = new ArrayList<>();
    public HeartRateModel currentHeartRateModel = new HeartRateModel();
    private List<Integer> rightHeartList = new ArrayList<>();
    public int currentHeartIndex = 0;
    Context context;
    private DeviceHomeDataSp mPedometerSettings;
    private boolean testState = false; //测试状态
    CountDownTimer timer;
    //TimeInf timeInf;


    public HeartRateModelImpl(Context context) {
        this.context = context;
        timer = new CountTimes(2 * 60 * 60 * 1000, 1000);

        mPedometerSettings = DeviceHomeDataSp.getInstance(context);
    }

//    public void setOnTimeCountIntf(TimeInf timeCountIntf) {
//        timeInf = timeCountIntf;
//    }

    public Object loadTodayHeartModelList() {
        long laststampTime = mPedometerSettings.getLast_Seen();
        String lastDate = TimeUtil.timeStamp2YMDDate(laststampTime);
        boolean today = lastDate.equals(TimeUtil.nowDate());
        if (today || laststampTime ==0) {
            currentHeartRateModel = (HeartRateModel) Conversion.stringToObject(mPedometerSettings.getString(MyApplication.account+"_devHeartRate", null));
            if (currentHeartRateModel == null) {
                currentHeartRateModel = SqlHelper.instance().getOneDayRecentHeartRateInfo(MyApplication.account,TimeUtil.getCurrentDate());
            }
        }else {
            currentHeartRateModel = new HeartRateModel();
        }
        return currentHeartRateModel;
    }

    public void addOrderHeartMode(HeartRateModel model) { //按从小到大心率排序，插入
        int index = 0;
        for (HeartRateModel heartRateModel : heartRateModelList) {
            if (heartRateModel.currentRate >= model.currentRate) {
                break;
            }
            index++;
        }
        currentHeartIndex = index;
        heartRateModelList.add(index, model);
    }

    public void addHeartModeByTimeDesc(HeartRateModel model) {
        heartRateModelList.add(0, model); // 添加到列表头部
    }

    public void removeHeartMode(int i) {
        heartRateModelList.remove(i);
    }

    public HeartRateModel getHeartRateModelByCurrentRate(int current) {  // 根据心率值得到心率对象
        for (HeartRateModel model : heartRateModelList) {
            if (model.currentRate == current) {
                return model;
            }
        }
        return null;
    }

    class CountTimes extends CountDownTimer {

        public CountTimes(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            currentHeartRateModel.durationTime++;     //
//            if (timeInf != null) {
//             //   timeInf.timeCount(currentHeartRateModel.durationTime);
//            }
        }

        @Override
        public void onFinish() {
            //  System.out.println("finish......");
        //    EventBus.getDefault().post(new StepChangeNotify.HeartMeasure(false));  // 停止计步
            stopTest();
            this.cancel();

        }
    }


    public void stopTest() {

        if (timer != null) {
            timer.cancel();     //取消计时器
        }

        if (testState == false) {
            return;
        }

        currentHeartRateModel.isRunning = false;

        testState = false;

    }

    public void startTest() {
        if (testState == true) {
            return;
        }

        createHeartRateModel(); // 实例化新对象
        startTimer();                     // 启动计时器
        testState = true;

    }

    public boolean isTestState() {
        return testState;
    }

    public void setTestState(boolean testState) {
        this.testState = testState;
    }

    private void startTimer() {
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                currentHeartRateModel.durationTime++;     //
//                if (timeInf != null) {
//                    timeInf.timeCount(currentHeartRateModel.durationTime);
//                }
//                //       EventBus.getDefault().post();
//            }
//        }, 50, 1000);

        if (timer != null) {
            timer.start();
        }
    }

    public int getDurationTime() {
        return currentHeartRateModel.durationTime;
    }


    public HeartRateModel getNextHeartRateModeByCurrentRate() {
        if (currentHeartIndex == heartRateModelList.size() - 1 || heartRateModelList.size() <= 0) {
            return null;
        }
//        int index = 0;
//        for (HeartRateModel model : heartRateModelList) {
//            if (model.currentRate == current) {
//                break;
//            }
//            index++;
//        }
        currentHeartIndex = currentHeartIndex + 1;
        return currentHeartRateModel = heartRateModelList.get(currentHeartIndex);
    }

    public HeartRateModel getBeforHeartRateModeByCurrentRate() {
        if (currentHeartIndex == 0) {
            return null;
        }

        currentHeartIndex = currentHeartIndex - 1;
        return currentHeartRateModel = heartRateModelList.get(currentHeartIndex);
    }

    public void createHeartRateModel() {
        currentHeartRateModel = new HeartRateModel();
    }

    public HeartRateModel getCurrentHeartRateModel() {
        return currentHeartRateModel;
    }

    public void addHeartRateValue(int value) {     //增加心率值
        currentHeartRateModel.heartTrendMap.put(System.currentTimeMillis(), value);
    }

    public void setDurationTime(int time) {
        currentHeartRateModel.durationTime = time;
    }

    public List<HeartRateModel> getHeartRateModelList() {
        return heartRateModelList;
    }

    public void setHeartRateModelList(List<HeartRateModel> heartRateModelList) {
        this.heartRateModelList = heartRateModelList;
    }

    public int getCurrentHeartIndex() {
        return currentHeartIndex;
    }

    public void setCurrentHeartIndex(int currentHeartIndex) {
        this.currentHeartIndex = currentHeartIndex;
    }

    public void saveTodayHeartModel() {

        mPedometerSettings.setString(MyApplication.account+"_devHeartRate", Conversion.objectToString(currentHeartRateModel));

    }

    public void clearCurrentRateList() {
        currentHeartRateModel.heartTrendMap.clear();
    }

    public void mapToList() {
        try {
            Map<Long, Integer> map = currentHeartRateModel.heartTrendMap;
            List<Integer> list = new ArrayList<>(map.values());
            Collections.reverse(list);    //反转排序

            rightHeartList = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //      return list;
    }

    public List<Integer> getCurrentRateList() {
        return rightHeartList;
    }


    public String getTestMoment() {
        Set<Long> timeSet = currentHeartRateModel.heartTrendMap.keySet();
        String temp = "00:00";
        for (Iterator iter = timeSet.iterator(); iter.hasNext(); ) {
            Long key = (Long) iter.next();
            System.out.println("时间戳：" + key + "====" + TimeUtil.timeStamp2_Hour_MinitueDate(key));
        }
        return temp;
    }

    public String getTestMoments() {         //得到测试完成时刻
        if (currentHeartRateModel.testMomentTime != null) {
            return currentHeartRateModel.testMomentTime;
        } else {
            return "1970-00-00 00:00:00";
        }
    }

    public void setTestMoment() {  // 测试完成时的 时刻

        currentHeartRateModel.testMomentTime = TimeUtil.timeStamp2FullDate(System.currentTimeMillis());

    }

    public List<Integer> getLeftBarHeartList() {       // 切换 记录的选择
        List<Integer> list = new ArrayList<>();
        for (HeartRateModel model : heartRateModelList) {
            list.add(model.currentRate);
        }
        return list;
    }


    public void setRecentRateList(List<Integer> recentRateList) {      //
        currentHeartRateModel.setRecentRateList(recentRateList);
    }

    public void setRealRateList(List<Integer> realRateList) {
        currentHeartRateModel.setRealRateList(realRateList);
    }

    public List<Integer> getRecentRateList() {
        return currentHeartRateModel.recentRateList;
    }

    public List<Integer> getRealRateList() {
        return currentHeartRateModel.realRateList;
    }

    public void setHighRate(int highRate) {
        currentHeartRateModel.HighRate = highRate;
    }

    public void setLowRate(int lowRate) {
        currentHeartRateModel.lowRate = lowRate;
    }

    public void setRightLowRate() {

        if (rightHeartList.size() > 0) {
            currentHeartRateModel.lowRate = rightHeartList.get(0);
        }
    }

    public void setRightHighRate() {

        if (rightHeartList.size() > 0) {
            currentHeartRateModel.HighRate = rightHeartList.get(rightHeartList.size() - 1);
        }
    }

    public void setRightCurrentRate() {            //计算平均值
        if (rightHeartList.size() > 0) {
            currentHeartRateModel.currentRate = getHeartCenterValue();
           // currentHeartRateModel.currentRate = rightHeartList.get(0);
        }

    }

    public void setCurrentRate(int currentRate) {
        currentHeartRateModel.currentRate = currentRate;
    }

    public int getLowRate() {
        return currentHeartRateModel.lowRate;
    }

    public int getHighRate() {
        return currentHeartRateModel.HighRate;
    }

    public int getCurrentRate() {
        return currentHeartRateModel.currentRate;
    }


    public int getOnLineCurrentRate() {
        return heartRateModelList.size() > 0 ? (int) heartRateModelList.get(heartRateModelList.size() - 1).currentRate : 0;
    }

    public int getHeartCenterValue() {     // 计算一次测试的平均心率值
        int center = 0;
        int index = 0;
        int sum = 0;
        List<Integer> centerList = getCurrentRateList();
        for (Integer value : centerList) {
            index++;
            sum += value;
        }
        if (index > 0) {
            center = sum / index;
        }
        return center;
    }

    public int getHeartListLowRate() {
        int low = 1000;
        for (HeartRateModel heartRateModel : heartRateModelList) {
            if (heartRateModel.lowRate < low) {
                low = heartRateModel.lowRate;
            }
        }
        return low;
    }

    public int getHeartListHighRate() {
        int high = -1;
        for (HeartRateModel heartRateModel : heartRateModelList) {
            if (heartRateModel.getHighRate() > high) {
                high = heartRateModel.lowRate;
            }
        }
        return high;
    }
//Gson gson = new Gson();

    public int getHeartRateScore() {
        heartRateModelList = SqlHelper.instance().getOneDayHeartRateInfo(MyApplication.account, TimeUtil.getCurrentDate());
        int score = 0;
        int count = 0;
        int todayHeartValueSum = 0;
        for (HeartRateModel heartRateModel : heartRateModelList) {
            if (heartRateModel.isRunning == true) {
                continue;
            }
            if (heartRateModel.currentRate >= 50 && heartRateModel.currentRate < 100) {
                todayHeartValueSum += 100;
            } else if ((heartRateModel.currentRate >= 100 && heartRateModel.currentRate < 120)) {
                todayHeartValueSum += 60;
            } else if ((heartRateModel.currentRate >= 40 && heartRateModel.currentRate < 50) || (heartRateModel.currentRate >= 120 && heartRateModel.currentRate < 140)) {
                todayHeartValueSum += 40;
            } else if (heartRateModel.currentRate >= 140 || heartRateModel.currentRate < 40) {
                todayHeartValueSum += 20;
            }
            count++;
        }
        if (count != 0) {
            score = todayHeartValueSum / count;
        }
        return score;
    }


    public void upLoadHeartServer() {
        currentHeartRateModel.account = MyApplication.account;
        SqlHelper.instance().insertHeartRate(MyApplication.account, currentHeartRateModel);
      //  this.heartRateModelList = SqlHelper.instance().getUnUpLoadToServerHeartRate(MyApplication.account);
     //   Gson gson = new Gson();
      //  if (heartRateModelList.size() > 0) {
      //      String json = gson.toJson(heartRateModelList);
         //   System.out.println("json: " + json);
        //    HttpImpl.getInstance().upLoadHeartRate(json);
     //   }
        //  Gson gson
    }


    /*
    *
    * 2016/11/17
    *
    * */


}
