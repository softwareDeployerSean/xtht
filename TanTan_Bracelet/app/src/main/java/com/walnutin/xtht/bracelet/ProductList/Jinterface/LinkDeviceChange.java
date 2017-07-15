package com.walnutin.xtht.bracelet.ProductList.Jinterface;

import java.util.List;

/**
 * 作者：MrJiang on 2016/7/14 18:01
 */
public interface LinkDeviceChange {
     void onLinkedDeviceChange(List list);
     void onScanDeviceChanged(List list);
     void onScanSingleDeviceChanged(Object myBleDevice);
}
