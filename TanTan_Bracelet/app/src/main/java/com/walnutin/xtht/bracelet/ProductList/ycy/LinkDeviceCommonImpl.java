package com.walnutin.xtht.bracelet.ProductList.ycy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.walnutin.xtht.bracelet.ProductList.BLECommonScan;
import com.walnutin.xtht.bracelet.ProductList.Conversion;
import com.walnutin.xtht.bracelet.ProductList.DeviceSharedPf;
import com.walnutin.xtht.bracelet.ProductList.GlobalValue;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.DeviceScanInterfacer;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.LinkDeviceChange;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.LinkDeviceIntf;
import com.walnutin.xtht.bracelet.ProductList.ModelConfig;
import com.walnutin.xtht.bracelet.ProductList.entity.Device;
import com.walnutin.xtht.bracelet.ProductList.entity.MyBleDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MrJiang on 2016/7/13 16:49
 */
public class LinkDeviceCommonImpl implements LinkDeviceIntf {
    private final String TAG = "LinkDeviceCommonImpl";
    private List<MyBleDevice> mLeDevices;
    private DeviceSharedPf mySharedPf;
    private Context mContext;
    private BLECommonScan mBleCommonScan;
    private DeviceScanInterfacer deviceScanInterfacer;
    public boolean closeBluetooth = false;
    private BluetoothAdapter bluetoothAdapter;
    LinkDeviceChange linkBlueDevice;
    List<Device> linkedDeviceList;


    public LinkDeviceCommonImpl(Context context) {
        mContext = context;
        mySharedPf = DeviceSharedPf.getInstance(context);
        mBleCommonScan = BLECommonScan.getInstance(context);// 用于BluetoothLeService实例化准备

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceScanInterfacer = new DeviceScanOpen();
        mBleCommonScan.setDeviceScanInterfacer(deviceScanInterfacer);
        mLeDevices = new ArrayList<>();
    }

    public boolean isSupportBle4_0() {
        if (mBleCommonScan.isSupportBle4_0()) {
            return true;
        }
        return false;
    }

    public void addLinkedDevice(Device device) {

        if (device == null || device.getDeviceName() == null || device.getDeviceAddr() == null || device.deviceAddr.equals("")) {
            return;
        }

        int index = -1;

        for (int i = 0; i < linkedDeviceList.size(); i++) {
            Device device1 = linkedDeviceList.get(i);
            if (device1 != null && device1.getDeviceName() != null && device1.getDeviceAddr() != null && device1.getDeviceName().equals(device.getDeviceName()) && device1.getDeviceAddr().equals(device.getDeviceAddr())) {
                //           Log.d(TAG, "addLinkedDevice: remove");
                //    linkedDeviceList.remove(i);
                index = i;
                break;
            }
        }
        if (index != -1) {
            linkedDeviceList.remove(index);
        }
        linkedDeviceList.add(0, device);
        //添加至队首
        setLinkedDevice(linkedDeviceList);
    }

    public void removeLinkedDeviceByName(String deviceName) {
        for (Device device : linkedDeviceList) {
            if (device.getDeviceName().equals(deviceName)) {
                linkedDeviceList.remove(device);
                return;
            }
        }
    }

    public void removeLinkedDeviceByDeviceAddr(String deviceAddr) {
        for (Device device : linkedDeviceList) {
            if (device.getDeviceAddr().equals(deviceAddr)) {
                linkedDeviceList.remove(device);
                return;
            }
        }
    }

    @Override
    public void removeLinkedDevice(Device device) {
        for (Device device2 : linkedDeviceList) {
            if (device2.getDeviceAddr().equals(device.getDeviceAddr()) && device2.getDeviceName().equals(device.getDeviceName())) {
                linkedDeviceList.remove(device2);
                setLinkedDevice(linkedDeviceList);
                return;
            }
        }
    }

    @Override
    public List getLocalLinkedList() {
        linkedDeviceList = (List<Device>) Conversion.stringToList(mySharedPf.getString("_devLinkedList", null));
        if (linkedDeviceList == null) {
            linkedDeviceList = new ArrayList<>();
        }
        setLinkedDevice(linkedDeviceList);
        return linkedDeviceList;
    }

    @Override
    public void saveLinked() {
        mySharedPf.setString("_devLinkedList", Conversion.listToString(linkedDeviceList));

    }

//    public List getLocalLinkedList() {
//        linkedDeviceList = (List<Device>) Conversion.stringToList(mySharedPf.getString(MyApplication.account + "_devLinkedList", null));
//        if (linkedDeviceList == null) {
//            linkedDeviceList = new ArrayList<>();
//        }
//        setLinkedDevice(linkedDeviceList);
//        return linkedDeviceList;
//    }
//
//    public void saveLinked() {
//        mySharedPf.setString(MyApplication.account + "_devLinkedList", Conversion.listToString(linkedDeviceList));
//    }

    @Override
    public boolean isBleOpened() {
        return mBleCommonScan.isBleEnabled();
    }

    @Override
    public void startScan() {
        //获取已配对过的蓝牙设备
        scanLeDevice(true);
    }


    private void scanLeDevice(final boolean enable) {
        mBleCommonScan.stopScan();
        mBleCommonScan.startScan();
    }

    @Override
    public void stopScan() {
        mBleCommonScan.stopScan();
    }

    @Override
    public List getLinkedDevices() {
        return linkedDeviceList;
    }


    public void setLinkedDevice(List l) {
        if (linkBlueDevice != null) {
            linkBlueDevice.onLinkedDeviceChange(l);
        }
    }

    @Override
    public List getScanListDevice() {
        return mLeDevices;
    }

    @Override
    public void setScanListDevice(List listDevice) {
        mLeDevices = listDevice;
        if (linkBlueDevice != null) {
            linkBlueDevice.onScanDeviceChanged(mLeDevices);
        }
    }


    private class DeviceScanOpen implements DeviceScanInterfacer {

        @Override
        public void LeScanCallback(final BluetoothDevice device, int i, byte[] scanRecaord) {
            boolean hasDevice = false;
            for (MyBleDevice bluetoothDevice : mLeDevices) {
                if (bluetoothDevice.getDevice().getAddress() != null && bluetoothDevice.getDevice().getName() != null) {

                    if (bluetoothDevice.getDevice().getAddress().equals(device.getAddress()) && bluetoothDevice.getDevice().getName().equals(device.getName())) {
                        hasDevice = true;
                        return;
                    }
                }
            }

            String serviceUUIDString = UUIDParser.getInstance().getServiceUUIDString(scanRecaord);
            //   System.out.println("DeviceScanOpen: "+device.getName() +" addr: "+device.getAddress()+" uuid: "+serviceUUIDString);

            if (!hasDevice) {
                if (device.getName() != null) {
                    if (serviceUUIDString != null) {
                        String factoryNameByUUID = ModelConfig.getInstance().getFactoryNameByUUID(serviceUUIDString, device.getName());
                        if (factoryNameByUUID != null) {
                            //      Log.i(TAG,device.getName()+" --  "+device.getAddress());
                            if (factoryNameByUUID.equals(GlobalValue.FACTORY_YCY) || factoryNameByUUID.equals(GlobalValue.FACTORY_WYP)) {
                            } else {
                                return;
                            }
                            MyBleDevice myBleDevice = new MyBleDevice();
                            myBleDevice.setDevice(device);
                            myBleDevice.setFactoryName(factoryNameByUUID);
                            mLeDevices.add(myBleDevice);
                            if (linkBlueDevice != null) {
                                linkBlueDevice.onScanSingleDeviceChanged(myBleDevice);
                            }

                        }
                    }
                }

            }

        }
    }


    @Override
    public void clearScanAndLinkedList() {
        mLeDevices.clear();
        setScanListDevice(mLeDevices);
    }

    @Override
    public void setOnLinkBlueDeviceChange(LinkDeviceChange linkBlueDevice) {
        this.linkBlueDevice = linkBlueDevice;

    }
//
//    @Override
//    public void setOnLinkBlueDeviceChange(LinkDeviceChange linkBlueDevices) {
//        linkBlueDevice = linkBlueDevices;
//    }
}
