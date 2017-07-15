package com.walnutin.xtht.bracelet.ProductList;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.walnutin.xtht.bracelet.ProductList.Jinterface.DeviceScanInterfacer;


/**
 * 作者：MrJiang on 2017/1/10 10:13
 */
public class BLECommonScan {
    private static BLECommonScan bleCommonScan;
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private DeviceScanInterfacer deviceScanInterfacer;
    private BluetoothLeService mBluetoothLeService;
    private final static String TAG = BLECommonScan.class.getSimpleName();

    private BLECommonScan(final Context context) {
        this.context = context;

    }

    public static BLECommonScan getInstance(Context context) {
        if (bleCommonScan == null) {
            bleCommonScan = new BLECommonScan(context);
        }
        return bleCommonScan;
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (deviceScanInterfacer != null) {
                deviceScanInterfacer.LeScanCallback(device, rssi, scanRecord);
            }
        }
    };


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isSupportBle4_0() {

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public void setDeviceScanInterfacer(DeviceScanInterfacer deviceScanInterfacer) {
        this.deviceScanInterfacer = deviceScanInterfacer;
    }

    public boolean isBleEnabled() {
        return this.bluetoothAdapter.isEnabled();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startScan() {
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopScan() {

        bluetoothAdapter.stopLeScan(leScanCallback);
    }
}
