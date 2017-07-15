package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;


import com.walnutin.xtht.bracelet.ProductList.DensityUtils;
import com.walnutin.xtht.bracelet.ProductList.DeviceListAdapter;
import com.walnutin.xtht.bracelet.ProductList.DeviceSharedPf;
import com.walnutin.xtht.bracelet.ProductList.GlobalValue;
import com.walnutin.xtht.bracelet.ProductList.HardSdk;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.DeviceLinkView;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHardSdkCallback;
import com.walnutin.xtht.bracelet.ProductList.LinkPresenter;
import com.walnutin.xtht.bracelet.ProductList.Utils;
import com.walnutin.xtht.bracelet.ProductList.entity.Device;
import com.walnutin.xtht.bracelet.ProductList.entity.MyBleDevice;
import com.walnutin.xtht.bracelet.ProductList.ycy.LinkService;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：MrJiang on 2017/5/27 16:47
 */
public class SearchActivity extends Activity implements DeviceLinkView, IHardSdkCallback {

    LinkPresenter linkPresenter;
    @BindView(R.id.topTitle)
    TopTitleLableView topTitleLableView;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.uiIndicator)
    UIActivityIndicatorView uiIndicator;
    private DeviceSharedPf mySharedPf;
    List<MyBleDevice> myBleDeviceList;
    DeviceListAdapter deviceListAdapter;
    HardSdk hardSdk;
    String deviceAddr;
    String deviceName;
    String braceletType;
    private final long SCAN_PERIOD = 120 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        hardSdk = HardSdk.getInstance();
        topTitleLableView.setOnBackListener(new TopTitleLableView.OnBackListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        topTitleLableView.getTitleView().setTextColor(Color.WHITE);

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = topTitleLableView.getTitleRl().getLayoutParams();
            layoutParams.height = DensityUtils.dip2px(getApplicationContext(), 72);
            topTitleLableView.setLayoutParams(layoutParams);
            topTitleLableView.getTitleRl().setLayoutParams(layoutParams);
            topTitleLableView.getBackView().setPadding(DensityUtils.dip2px(getApplicationContext(), 14), DensityUtils.dip2px(getApplicationContext(), 22), 0, 0);
            topTitleLableView.getTitleView().setPadding(0, DensityUtils.dip2px(getApplicationContext(), 22), 0, 0);
        }
        uiIndicator.start();

        linkPresenter = new LinkPresenter(this, getApplicationContext());
        linkPresenter.getLocalLinkedDeviceList();
        if (!linkPresenter.isSupportBle4_0()) {
            Utils.showToast(getApplicationContext(), getString(R.string.isBluetoothSupport));
            finish();
            return;
        }
        hardSdk.setHardSdkCallback(this);
        initView();
        mySharedPf = DeviceSharedPf.getInstance(getApplicationContext());
        startScanDev();


    }

    private void startScanDev() {  // 扫描蓝牙
        linkPresenter.clearDeviceList();
        linkPresenter.startScan();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //        mScanning = false;
                linkPresenter.stopScan();
                //        searchFinishStatus();
            }
        }, SCAN_PERIOD);

    }

    private void initView() {
        myBleDeviceList = new ArrayList<>();
        deviceListAdapter = new DeviceListAdapter(getApplicationContext(), myBleDeviceList);
        listview.setAdapter(deviceListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyBleDevice myBleDevice = myBleDeviceList.get(position);
                MyApplication.tmpDeviceName = myBleDevice.getDevice().getName();
                deviceAddr = myBleDevice.getDevice().getAddress();
                deviceName = myBleDevice.getDevice().getName();
                braceletType = myBleDevice.getFactoryName();
                if (!HardSdk.getInstance().refreshBleServiceUUID(braceletType, myBleDevice.getDevice().getName(), deviceAddr, getApplicationContext())) {
                    return;
                }
                HardSdk.getInstance().connect(deviceAddr);
                showDialog();
            }
        });
    }

    LoadDataDialog loadDataDialog;

    void showDialog() {
        if (loadDataDialog == null || !loadDataDialog.isShowing()) {
            loadDataDialog = new LoadDataDialog(this, "link");
            loadDataDialog.setCancelable(false);
            //    loadDataDialog.setOnKeyListener(mCancelListener);
            loadDataDialog.setCanceledOnTouchOutside(false);
            loadDataDialog.show();

        }
    }


    void disMissDialog() {
        if (loadDataDialog != null && loadDataDialog.isShowing()) {
            loadDataDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        linkPresenter.saveLinkedDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linkPresenter.stopScan();
        mHandler.removeCallbacksAndMessages(null);
//        EventBus.getDefault().unregister(this);
        hardSdk.removeHardSdkCallback(this);
        uiIndicator.stop();

    }

    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 10:
                    myBleDeviceList.clear();
                    //  mLeDeviceListAdapter.setLeDeviceList((List) msg.obj);
                    break;
                case 5:

                    deviceListAdapter.addDevice((MyBleDevice) msg.obj);
                    break;
            }
        }
    };

    @Override
    public List<BluetoothDevice> getScanListDevices() {
        return null;
    }

    @Override
    public List<BluetoothDevice> getLinkedListDevices() {
        return null;
    }

    @Override
    public void updateScanListDevices(List list) {

    }

    @Override
    public void updateScanSingleDevice(Object object) {
        MyBleDevice myBle = (MyBleDevice) object;

        Message message = mHandler.obtainMessage(5);
        message.obj = object;
        mHandler.sendMessage(message);
    }

    @Override
    public void updateLinkedListDevices(List list) {
        //mConnectedDv = list;

    }

    @Override
    public void updateLinkState(boolean state) {

    }

    @Override
    public void switchBlueBottomState(boolean state) {

    }

    @Override
    public void connectedDeviceName(String factoryNameString, String deviceName, String addr) {

    }

    @Override
    public void onCallbackResult(int flag, boolean state, Object obj) {

        switch (flag) {
            case GlobalValue.CONNECTED_MSG:
                disMissDialog();

                mySharedPf.setString("device_name", deviceName);
                mySharedPf.setString("device_address", deviceAddr);
                mySharedPf.setString("device_factory", braceletType);
                MyApplication.deviceName = deviceName;
                MyApplication.deviceAddr = deviceAddr;
                MyApplication.isSyncing = false;
                linkPresenter.addLinkedDevice(new Device(braceletType, deviceName, deviceAddr));
                Utils.showToast(getApplicationContext(), getString(R.string.linkConnect));
                MyApplication.isDevConnected = true;

                Intent serviceIntent = new Intent(getApplicationContext(), LinkService.class);
                startService(serviceIntent);
                //    Intent intent = new Intent(SearchActivity.this, UserDetailActivity.class);
                //      startActivity(intent);
                finish();
                System.out.println("Search finish");

                break;
            case GlobalValue.CONNECT_TIME_OUT_MSG:
                //     Log.d(TAG, "handleMessage: CONNECT_TIME_OUT_MSG");
                Utils.showToast(getApplicationContext(), getString(R.string.braceletTimeOut));
                MyApplication.isDevConnected = false;
                disMissDialog();
                break;
            case GlobalValue.DISCONNECT_MSG:
                Utils.showToast(getApplicationContext(), getString(R.string.linkFailed));
                MyApplication.isDevConnected = false;
                disMissDialog();
                break;
        }
    }

    @Override
    public void onStepChanged(int step, float distance, int calories, boolean finish_status) {

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
}
