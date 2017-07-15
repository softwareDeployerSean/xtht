package com.walnutin.xtht.bracelet.ProductList;

import android.content.BroadcastReceiver;
import android.content.Context;



import com.walnutin.xtht.bracelet.ProductList.Jinterface.DeviceLinkView;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.LinkDeviceChange;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.LinkDeviceIntf;
import com.walnutin.xtht.bracelet.ProductList.entity.Device;
import com.walnutin.xtht.bracelet.ProductList.ycy.LinkDeviceCommonImpl;

import java.util.List;

/**
 * 作者：MrJiang
 */
public class LinkPresenter implements LinkDeviceChange {
    private LinkDeviceIntf linkDeviceIntf;
    private DeviceLinkView deviceLinkView;
    Context mContext;
    private BroadcastReceiver mStatusReceiver;


    public LinkPresenter(DeviceLinkView dlv, Context context) {
        deviceLinkView = dlv;
        linkDeviceIntf = new LinkDeviceCommonImpl(context);
        mContext = context;
        linkDeviceIntf.setOnLinkBlueDeviceChange(this);
    }

    public boolean isSupportBle4_0() {
        return linkDeviceIntf.isSupportBle4_0();
    }

    public boolean isBleOpen() {
        return linkDeviceIntf.isBleOpened();
    }

    public void startScan() {
        linkDeviceIntf.startScan();
    }

    public void stopScan() {
        linkDeviceIntf.stopScan();
    }

    public void clearDeviceList() {
        linkDeviceIntf.clearScanAndLinkedList();
    }

    public void saveLinkedDevice() {
        linkDeviceIntf.saveLinked();
    }

    public List getLocalLinkedDeviceList() {
        return linkDeviceIntf.getLocalLinkedList();
    }

    public List getScanDeviceList() {
        return linkDeviceIntf.getScanListDevice();
    }

    public List getLinkedDeviceList() {
        return linkDeviceIntf.getLinkedDevices();
    }

    public void removeDevice(Device device) {
        linkDeviceIntf.removeLinkedDevice(device);
    }

    public void addLinkedDevice(Device device) {
        linkDeviceIntf.addLinkedDevice(device);
    }



    @Override
    public void onLinkedDeviceChange(List list) {
        deviceLinkView.updateLinkedListDevices(list);
    }

    @Override
    public void onScanDeviceChanged(List list) {
        deviceLinkView.updateScanListDevices(list);
    }

    @Override
    public void onScanSingleDeviceChanged(Object myBleDevice) {
        deviceLinkView.updateScanSingleDevice(myBleDevice);
    }
}
