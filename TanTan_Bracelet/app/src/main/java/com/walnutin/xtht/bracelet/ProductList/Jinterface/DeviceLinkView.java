package com.walnutin.xtht.bracelet.ProductList.Jinterface;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * 作者：MrJiang on 2016/7/13 16:49
 */
public interface DeviceLinkView {

    List<BluetoothDevice> getScanListDevices();

    List<BluetoothDevice> getLinkedListDevices();

    void updateScanListDevices(List list);
    void updateScanSingleDevice(Object object);

    void updateLinkedListDevices(List list);

    void updateLinkState(boolean state);

    void switchBlueBottomState(boolean state);

    void connectedDeviceName(String factoryNameString, String deviceName, String addr);
}
