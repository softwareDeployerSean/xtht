package com.walnutin.xtht.bracelet.ProductList.entity;



import com.walnutin.xtht.bracelet.ProductList.Jinterface.ICommonSDKIntf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by chenliu on 2017/2/28.
 */

public class BandModel {

    private String factoryName;
    private UUID serviceUUID;
    private UUID notifyUUID;
    private UUID writeUUID;
    private boolean isSdk;
    private String serviceUUID16;
    private Class dataProcessingClazz;
    private Class sdkClazz;


    public BandModel(String factoryName, boolean isSdk, Class sdkClazz, Class dataProcessingClazz, UUID serviceUUID, UUID notifyUUID, UUID writeUUID, String serviceUUID16) {

        this.factoryName = factoryName;
        this.isSdk = isSdk;
        this.serviceUUID = serviceUUID;
        this.notifyUUID = notifyUUID;
        this.writeUUID = writeUUID;
        this.serviceUUID16 = serviceUUID16;
        this.dataProcessingClazz = dataProcessingClazz;
        this.sdkClazz = sdkClazz;
    }


    public String getFactoryName() {
        return factoryName;
    }

    public UUID getServiceUUID() {
        return serviceUUID;
    }

    public UUID getNotifyUUID() {
        return notifyUUID;
    }

    public UUID getConfUUID() {
        return writeUUID;
    }

    public boolean isSdk() {
        return isSdk;
    }

    public String getServiceUUID16() {
        return serviceUUID16;
    }

    public Class getDataProcessingClazz() {
        return dataProcessingClazz;
    }

    public ICommonSDKIntf getSdkInstance() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Method method = sdkClazz.getMethod("getInstance");
        return (ICommonSDKIntf) method.invoke(null);
    }


}
