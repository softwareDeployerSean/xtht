package com.walnutin.xtht.bracelet.ProductList.Jinterface;

import android.bluetooth.BluetoothDevice;

/**
 * Created by chenliu on 2017/4/11.
 */

public interface IHardScanCallback {

    void onFindDevice(BluetoothDevice device, int rssi, String factoryNameByUUID, byte[] scanRecord);

}
