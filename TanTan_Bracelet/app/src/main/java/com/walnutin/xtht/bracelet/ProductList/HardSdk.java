package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.walnutin.xtht.bracelet.ProductList.Jinterface.ICommonSDKIntf;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IDataCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHardSdkCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IRealDataSubject;
import com.walnutin.xtht.bracelet.ProductList.entity.HeartRateModel;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.ProductList.entity.StepInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenliu
 */

public class HardSdk implements ICommonSDKIntf, IDataCallback, IRealDataSubject {

    private final static String TAG = HardSdk.class.getSimpleName();
    private static HardSdk mHardSdk;
    private ICommonSDKIntf mICommonImpl;
    private List<IHardSdkCallback> mIHardSdkCallbackList = new ArrayList<>();
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GlobalValue.DISCONNECT_MSG || msg.what == GlobalValue.CONNECTED_MSG) {
                Log.d(TAG, "handleMessage:  removeCallbacks(timeoutTask);  1 ");
                mHandler.removeCallbacks(timeoutTask);
            }
            for (IHardSdkCallback iHardSdkCallbackImpl : mIHardSdkCallbackList) {
                if (iHardSdkCallbackImpl != null) {
                    iHardSdkCallbackImpl.onCallbackResult(msg.what, true, msg.obj);
                }
            }
        }
    };

    private HardSdk() {

    }

    public static HardSdk getInstance() {
        if (mHardSdk == null) {
            mHardSdk = new HardSdk();
        }
        return mHardSdk;
    }


    @Override
    public boolean initialize(Context context) {
        return mICommonImpl.initialize(context);
    }

    @Override
    public void initService() {
        mICommonImpl.initService();
    }

    /**
     * 更新要调用的sdk对象
     *
     * @param factoryName
     * @param deviceName
     * @param deviceAddr
     * @param applicationContext
     * @return
     */
    public boolean refreshBleServiceUUID(String factoryName, String deviceName, String deviceAddr, Context applicationContext) {
        ICommonSDKIntf commonSDKIntf = ProductFactory.getInstance().creatSDKImplByUUID(factoryName, applicationContext);
        setICommonImpl(commonSDKIntf);
        if (!initialize(applicationContext)) {
            return false;
        }
        setIDataCallBack(this);
        setRealDataSubject(this);
        refreshBleServiceUUID();
        return true;
    }


    @Override
    public void refreshBleServiceUUID() {
        mICommonImpl.refreshBleServiceUUID();
    }

    @Override
    public void connect(String addr) {
        mHandler.postDelayed(timeoutTask, GlobalValue.BLE_CONNECT_TIME_OUT_MILLISECOND);
        mICommonImpl.connect(addr);

    }

    @Override
    public void disconnect() {
//        mHandler.removeCallbacks(timeoutTask);
        mICommonImpl.disconnect();
    }

    @Override
    public void readRssi() {
        mICommonImpl.readRssi();
    }

    @Override
    public void findBand(int num) {
        mICommonImpl.findBand(num);
    }

    @Override
    public void stopVibration() {
        mICommonImpl.stopVibration();
    }

    @Override
    public void resetBracelet() {
        mICommonImpl.resetBracelet();
    }

    @Override
    public void sendCallOrSmsInToBLE(String number, int smsType, String contact, String content) {
        mICommonImpl.sendCallOrSmsInToBLE(number, smsType, contact, content);
    }

    @Override
    public void sendQQWeChatTypeCommand(int type, String body) {
        mICommonImpl.sendQQWeChatTypeCommand(type, body);
    }

    @Override
    public void setUnLostRemind(boolean isOpen) {
        mICommonImpl.setUnLostRemind(isOpen);
    }

    @Override
    public void setAlarmClcok(int flag, byte weekPeroid, int hour, int minitue, boolean isOpen, String type, String tip) {
        mICommonImpl.setAlarmClcok(flag, weekPeroid, hour, minitue, isOpen, type, tip);
    }

    @Override
    public void sendSedentaryRemindCommand(int isOpen, int time, String startTime, String endTime) {
        mICommonImpl.sendSedentaryRemindCommand(isOpen, time, startTime, endTime);
    }

    @Override
    public void setHeightAndWeight(int height, int weight, int screenOnTime) {
        mICommonImpl.setHeightAndWeight(height, weight, screenOnTime);
    }

    @Override
    public void sendOffHookCommand() {
        mICommonImpl.sendOffHookCommand();
    }

    @Override
    public void openTakePhotoFunc(boolean isOpen) {
        mICommonImpl.openTakePhotoFunc(isOpen);
    }

    @Override
    public void startRateTest() {
        mICommonImpl.startRateTest();
    }

    @Override
    public void stopRateTest() {
        mICommonImpl.stopRateTest();
    }

    @Override
    public void syncAllStepData() {
        mICommonImpl.syncAllStepData();
    }

    @Override
    public void syncAllHeartRateData() {
        mICommonImpl.syncAllHeartRateData();
    }

    @Override
    public void syncAllSleepData() {
        mICommonImpl.syncAllSleepData();
    }

    @Override
    public void syncAllBloodData() {
        mICommonImpl.syncAllBloodData();
    }


    @Override
    public StepInfo queryOneDayStepInfo(String date) {
        return mICommonImpl.queryOneDayStepInfo(date);
    }

    @Override
    public Map<Integer, Integer> queryOneHourStep(String date) {
        return mICommonImpl.queryOneHourStep(date);
    }

    @Override
    public SleepModel querySleepInfo(String startDate, String endDate) {
        return mICommonImpl.querySleepInfo(startDate, endDate);
    }

    @Override
    public List<HeartRateModel> queryRateOneDayDetailInfo(String date) {
        return mICommonImpl.queryRateOneDayDetailInfo(date);
    }

    @Override
    public void syncBraceletDataToDb() {
        mICommonImpl.syncBraceletDataToDb();
    }

    @Override
    public void setRealDataSubject(IRealDataSubject iDataSubject) {
        mICommonImpl.setRealDataSubject(iDataSubject);
    }

    @Override
    public void openFuncRemind(int type, boolean isOpen) {
        mICommonImpl.openFuncRemind(type, isOpen);
    }

    @Override
    public boolean isSupportHeartRate(String deviceName) {
        return mICommonImpl.isSupportHeartRate(deviceName);
    }

    @Override
    public boolean isSupportBloodPressure(String deviceName) {
        return mICommonImpl.isSupportBloodPressure(deviceName);
    }

    @Override
    public boolean isSupportUnLostRemind(String deviceName) {
        return mICommonImpl.isSupportUnLostRemind(deviceName);
    }

    @Override
    public int getAlarmNum() {
        return mICommonImpl.getAlarmNum();
    }

    @Override
    public void noticeRealTimeData() {
        mICommonImpl.noticeRealTimeData();
    }

    @Override
    public void queryDeviceVesion() {
        mICommonImpl.queryDeviceVesion();
    }

    @Override
    public boolean isVersionAvailable(String version) {
        return mICommonImpl.isVersionAvailable(version);
    }

    @Override
    public void startUpdateBLE() {
        mICommonImpl.startUpdateBLE();
    }

    @Override
    public void cancelUpdateBle() {
        mICommonImpl.cancelUpdateBle();
    }

    @Override
    public void readBraceletConfig() {
        mICommonImpl.readBraceletConfig();
    }

    @Override
    public void setIDataCallBack(IDataCallback iDataCallBack) {
        mICommonImpl.setIDataCallBack(iDataCallBack);
    }

    @Override
    public void setDeviceSwitch(String type, boolean isOpen) {

    }

    public void findBattery() {
        mICommonImpl.findBattery();
    }


    @Override
    public void writeCommand(String hexValue) {
        mICommonImpl.writeCommand(hexValue);
    }

    public void setICommonImpl(ICommonSDKIntf mICommonImpl) {
        this.mICommonImpl = mICommonImpl;
    }

    @Override
    public void onResult(Object data, boolean state, int flag) {
        Message message = mHandler.obtainMessage();
        message.what = flag;
        message.obj = data;
        mHandler.sendMessage(message);
    }

    @Override
    public void onSynchronizingResult(String data, boolean state, int status) {

    }

    public void setHardSdkCallback(IHardSdkCallback mHardSdkCallbackImpl) {
        if (mIHardSdkCallbackList != null && !mIHardSdkCallbackList.contains(mHardSdkCallbackImpl)) {
            mIHardSdkCallbackList.add(mHardSdkCallbackImpl);
        }
    }

    public void removeHardSdkCallback(IHardSdkCallback mHardSdkCallbackImpl) {
        if (mHardSdkCallbackImpl != null && mIHardSdkCallbackList != null && mIHardSdkCallbackList.contains(mHardSdkCallbackImpl)) {
            mIHardSdkCallbackList.remove(mHardSdkCallbackImpl);
        }
    }

    private Runnable timeoutTask = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: timeoutTask");
            mHandler.sendEmptyMessage(GlobalValue.CONNECT_TIME_OUT_MSG);
        }
    };

    @Override
    public void stepChanged(int step, float distance, int calories, boolean finish_status) {
        for (IHardSdkCallback hardSdkCallbackImpl : mIHardSdkCallbackList) {
            if (hardSdkCallbackImpl != null) {
                hardSdkCallbackImpl.onStepChanged(step, distance, calories, finish_status);
            }
        }
    }

    @Override
    public void sleepChanged(int lightTime, int deepTime, int sleepAllTime, int[] sleepStatusArray, int[] timePointArray, int[] duraionTimeArray) {
        for (IHardSdkCallback hardSdkCallbackImpl : mIHardSdkCallbackList) {
            if (hardSdkCallbackImpl != null) {
                hardSdkCallbackImpl.onSleepChanged(lightTime, deepTime, sleepAllTime, sleepStatusArray, timePointArray, duraionTimeArray);
            }
        }
    }

    @Override
    public void heartRateChanged(int rate, int status) {
        for (IHardSdkCallback hardSdkCallbackImpl : mIHardSdkCallbackList) {
            if (hardSdkCallbackImpl != null) {
                hardSdkCallbackImpl.onHeartRateChanged(rate, status);
            }
        }
    }

    @Override
    public void bloodPressureChange(int hightPressure, int lowPressure, int status) {
        for (IHardSdkCallback hardSdkCallbackImpl : mIHardSdkCallbackList) {
            if (hardSdkCallbackImpl != null) {
                hardSdkCallbackImpl.bloodPressureChange(hightPressure, lowPressure, status);
            }
        }
    }
}
