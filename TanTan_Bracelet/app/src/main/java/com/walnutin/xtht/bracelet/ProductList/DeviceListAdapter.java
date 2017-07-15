package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.walnutin.xtht.bracelet.ProductList.entity.MyBleDevice;
import com.walnutin.xtht.bracelet.R;

import java.util.List;

/**
 * 作者：MrJiang on 2016/5/12 17:07
 */
public class DeviceListAdapter extends BaseAdapter {
    private Context mContext;

    List<MyBleDevice> deviceList;
    //String braceletType;

    public DeviceListAdapter(Context context, List<MyBleDevice> mgroupList) {
        mContext = context;
        //     this.braceletType = braceletType;
        deviceList = mgroupList;

    }

    public void setDeviceList(List<MyBleDevice> MyBleDevices) {
        // musicList = MyBleDevices;
//        System.out.println(MyBleDevices.size());
//        musicList.clear();
//        int size = MyBleDevices.size();
//        for (int i = 0;i< size ; i++) {
//
//            musicList.add(MyBleDevices.get(i));
//        }
        deviceList = MyBleDevices;

        notifyDataSetChanged();
    }

    public void addDevice(MyBleDevice device) {
        for (MyBleDevice myBleDevice : deviceList) {
            if (myBleDevice.getDevice().getAddress().equals(device.getDevice().getAddress()) &&
                    myBleDevice.getDevice().getName().equals(device.getDevice().getName())) {
                return;
            }
        }
        deviceList.add(device);
        notifyDataSetChanged();
        // }
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyBleDevice myBleDevice = deviceList.get(position);
        DeviceUtils deviceUtils = null;

        if (convertView == null) {
            deviceUtils = new DeviceUtils();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_seartch_item, null);
            deviceUtils.deviceImage = (ImageView) convertView.findViewById(R.id.left_img);
            deviceUtils.deviceName = (TextView) convertView.findViewById(R.id.centerTitle);

            convertView.setTag(deviceUtils);
        } else {
            deviceUtils = (DeviceUtils) convertView.getTag();
        }
        deviceUtils.deviceName.setText(myBleDevice.getDevice().getName()+myBleDevice.getDevice().getAddress());

        return convertView;
    }

    public class DeviceUtils {

        ImageView deviceImage;
        TextView deviceName;


    }
}
