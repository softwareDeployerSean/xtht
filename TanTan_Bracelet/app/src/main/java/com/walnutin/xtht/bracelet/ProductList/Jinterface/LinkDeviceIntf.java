package com.walnutin.xtht.bracelet.ProductList.Jinterface;




import com.walnutin.xtht.bracelet.ProductList.entity.Device;

import java.util.List;

/**
 * 作者：MrJiang on 2016/7/13 16:49
 */
public interface LinkDeviceIntf {
    void startScan();

    void stopScan();

    List getLinkedDevices();

    List getScanListDevice();

    void addLinkedDevice(Device device);

    void removeLinkedDevice(Device device);

    List getLocalLinkedList();

    void saveLinked();

    void setScanListDevice(List listDevice);

    boolean isSupportBle4_0();

    boolean isBleOpened();

    void clearScanAndLinkedList();

    void setOnLinkBlueDeviceChange(LinkDeviceChange linkBlueDevice);

}
