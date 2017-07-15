package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.util.Log;

import com.walnutin.xtht.bracelet.ProductList.Jinterface.ICommonSDKIntf;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IConnectionStateCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IDataCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHeartRateListener;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IRealDataSubject;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.ISleepListener;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IStepListener;


/**
 * Created by chenliu on 2017/2/24.
 */

public abstract class BleBaseSdk implements ICommonSDKIntf, IConnectionStateCallback, IStepListener, IHeartRateListener, ISleepListener, IDataCallback {
    protected BLEServiceOperate mBLEServiceOperate;
    protected BluetoothLeService mBluetoothLeService;
    protected Context mContext;
    protected IRealDataSubject iDataSubject;
    final String TAG = BleBaseSdk.class.getSimpleName();
    protected IDataCallback mIDataCallback;

    @Override
    public boolean initialize(Context context) {
        mContext = context;
        mBLEServiceOperate = BLEServiceOperate.getInstance(mContext);
        initService();

        try {
            if (!mBLEServiceOperate.isSupportBle4_0()) {
                return false;
            }
            mBluetoothLeService = mBLEServiceOperate.getBleService();

            if (mBluetoothLeService != null) {
                mBluetoothLeService.setICallback(this);
                mBluetoothLeService.setIDataCallback(this);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void connect(String addr) {
        if (mBLEServiceOperate != null) {
            mBLEServiceOperate.connect(addr);
        }
    }

    @Override
    public void disconnect() {
        Log.i(TAG, "disconnect");
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
        }
    }

    @Override
    public void readRssi() {
        mBluetoothLeService.readRssi();
    }


    @Override
    public void setRealDataSubject(IRealDataSubject iDataSubject) {
        this.iDataSubject = iDataSubject;
    }


    @Override
    public void setIDataCallBack(IDataCallback iDataCallBack) {
        this.mIDataCallback = iDataCallBack;
    }

    @Override
    public void onResult(Object data, boolean state, int flag) {
        if(flag == GlobalValue.READ_RSSI_VALUE){
            mIDataCallback.onResult(data,state,flag);
        }
    }

    @Override
    public void onSynchronizingResult(String data, boolean state, int status) {
    }
}
