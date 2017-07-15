package com.walnutin.xtht.bracelet.ProductList.Jinterface;

import android.bluetooth.BluetoothDevice;

public interface DeviceScanInterfacer {
    void LeScanCallback(BluetoothDevice device, int rssi, byte[] scanRecaord);
}
