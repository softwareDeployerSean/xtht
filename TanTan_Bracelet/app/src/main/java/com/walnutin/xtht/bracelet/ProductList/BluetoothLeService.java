/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.walnutin.xtht.bracelet.ProductList;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.walnutin.xtht.bracelet.ProductList.Jinterface.IConnectionStateCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IDataCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IDataProcessing;

import java.util.List;
import java.util.UUID;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = GlobalValue.DISCONNECT_MSG;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    private UUID mServiceUUID;
    private UUID mNotifyUUID;
    private UUID mConfUUID;
    private IDataProcessing mDataProcessing;
    private boolean mIsGattDead;
    private IDataCallback mIDataCallback;

    IConnectionStateCallback callback;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            Log.d(TAG, "onConnectionStateChange: status" + status);
            Log.d(TAG, "onConnectionStateChange: newState:" + newState);
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                if (mConnectionState == GlobalValue.CONNECTED_MSG) {
                    return;
                }
                mConnectionState = GlobalValue.CONNECTED_MSG;
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                if (mConnectionState == GlobalValue.DISCONNECT_MSG) {
                    return;
                }
                //   broadcastUpdate(intentAction);
                if (callback != null) {
                    Log.d(TAG, "onConnectionStateChange: newState == BluetoothProfile.STATE_DISCONNECTED");
                    mConnectionState = GlobalValue.DISCONNECT_MSG;
                    callback.OnConnetionStateResult(true, GlobalValue.DISCONNECT_MSG);
                }
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            Log.d(TAG, "onServicesDiscovered: 进入了一次onServicesDiscovered");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                //      broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                if (callback != null) {

//                    Log.d(TAG, "onServicesDiscovered: gatt.getServices().size()"+gatt.getDevice().getUuids());
                    BluetoothGattService RxService = gatt.getService(mServiceUUID);
                    if (RxService == null) {
                        Log.d(TAG, "onServicesDiscovered:  RxService == null");
                        callback.OnConnetionStateResult(true, GlobalValue.DISCONNECT_MSG);
                    }
                    enableCharacteristicNotification();

                    Log.d(TAG, "onServicesDiscovered: 发送连接成功消息，次数");
                    callback.OnConnetionStateResult(true, GlobalValue.CONNECTED_MSG);
                }
            }
//            else if(status == BluetoothGatt.GATT_FAILURE){
//                Log.w(TAG, "onServicesDiscovered received: 发送连接失败 注意" + status);
//                callback.OnConnetionStateResult(true, GlobalValue.DISCONNECT_MSG);
//
//            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.w(TAG, "onCharacteristicRead : " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                //        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //       Log.w(TAG, "onCharacteristicChanged : " + DigitalTrans.byteArrHexToString(characteristic.getValue()));
            if (mNotifyUUID.equals(characteristic.getUuid())) {
                mDataProcessing.processingData(characteristic.getValue());
            }
        }


        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
//            Message msg = mDeviceLinkServiceHandler.obtainMessage();
//            msg.what = GlobalVariable.GET_RSSI_MSG;
//            Bundle bundle = new Bundle();
//            bundle.putInt(GlobalVariable.EXTRA_RSSI, rssi);
//            msg.setData(bundle);
//            mDeviceLinkServiceHandler.sendMessage(msg);
            mIDataCallback.onResult(rssi, true, GlobalValue.READ_RSSI_VALUE);
        }
    };

    public void setIDataCallback(IDataCallback iDataCallback) {
        mIDataCallback = iDataCallback;
    }


    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (mNotifyUUID.equals(characteristic.getUuid())) {

            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        }

        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: first in 进入绑定");
        String factoryName = intent.getStringExtra(GlobalValue.FACTORY_NAME);
        Log.d(TAG, "onBind: second factoryname:" + factoryName);
        getCurrentUUID(factoryName);
        return mBinder;
    }


    public void getCurrentUUID(String factoryName) {
        mServiceUUID = ModelConfig.getInstance().getServiceUUID(factoryName);
        mNotifyUUID = ModelConfig.getInstance().getNotifyUUID(factoryName);
        mConfUUID = ModelConfig.getInstance().getConfUUID(factoryName);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        Log.d(TAG, "onUnbind: 解除绑定");
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        registerReceiver(blueStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        mDataProcessing = ProductFactory.getInstance().creatDataProcessingImpl();


     //   Log.d("myresult", "initialize: mDataProcessing:" + mDataProcessing);

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            // Previously connected device.  Try to reconnect.
            if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                    && mBluetoothGatt != null && !mIsGattDead) {
                Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");

                if (mBluetoothGatt.connect()) {
                    mConnectionState = GlobalValue.CONNECTING_MSG;
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (device != null) {
                mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            }
        }

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        mIsGattDead = false;
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = GlobalValue.CONNECTING_MSG;
        return true;
    }


    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public void setICallback(IConnectionStateCallback callback) {
        this.callback = callback;
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param1 Characteristic to act on.
     * @param2 true, enable notification.  False otherwise.
     */
    public void enableCharacteristicNotification() {
        BluetoothGattService RxService = mBluetoothGatt.getService(mServiceUUID);
        if (RxService == null) {
            showMessage("Rx service not found!");
            //        broadcastUpdate(DEVICE_DOES_NOT_SUPPORT);
            return;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(mNotifyUUID);
        if (TxChar == null) {
            showMessage("Tx charateristic not found!");
            //       broadcastUpdate(DEVICE_DOES_NOT_SUPPORT);
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(TxChar, true);

        BluetoothGattDescriptor descriptor2 = TxChar.getDescriptor(ModelConfig.getInstance().getDEC_2());
        descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor2);
    }

    public void writeRXCharacteristic(byte[] value) {
        //   Log.d(TAG, "write writeRXCharacteristic");

        if (!mIsGattDead) {
            BluetoothGattService RxService = mBluetoothGatt.getService(mServiceUUID);
            //    showMessage("mBluetoothGatt null--->  " + mBluetoothGatt);
            if (RxService == null) {
                showMessage("Rx service not found!");
                //     broadcastUpdate(DEVICE_DOES_NOT_SUPPORT);
                return;
            }
            BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(mConfUUID);
            if (RxChar == null) {
                showMessage("Rx charateristic not found!");
                //      broadcastUpdate(DEVICE_DOES_NOT_SUPPORT);
                return;
            }
            RxChar.setValue(value);
            boolean statuss = mBluetoothGatt.writeCharacteristic(RxChar);
           // Log.d(TAG, "write TXchar - status=" + statuss + " : " + DigitalTrans.byteArrHexToString(value));

        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }


    private void showMessage(String msg) {
        Log.e(TAG, msg);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        if (blueStateBroadcastReceiver != null) {
            try {
                unregisterReceiver(blueStateBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void readRssi() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.readRemoteRssi();
        }
    }

    private BroadcastReceiver blueStateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_OFF:
                    Log.i(TAG, "blueState: STATE_OFF");
                    mIsGattDead = true;
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.i(TAG, "blueState: STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.i(TAG, "blueState: STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i(TAG, "blueState: STATE_TURNING_OFF");
                    break;
                default:
                    break;
            }
        }
    };


}
