package com.walnutin.xtht.bracelet.ProductList.ycy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.walnutin.xtht.bracelet.ProductList.DeviceOtherInfoManager;
import com.walnutin.xtht.bracelet.ProductList.DeviceSharedPf;
import com.walnutin.xtht.bracelet.ProductList.GlobalValue;
import com.walnutin.xtht.bracelet.ProductList.HardSdk;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHardSdkCallback;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.Utils;
import com.walnutin.xtht.bracelet.ProductList.entity.StepChangeNotify;
import com.walnutin.xtht.bracelet.ProductList.eventbus.SyncStatus;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

/**
 * 作者：MrJiang on 2017/4/10
 */
public class LinkService extends Service implements IHardSdkCallback {

    final String TAG = LinkService.class.getSimpleName();

    DeviceSharedPf mySharedPf;
    private Context mContext;
    boolean isAttempLinking = false;  // 尝试连接
    LinkTime linkTime;
    boolean stepSyncStarted = false;
    String lastSyncTime = "";
    private String toastString;
    int rssi_value;
    int lostIndex = 0;
    DeviceOtherInfoManager deviceOtherInfoManager;
    private int FINISH_TIME = 1000 * 60 * 10;  //倒计时 十分钟
    private int ONE_TRY = 1000 * 15;         // 每5秒钟尝试一次
    private boolean isStartingLinking;


    @Override
    public void onCreate() {
        super.onCreate();

        mySharedPf = DeviceSharedPf.getInstance(getApplicationContext());
        MyApplication.tmpDeviceName = mySharedPf.getString("device_name");
        MyApplication.tmpDeviceAddr = mySharedPf.getString("device_address");
        MyApplication.tmpFactoryName = mySharedPf.getString("device_factory");
        linkTime = new LinkTime(FINISH_TIME, ONE_TRY);
        checkSyncStatus = new SycnStatusListener(1000 * 30, 1000);
        deviceOtherInfoManager =DeviceOtherInfoManager.getInstance(getApplicationContext());
        EventBus.getDefault().register(this);
        HardSdk.getInstance().setHardSdkCallback(this);
        if (MyApplication.tmpFactoryName != null && !MyApplication.tmpFactoryName.equals("null")) {
            refreshSdkByUUID(MyApplication.tmpFactoryName, MyApplication.tmpDeviceName, MyApplication.tmpDeviceAddr);
        }
        registerReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (MyApplication.tmpDeviceAddr != null && MyApplication.isDevConnected == false) {
            isStartingLinking = true;
            isAttempLinking = false;
            linkTime.cancel();
            System.out.println("onStartCommand:"+MyApplication.tmpDeviceAddr);
            HardSdk.getInstance().connect(MyApplication.tmpDeviceAddr);
        }
        if (mySharedPf.getBoolean("isFirstRunApp", true) && MyApplication.isDevConnected == true) {
            connectOper();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void refreshSdkByUUID(String factoryName, String deviceName, String deviceAddr) {
        Log.i(TAG, "refreshSdkByUUID: deviceName" + deviceName);
        Log.i(TAG, "refreshSdkByUUID: deviceAddr" + deviceAddr);
        Log.d(TAG, "refreshSdkByUUID: factoryName" + factoryName);
        //   Log.d("myresult", "refreshSdkByUUID: HardSdk.getInstance()" + HardSdk.getInstance());

        if (!HardSdk.getInstance().refreshBleServiceUUID(factoryName, deviceName, deviceAddr, getApplicationContext())) {
//            stopSelf();
            Toast.makeText(mContext, "初始化失败", Toast.LENGTH_LONG).show();
        }


    }

    private void registerReceiver() {


        IntentFilter mFilter2 = new IntentFilter();
        mFilter2.addAction(Config.NOTICE_ACTION);
        mFilter2.addAction(Config.NOTICE_MSG_ACTION);
        mFilter2.addAction(Config.NOTICE_PHONE_ACTION);
        registerReceiver(mDeviceNoticeReceiver, mFilter2);


    }


    String contact;
    boolean isPhone = false;
    boolean isOther = false; // 暂代指红包模式
    private BroadcastReceiver mDeviceNoticeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //     System.out.println("");
            if (HardSdk.getInstance() == null) {
                return;
            }
            String action = intent.getAction();
            if (action.equals(Config.NOTICE_ACTION)) {
                String type = intent.getStringExtra("type");
                Log.d(TAG, "onReceive: type:" + type);
                if (type.equals("msg")) {
                    String content = intent.getStringExtra("content");
                    if (content == null) {
                        return;
                    }
                    HardSdk.getInstance().sendQQWeChatTypeCommand(GlobalValue.TYPE_MESSAGE_SMS, content);
                } else if (type.equals("WeChat")) {
                    String content = intent.getStringExtra("content");
                    if (content == null) {
                        return;
                    }
                    System.out.println("content:" + content);
                    HardSdk.getInstance().sendQQWeChatTypeCommand(GlobalValue.TYPE_MESSAGE_WECHAT, content);
                } else if (type.equals("qq")) {
                    String content = intent.getStringExtra("content");
                    if (content == null) {
                        return;
                    }
                    HardSdk.getInstance().sendQQWeChatTypeCommand(GlobalValue.TYPE_MESSAGE_QQ, content);
                } else if (type.equals("facebook")) {
                    String content = intent.getStringExtra("content");
                    if (content == null) {
                        return;
                    }
                    HardSdk.getInstance().sendQQWeChatTypeCommand(GlobalValue.TYPE_MESSAGE_FACEBOOK, content);
                } else if (type.equals("whatapp")) {
                    String content = intent.getStringExtra("content");
                    if (content == null) {
                        return;
                    }
                    HardSdk.getInstance().sendQQWeChatTypeCommand(GlobalValue.TYPE_MESSAGE_WHATSAPP, content);
                } else if (type.equals("redpacket")) {
                    if (intent.getStringExtra("content") != null) {
                        contact = intent.getStringExtra("content");  //
                        isOther = true;
                        isPhone = false;
                        HardSdk.getInstance().sendCallOrSmsInToBLE("18888888888", GlobalValue.TYPE_MESSAGE_SMS, "", contact);
                        mHandler.removeMessages(GlobalValue.RED_PACTAGE);
                        Message msg = new Message();
                        msg.what = GlobalValue.RED_PACTAGE;
                        msg.obj = intent.getStringExtra("contact");
                        mHandler.sendMessageDelayed(msg, 2000);
                    }

                }
            } else if (action.equals(Config.NOTICE_MSG_ACTION)) {
                String contactName = intent.getStringExtra("contacts");
                //     System.out.println("contacts: " + contact);
                isOther = false;
                if (contactName == null) {
                    return;
                }
                contact = Utils.getContactNameFromPhoneNum(getApplicationContext(), contactName);
                isPhone = false;
                String content = intent.getStringExtra("content");
                if (content == null) {
                    return;
                }
                HardSdk.getInstance().sendCallOrSmsInToBLE(contactName, GlobalValue.TYPE_MESSAGE_SMS, contact, content);
            } else if (action.equals(Config.NOTICE_PHONE_ACTION)) {
                String state = intent.getStringExtra("state");
                if (state.equals("CALL_STATE_RINGING")) {
                    String contactName = intent.getStringExtra("contacts");
                    isOther = false;
                    if (contactName == null) {
                        return;
                    }
                    contact = Utils.getContactNameFromPhoneNum(getApplicationContext(), contactName);
                    System.out.println("contact: " + contact);
                    isPhone = true;
                    HardSdk.getInstance().sendCallOrSmsInToBLE(contactName, GlobalValue.TYPE_MESSAGE_PHONE, contact, null);
                } else if (state.equals("CALL_STATE_OFFHOOK")) {
                    HardSdk.getInstance().sendOffHookCommand();
                }
            }
        }
    };


    @Subscriber
    public void syncBraceletDev(StepChangeNotify.SyncData sync) {
        //   Utils.showToast(getApplicationContext(), getString(R.string.startSync));
        EventBus.getDefault().post(new SyncStatus(true));  // 同步开始
        MyApplication.isSyncing = true;
        stepSyncStarted = false;
        checkSyncStatus.cancel();
        checkSyncStatus.start();
        DeviceSharedPf.getInstance(getApplicationContext()).setString("lastsyncSleepTime" + MyApplication.deviceAddr,
                "2010"); // 设置同步日期
        findHandler.removeCallbacks(null);
        lastSyncTime = "2010"; // 设置同步日期
        HardSdk.getInstance().syncAllStepData();

    }


    Runnable findBattery = new Runnable() {
        @Override
        public void run() {
            if (MyApplication.isSyncing == false && MyApplication.isDevConnected == true) {
                if (Utils.isBackground(getApplicationContext())) {
                    HardSdk.getInstance().findBattery();
                }
            }
            findHandler.postDelayed(this, 30000);
        }
    };


    Handler findHandler = new Handler();



    @Override
    public void onCallbackResult(int flag, boolean state, Object data) {
        switch (flag) {
            case GlobalValue.STEP_SYNC_START:
                stepSyncStarted = true;
                break;
            case GlobalValue.BLOOD_FINISH:
                HardSdk.getInstance().syncAllHeartRateData();
                break;
            case GlobalValue.HEART_FINISH:
                if (lastSyncTime.equals(TimeUtil.getCurrentDate())) { //是同一天
                    onCallbackResult(GlobalValue.SYNC_FINISH, true, null);
                    return;
                }
                HardSdk.getInstance().syncAllSleepData(); //开始同步睡眠
                break;
            case GlobalValue.STEP_FINISH:
                if (MyApplication.isSyncing == false) {  // 同步完成，规避重复完成
                    return;
                }
                stepSyncStarted = true;
                HardSdk.getInstance().syncAllBloodData();
                break;
            case GlobalValue.SYNC_FINISH:

                if (MyApplication.isSyncing == false) {  // 同步完成，规避重复完成
                    return;
                }
                syncFinishedOper();
                break;
            case GlobalValue.READ_RSSI_VALUE:    //得到线损值
                if (data == null) {
                    return;
                }
                rssi_value = (int) data;
                System.out.println("rssi_value:" + rssi_value);
                if (deviceOtherInfoManager.isUnLost() && rssi_value <= -87) { // 智能防丢
                    lostIndex++;

                } else {
                    lostIndex = 0;
                }
                break;
            //连接超时：
            case GlobalValue.CONNECT_TIME_OUT_MSG:
                Log.d(TAG, "handleMessage: CONNECT_TIME_OUT_MSG");
                toastString = getString(R.string.braceletTimeOut);
            case GlobalValue.DISCONNECT_MSG:
                Log.d(TAG, "handleMessage: DISCONNECT_MSG isAttempLinking" + isAttempLinking);
                isStartingLinking = false;
                if (flag == GlobalValue.DISCONNECT_MSG) {
                    toastString = getString(R.string.braceletbreak);
                }
                if (isAttempLinking == false) {
                    Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
                }
                if (lostIndex >= 3) {
                    if (lostIndex >= 3) {
                        Intent intent = new Intent("lost_alarm");
                      //  MyApplication.getContext().sendBroadcast(intent);
                    }
                }
                lostIndex = 0;
                disconnectOper();
                break;
            case GlobalValue.CONNECTED_MSG:
                //    System.out.println(" 同步 已连接： " + DeviceLinkService.this);
                //      System.out.println("同步已连接：lastSyncTime:" + lastSyncTime);
                Log.d(TAG, "handleMessage: CONNECTED_MSG");

                connectOper();
                //   mHandler.removeCallbacksAndMessages(null);

                break;
        }
    }


    private void connectOper() {
        MyApplication.isDevConnected = true;
        mySharedPf.setString("device_name", MyApplication.tmpDeviceName);
        mySharedPf.setString("device_address", MyApplication.tmpDeviceAddr);
        mySharedPf.setString("device_factory", MyApplication.tmpFactoryName);
        MyApplication.deviceAddr = MyApplication.tmpDeviceAddr;
        MyApplication.deviceName = MyApplication.tmpDeviceName;
        lastSyncTime = DeviceSharedPf.getInstance(getApplicationContext()).getString("lastsyncSleepTime" + MyApplication.deviceAddr, "2010");

        MyApplication.isSyncing = true;  // 开始同步
        linkTime.cancel();
        isAttempLinking = false;
        stepSyncStarted = false;
        HardSdk.getInstance().syncAllStepData();
        checkSyncStatus.cancel();
        checkSyncStatus.start();  // 检测同步状态
        EventBus.getDefault().post(new SyncStatus(true));  // 同步开始

        if (deviceOtherInfoManager.isUnLost()) {
            if (HardSdk.getInstance() != null) {
                mHandler.removeCallbacks(lostRunnable);
                mHandler.postDelayed(lostRunnable, 20000);
            }
        }
    }

    private void disconnectOper() {
        EventBus.getDefault().post(new SyncStatus(false));  // 同步结束
        if (MyApplication.isManualOff == true) {
            MyApplication.isManualOff = false;
        } else {
            if (isAttempLinking == false) { // 是不是在尝试连接
                linkTime.cancel();
                linkTime.start();
            }
        }
        MyApplication.deviceName = "";
        MyApplication.deviceAddr = "";
        configHandler.removeCallbacks(null);
        checkSyncStatus.cancel();
        MyApplication.isDevConnected = false;
        mHandler.removeCallbacksAndMessages(null); // 取消Handle所有消息
        findHandler.removeCallbacksAndMessages(null); // 取消Handle所有消息

    }

    private void syncFinishedOper() {
        mySharedPf.setBoolean("isFirstRunApp", false);

        if (isSyncTimeOut == true) {
            Utils.showToast(getApplicationContext(), getString(R.string.syncTimeOut));
        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.syncFinish));
        }
        checkSyncStatus.cancel();
        DeviceSharedPf.getInstance(getApplicationContext()).setString("lastsyncSleepTime" + MyApplication.deviceAddr, TimeUtil.getCurrentDate()); // 设置同步日期
        MyApplication.isSyncing = false;


        if (0 == DeviceSharedPf.getInstance(getApplicationContext()).getInt("isWHSynced", 0)) // 身高体重是否同步完
        {
            setHeightAndWeight();
        }
//        mHandler.removeMessages(GlobalValue.SYNC_FINISH);   //?

        //     HardSdk.getInstance().readBraceletConfig();

        //syncConfig(); //同步手机配置到手环


        HardSdk.getInstance().syncBraceletDataToDb();
        EventBus.getDefault().post(new SyncStatus(false));  // 同步结束

        findHandler.postDelayed(findBattery, 30000);
    }

    void setHeightAndWeight() {
        // int time = intent.getIntExtra("screentime", 5); //默认5s
        //    int height = Integer.valueOf(mySharedPf.getString("height", "170"));
        //     int weight = Integer.valueOf(mySharedPf.getString("weight", "60").split("\\.")[0]);
        //    HardSdk.getInstance().setHeightAndWeight(height, weight, deviceOtherInfoManager.getLightScreenTime());
        //     DeviceSharedPf.getInstance(getApplicationContext()).setInt("isWHSynced", 1);
    }


    Runnable lostRunnable = new Runnable() {
        @Override
        public void run() {
            HardSdk.getInstance().readRssi();
            mHandler.postDelayed(this, 1500);
        }
    };


    private Handler configHandler = new Handler();

//    List<Clock> clockList;
//    int clockSize = 0;
//
//    private void syncConfig() {
//        clockList = clockManager.getLocalAlarmInfo(mySharedPf.getString("device_factory", GlobalValue.FACTORY_YCY));
//        clockSize = clockList.size();
//        configHandler.removeCallbacks(null);
//        configFlag = 0;
//        configHandler.post(syncConfigRunable);
//    }
//
//    int configFlag = 0;
//    AlarmManager alarmManager;
//    ClockManager clockManager;
//    NoticeInfo noticeInfo;
//
//
//    Runnable syncConfigRunable = new Runnable() {
//        @Override
//        public void run() {
//            switch (configFlag) {
//                case 0:
//                    HardSdk.getInstance().openFuncRemind(GlobalValue.TYPE_MESSAGE_PHONE, noticeInfo.isEnablePhone);
//                    break;
//                case 1:
//                    HardSdk.getInstance().openFuncRemind(GlobalValue.TYPE_MESSAGE_SMS, noticeInfo.isEnableMsg);
//                    break;
//                case 2:
//                    HardSdk.getInstance().openFuncRemind(GlobalValue.TYPE_MESSAGE_QQ, noticeInfo.isEnableQQ);
//                    break;
//                case 3:
//                    HardSdk.getInstance().openFuncRemind(GlobalValue.TYPE_MESSAGE_WECHAT, noticeInfo.isEnableWeChat);
//                    break;
//                case 4:
//                    HardSdk.getInstance().openFuncRemind(GlobalValue.TYPE_MESSAGE_FACEBOOK, noticeInfo.isEnableFaceBook); // facebook通知
//                    break;
//
//                case 5:
//                    HardSdk.getInstance().sendSedentaryRemindCommand(deviceOtherInfoManager.isLongSitRemind() == true ? 1 : 0,
//                            deviceOtherInfoManager.getLongSitTime(), deviceOtherInfoManager.getLongSitStartTime(), deviceOtherInfoManager.getLongSitEndTime());
//                    break;
//
//                case 6:
//                    int heightType = MySharedPf.getInstance(getApplicationContext()).getInt("heightType", 0);
//                    String height = MySharedPf.getInstance(getApplicationContext()).getString("height");
//                    String weight = MySharedPf.getInstance(getApplicationContext()).getString("weight");
//                    int cmValue = 172;
//                    int kgValue = 60;
//                    if (heightType == 0) {
//                        int inch = Integer.valueOf(height.split("'")[0]);
//                        String footTmp = height.split("'")[1]; // 10";
//                        footTmp = footTmp.substring(0, footTmp.indexOf("\""));
//                        int foot = Integer.valueOf(footTmp);
//                        cmValue = (int) Math.round(inch * 30.48 + foot * 2.54);
//
//                    } else {
//                        cmValue = Integer.parseInt(height);
//                    }
//                    int weightType = MySharedPf.getInstance(getApplicationContext()).getInt("weightType", 0);
//
//                    if (weightType == 0) {
//                        int pound = Integer.valueOf(weight);
//                        kgValue = (int) Math.round(0.4536 * pound);
//                    } else {
//                        kgValue = Integer.parseInt(weight);
//                    }
//                    HardSdk.getInstance().setHeightAndWeight(cmValue, kgValue, deviceOtherInfoManager.getLightScreenTime());
//                    //    configHandler.removeCallbacks(null);
//                    break;
//
//            }
//
//            try {
//
//                if (clockSize > 0 && configFlag > 6) {
//                    System.out.println("clock Size: " + clockSize);
//                    Clock clock = clockList.get(clockSize - 1);
//                    packageSendInfo(clock.getTime(), "haha", clock.serial, clock.repeat, clock.isEnable, clock.type, clock.tip);
//                    clockSize--;
//                } else if (clockSize == 0 && configFlag > 6) {
//                    configHandler.removeCallbacks(null);
//                    return;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            configFlag++;
//            configHandler.postDelayed(this, 99);
//        }
//    };
//
//
//    void packageSendInfo(String time, String repeatValue, int flag, int cycle, boolean isOpen, String type, String tip) {
//        if (time != null && time.length() > 0 && repeatValue != null && repeatValue.length() >= 2) {
//            String[] times = time.split(":");
//            int hour = Integer.parseInt(times[0]);
//            int minitue = Integer.parseInt(times[1]);
//            byte weekPeroid = 0;
//            if (cycle == 0) {  // 每天的闹钟
//                weekPeroid = Config.EVERYDAY;
//            } else {
//                weekPeroid = WeekUtils.getWeekByteByReapeat(cycle); // 得到星期的信息
//            }
//
//
//            HardSdk.getInstance().setAlarmClcok(flag, weekPeroid, hour, minitue, isOpen, type, tip);
//        }
//    }

    @Override
    public void onStepChanged(int step, float distance, int calories, boolean finish_status) {

        Log.i(TAG, step + "" + " distance: " + distance);
    }

    @Override
    public void onSleepChanged(int lightTime, int deepTime, int sleepAllTime, int[] sleepStatusArray, int[] timePointArray, int[] duraionTimeArray) {

    }

    @Override
    public void onHeartRateChanged(int rate, int status) {

    }

    @Override
    public void bloodPressureChange(int hightPressure, int lowPressure, int status) {

    }


    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    //下面3个计时器
    public class LinkTime extends CountDownTimer {

        public LinkTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //   MyApplication.ins;    // 尝试连接
            isAttempLinking = true;
            if (MyApplication.tmpDeviceAddr != null && HardSdk.getInstance() != null) {
                Log.d(TAG, "onTick: 尝试重连接");
                HardSdk.getInstance().connect(MyApplication.tmpDeviceAddr);
            }
        }

        @Override
        public void onFinish() {
            isAttempLinking = false;
            this.cancel();
            stopSelf();
        }
    }

    boolean isSyncTimeOut = false;
    SycnStatusListener checkSyncStatus;

    public class SycnStatusListener extends CountDownTimer {

        public SycnStatusListener(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //    System.out.println("同步 记步状态开始同步" + millisUntilFinished);

            if (stepSyncStarted == true) {
                //   this.onFinish();
                //   this.cancel();
            } else {
                HardSdk.getInstance().syncAllStepData();
            }
            isSyncTimeOut = false;
        }

        @Override
        public void onFinish() {
            this.cancel();
            if (MyApplication.isDevConnected == true) {
                if (!GlobalValue.FACTORY_YCY.equals(MyApplication.globalFactoryName)) {
                    isSyncTimeOut = true;
                    onCallbackResult(GlobalValue.SYNC_FINISH, true, null);
                } else {
//                    mHandler.sendEmptyMessageDelayed(GlobalValue.SYNC_FINISH, 120 * 1000);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCallbackResult(GlobalValue.SYNC_FINISH, true, null);
                        }
                    }, 50 * 1000);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.isManualOff = true;
        HardSdk.getInstance().disconnect();// unBindService
        linkTime.cancel();
        isAttempLinking = false;
        findHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(mDeviceNoticeReceiver);
        EventBus.getDefault().unregister(this);
    }
}
