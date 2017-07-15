package com.walnutin.xtht.bracelet.ProductList.entity;

import java.io.Serializable;

/**
 * 作者：MrJiang
 */
public class Device implements Serializable {
    public String deviceName;
    public String deviceAddr;
    private String factoryName;

    public Device(String factoryName, String deviceName, String deviceAddr) {
        this.deviceName = deviceName;
        this.deviceAddr = deviceAddr;
        this.factoryName = factoryName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddr() {
        return deviceAddr;
    }

    public void setDeviceAddr(String deviceAddr) {
        this.deviceAddr = deviceAddr;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
}
