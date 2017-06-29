package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IABleConnectStatusListener;
import com.veepoo.protocol.listener.base.IConnectResponse;
import com.veepoo.protocol.listener.base.INotifyResponse;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;

public class EpConnecteService extends Service {

    private static final String TAG = "[TAN][" + EpConnecteService.class.getSimpleName() + "]";

    public static final String EP_CONNECTED_STATE_ACTION = "com.xtht.epconnect_action";
    public static final String STATA_CHANGE_ACTION = "com.xtht.ep.state_change.action";

    VPOperateManager mVpoperateManager;

    ReConnecteThread connecteThread;

    boolean isConnected = false;

    public EpConnecteService() {
    }

    @Override
    public void onCreate() {
        mVpoperateManager = VPOperateManager.getMangerInstance(MyApplication.getAppContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        connectDevice();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EP_CONNECTED_STATE_ACTION);

        MyApplication.getAppContext().registerReceiver(connectStateBroadcastReceiver, intentFilter);

        return START_STICKY;
    }

    public void registerStateListener(String mac) {
        mVpoperateManager.registerConnectStatusListener(mac, mBleConnectStatusListener);
    }

    private void connectDevice() {

        if (!BluetoothUtils.isBluetoothEnabled()) {
            LogUtils.debugInfo(TAG, "蓝牙连接断开");
            return;
        }

        String mac = DataHelper.getStringSF(this, "mac");
        LogUtils.debugInfo(TAG + ",mac=" + mac);
        if (mac != null && !mac.equals("") && !mac.equals("default")) {

            mVpoperateManager.registerConnectStatusListener(mac, mBleConnectStatusListener);

            mVpoperateManager.connectDevice(mac, new IConnectResponse() {
                @Override
                public void connectState(int code, BleGattProfile profile, boolean isoadModel) {
                    if (code == Code.REQUEST_SUCCESS) {
                        //蓝牙与设备的连接状态
                        LogUtils.debugInfo(TAG + "连接成功");
                        LogUtils.debugInfo(TAG + "是否是固件升级模式=" + isoadModel);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "isoadModel", isoadModel + ""); //连接成功
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "2"); //连接成功
                    } else {
                        LogUtils.debugInfo(TAG + "连接失败");
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "0"); //连接失败
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connected_address", "null");
                    }
                }
            }, new INotifyResponse() {
                @Override
                public void notifyState(int state) {
                    if (state == Code.REQUEST_SUCCESS) {
                        isConnected = true;
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connected_address", mac);
                        LogUtils.debugInfo(TAG + ", 监听成功，可以进行其它操作了");
                        DataHelper.setStringSF(MyApplication.getAppContext(), "mac", mac);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "3"); //连接监听
                        if(connecteThread != null) {
                            connecteThread.interrupt();
                            connecteThread = null;
                        }

                    } else {
                        LogUtils.debugInfo(TAG + "监听失败，重新连接");
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "1"); //连接监听
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connected_address", "null");
                    }

                }
            });
        }else {
            LogUtils.debugInfo(TAG + ",-----------没有存在的mac");
        }
    }

    /**
     * 监听系统蓝牙的打开和关闭的回调状态
     */
    private final IABleConnectStatusListener mBleConnectStatusListener = new IABleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {

            if (status == Constants.STATUS_CONNECTED) {
//                Logger.t(TAG).i("STATUS_CONNECTED");
                LogUtils.debugInfo("----------------------连接成功------------------------------");
                LogUtils.debugInfo(TAG + "STATUS_CONNECTED");
            } else if (status == Constants.STATUS_DISCONNECTED) {
//                Logger.t(TAG).i("STATUS_DISCONNECTED");
                LogUtils.debugInfo(TAG + "STATUS_DISCONNECTED");
                LogUtils.debugInfo("----------------------连接断开------------------------------");
                DataHelper.setStringSF(MyApplication.getAppContext(), "connected_address", "null");
                DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "0"); //连接失败
                isConnected = false;
                if(connecteThread == null) {
                    connecteThread = new ReConnecteThread();
                    connecteThread.start();
                }

            }
            Intent intent = new Intent(STATA_CHANGE_ACTION);
            MyApplication.getAppContext().sendBroadcast(intent);
        }
    };

    private int retryTime = 0;

    class ReConnecteThread extends Thread {

        @Override
        public void run() {
            LogUtils.debugInfo(TAG + "----------threadID=" + Thread.currentThread().getId());
            while (!isConnected) {
                LogUtils.debugInfo("11111111111111111111111111111111");
                if(retryTime > 40) {
                    break;
                }
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isConnected) {
                    LogUtils.debugInfo("2222222222222222222222222222222222222222222");
                    retryTime++;
                    connectDevice();
                }
            }

            retryTime = 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver connectStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mac = intent.getStringExtra("mac");

            LogUtils.debugInfo(TAG + ",--------------connectStateBroadcastReceiver mac = " + mac);

            if(mac != null && !mac.equals("")) {
                mVpoperateManager.registerConnectStatusListener(mac, mBleConnectStatusListener);
            }
        }
    };

}
