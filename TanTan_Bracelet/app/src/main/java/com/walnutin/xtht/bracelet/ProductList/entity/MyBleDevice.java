package com.walnutin.xtht.bracelet.ProductList.entity;

import android.bluetooth.BluetoothDevice;

/**
 * Created by chenliu on 2017/2/22.
 */

public class MyBleDevice {

    private BluetoothDevice device;
    private String factoryName;

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
}
