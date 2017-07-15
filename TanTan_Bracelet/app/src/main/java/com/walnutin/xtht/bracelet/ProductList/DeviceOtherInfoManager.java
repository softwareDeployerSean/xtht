package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;



/**
 * Created by caro on 16/6/8.
 * <p>
 * please instance this singon in application.maybe
 */
public class DeviceOtherInfoManager {

    private static DeviceOtherInfoManager instance;
    private Context context;
    private DeviceOtherSetting deviceOtherSetting;
    DeviceSharedPf deviceSharedPf;

    /**
     * @param context
     * @return
     */
    public static DeviceOtherInfoManager getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceOtherInfoManager(context);
        }
        return instance;
    }

    public DeviceOtherInfoManager(Context mcontext) {
        this.context = mcontext;
        deviceOtherSetting = new DeviceOtherSetting();
        deviceSharedPf = DeviceSharedPf.getInstance(context);

    }

    public DeviceOtherSetting getLocalDeviceOtherSettingInfo() {
        deviceOtherSetting = (DeviceOtherSetting) Conversion.stringToObject(deviceSharedPf.getString("deviceOtherSettingInfo", null));
        if (deviceOtherSetting == null) {
            deviceOtherSetting = new DeviceOtherSetting();

        }
        return deviceOtherSetting;
    }

    public void setDeviceOtherSetting(DeviceOtherSetting deviceOtherSetting) {
        this.deviceOtherSetting = deviceOtherSetting;
    }

    public void saveDeviceOtherInfo() {
        deviceSharedPf.setString("deviceOtherSettingInfo", Conversion.objectToString(deviceOtherSetting)); // 保存推送设置
    }


    public boolean isUnLost() {
        return deviceOtherSetting.isUnLost;
    }

    public void setUnLost(boolean unLost) {
        deviceOtherSetting.isUnLost = unLost;
    }

    public boolean isLongSitRemind() {
        return deviceOtherSetting.longSitRemind;
    }

    public void setLongSitRemind(boolean longSitRemind) {
        this.deviceOtherSetting.longSitRemind = longSitRemind;
    }

    public int getLongSitTime() {
        return deviceOtherSetting.longSitTime;
    }

    public void setLongSitTime(int longSitTime) {
        this.deviceOtherSetting.longSitTime = longSitTime;
    }

    public int getLightScreenTime() {
        return deviceOtherSetting.lightScreenTime;
    }

    public void setLightScreenTime(int lightScreenTime) {
        this.deviceOtherSetting.lightScreenTime = lightScreenTime;
    }

    public String getLongSitStartTime() {
        return deviceOtherSetting.longSitStartTime;
    }

    public void setLongSitStartTime(String longSitStartTime) {
        this.deviceOtherSetting.longSitStartTime = longSitStartTime;
    }

    public String getLongSitEndTime() {
        return deviceOtherSetting.longSitEndTime;
    }

    public void setLongSitEndTime(String longSitEndTime) {
        this.deviceOtherSetting.longSitEndTime = longSitEndTime;
    }
}
