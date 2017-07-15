package com.walnutin.xtht.bracelet.ProductList.imp;

import android.content.Context;

import com.google.gson.Gson;
import com.walnutin.xtht.bracelet.ProductList.Conversion;
import com.walnutin.xtht.bracelet.ProductList.DeviceHomeDataSp;
import com.walnutin.xtht.bracelet.ProductList.SleepSharedPf;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * 作者：MrJiang
 */
public class SleepModelImpl {
    public SleepModel sleepModel;
    private DeviceHomeDataSp mPedometerSettings;
    Context context;
    int sleepClockDuration;

    public SleepModelImpl(Context context) {
        this.context = context;
        mPedometerSettings = DeviceHomeDataSp.getInstance(context);
    }


    public SleepModel loadTodaySleepModel() {
        long laststampTime = mPedometerSettings.getLast_Seen();
        String lastDate = TimeUtil.timeStamp2YMDDate(laststampTime);
        boolean today = lastDate.equals(TimeUtil.nowDate());
        if (today || laststampTime == 0) {
            sleepModel = (SleepModel) Conversion.stringToObject(mPedometerSettings.getString(MyApplication.account + "_devSleep", null));
            if (sleepModel == null) {
                sleepModel = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, TimeUtil.getCurrentDate());
            }
            if (sleepModel == null) {
                sleepModel = new SleepModel();

            }
        } else {
            sleepModel = new SleepModel();
        }

        return sleepModel;
    }

    void initTestData() {
        //   String jsonS = "{\"account\":\"15323716780\",\"timePointArray\":[1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388],\"duraionTimeArray\":[30,15,45,30,30,30,45,30,30,15,15,15,45,15,15],\"sleepStatusArray\":[1,0,1,0,1,0,1,0,1,0,1,0,1,1,2],\"lightTime\":255,\"deepTime\":135,\"totalTime\":390}";
        sleepModel.account = "15323716780";

    }

    public void setSleepModel(SleepModel sleepModel) {
        this.sleepModel = sleepModel;
    }

    public void saveTodaySleepModel() {
        mPedometerSettings.setString(MyApplication.account + "_devSleep", Conversion.objectToString(sleepModel));
    }

    public void setDuraionTimeArray(int[] duraionTimeArray) {
        sleepModel.duraionTimeArray = duraionTimeArray;
    }

    public void setSleepStatusArray(int[] sleepStatusArray) {
        sleepModel.sleepStatusArray = sleepStatusArray;
    }

    public void setTimePointArrays(int[] timePointArray) {
        if (timePointArray == null) {
            return;
        }
        sleepModel.timePointArray = timePointArray;
        List<Integer> durationStartPos = new ArrayList<Integer>();
        int initValue = timePointArray[0] - sleepModel.duraionTimeArray[0];
        initValue = initValue < 0 ? initValue + 1440 : initValue;
        durationStartPos.add(initValue);
        for (int i = 1; i < timePointArray.length; i++) {
            initValue += sleepModel.duraionTimeArray[i - 1];
            durationStartPos.add(initValue % 1440);
        }
        sleepModel.setDurationStartPos(durationStartPos);
    }

    public void setTimePointArray(int[] timePointArray) {  // 一定要设置
        sleepModel.timePointArray = timePointArray;
        List<Integer> durationStartPos = new ArrayList<Integer>();
        int initValue = timePointArray[0] - sleepModel.duraionTimeArray[0]; // 开始时刻
        initValue = initValue < 0 ? initValue + 1440 : initValue;
        durationStartPos.add(initValue);

        for (int i = 1; i < timePointArray.length; i++) {
            int gapValue = timePointArray[i] - timePointArray[i - 1];
            gapValue = gapValue > 0 ? gapValue : (gapValue + 1440) % 1440;
            //     initValue += gapValue;
            sleepModel.duraionTimeArray[i] = gapValue;  //重置 状态数组

            durationStartPos.add(timePointArray[i - 1] % 1440);   // 每一状态的开始时刻
        }
        sleepModel.setDurationStartPos(durationStartPos);
    }


    private int getDurationLen() {      //得到持续时长
        int len = 0;
//        for (int i = 0; i < sleepModel.duraionTimeArray.length; i++) {
//            len += sleepModel.duraionTimeArray[i];
//        }
        if (sleepModel.getTimePointArray() == null) {
            return 0;
        }
        len = sleepModel.timePointArray[getTimePointArray().length - 1] - (sleepModel.timePointArray[0] - sleepModel.duraionTimeArray[0]);
        len = len > 0 ? len : len + 1440;     //睡眠总时长
        sleepModel.setAllDurationTime(len);
        return len;
    }

    public int getAllDurationTime() {
        return getDurationLen();
    }

    public int getTotalTime() {
        return sleepModel.totalTime;
    }

    public int getSleepScore() {     // 产生一个分数
        int sleepScore = sleepModel.getTotalTime();
        int score = 0;
        if (sleepScore >= 6 * 60 && sleepScore < 11 * 60) {
            score = 100;
        } else if ((sleepScore >= 5 * 60 && sleepScore < 6 * 60) || (sleepScore >= 11 * 60 && sleepScore < 12 * 60)) {
            score = 80;
        } else if ((sleepScore >= 4 * 60 && sleepScore < 5 * 60) || (sleepScore >= 12 * 60 && sleepScore < 13 * 60)) {
            score = 60;
        } else if ((sleepScore >= 3 * 60 && sleepScore < 4 * 60) || (sleepScore >= 13 * 60 && sleepScore < 14 * 60)) {
            score = 40;
        } else if ((sleepScore >= 2 * 60 && sleepScore < 3 * 60) || (sleepScore >= 14 * 60)) {
            score = 20;
        } else {
            score = 0;
        }

        return score;
    }

    public void setTotalTime(int totalTime) {
        sleepModel.totalTime = totalTime;
    }

    public void setTotalTime(int[] duraionTimeArray) {

        if (duraionTimeArray == null) {
            return;
        }
        int sum = 0;
        for (int i = 0; i < duraionTimeArray.length; i++) {
            sum += duraionTimeArray[i];
        }
        sleepModel.totalTime = sum;

    }

    public int getLightTime() {
        return sleepModel.lightTime;
    }

    public void setLightTime(int lightTime) {
        sleepModel.lightTime = lightTime;
    }

    public int getDeepTime() {
        return sleepModel.deepTime;
    }

    public void setDeepTime(int deepTime) {
        sleepModel.deepTime = deepTime;
    }

    public String getStartSleep() {
        return sleepModel.startSleep;
    }

    public void reFormatSleepDurtion(){

    }

    public void setStartSleep() {
        sleepModel.startSleep = TimeUtil.MinutiToTime(sleepModel.durationStartPos.get(0)); //开始时间

    }

    public String getEndSleep() {
        return sleepModel.endSleep;
    }

    public void setEndSleep() {
        sleepModel.endSleep = TimeUtil.MinutiToTime(sleepModel.timePointArray[sleepModel.timePointArray.length - 1]);

    }

    public int[] getSleepStatusArray() {
        return sleepModel.getSleepStatusArray();
    }

    public List<Integer> getDurationStartPos() {
        return sleepModel.getDurationStartPos();
    }

    public int[] getTimePointArray() {
        return sleepModel.getTimePointArray();
    }

    public int[] getDuraionTimeArray() {
        return sleepModel.getDuraionTimeArray();
    }


    public void testInsertSleepData() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            calendar.add(Calendar.DATE, 1);
            if (random.nextInt(10) % 2 == 0) {
                SleepModel sleepModel = new SleepModel();
                sleepModel.account = "15323716780";
                sleepModel.date = TimeUtil.getformatData(calendar.getTime());
                SqlHelper.instance().insertOrUpdateSleepData(MyApplication.account, sleepModel);
            }
        }
    }

    /*
    * 得到清醒次数
    * */

    public int getSoberNum() {
        int time = 0;
        if (sleepModel.getSleepStatusArray() != null) {
            int[] status = sleepModel.getSleepStatusArray();
            for (int i = 0; i < status.length; i++) {
                if (status[i] == 2) {
                    time++;
                }
            }
        }
        return time;
    }

    public int getSoberTime() {
        return sleepModel.getSoberTime();
    }

    public void upLoadSleepServer() {
        sleepModel.account = MyApplication.account;
        sleepModel.date = TimeUtil.getCurrentDate();
        sleepClockDuration = SleepSharedPf.getInstance(context).getSleepClockDuration(480); //预计睡眠时长
        sleepModel.sleepStatus = (int) (sleepModel.totalTime * 100) / sleepClockDuration;
        sleepModel.soberTime = sleepModel.totalTime - (sleepModel.deepTime + sleepModel.lightTime);
        sleepModel.soberNum = getSoberNum();
        Gson gson = new Gson();
        //    String jsonSleep = gson.toJson(sleepModel);
        //      System.out.println("sleep: json: " + jsonSleep);
        SqlHelper.instance().insertOrUpdateSleepData(MyApplication.account, sleepModel);
//        List<SleepModel> sleepModelList = SqlHelper.instance().getUnUpLoadSleepDataToServer(sleepModel.account);
//        if (sleepModelList.size() > 0) {
//            String json = gson.toJson(sleepModelList);
//            HttpImpl.getInstance().upLoadSleepData(json);
//        }
    }

}
