package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.util.Log;


import com.walnutin.xtht.bracelet.ProductList.entity.BandModel;
import com.walnutin.xtht.bracelet.ProductList.ycy.YcySdk;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by chenliu on 2017/1/18.
 * 只和厂家UUID有关
 */

public class ModelConfig {
    private static final String TAG = "ModelConfig";

    private final UUID WYP_SERVICE_UUID = UUID.fromString("F0080001-0451-4000-B000-000000000000");
    private final UUID WYP_NOTIFY_CHAR_UUID = UUID.fromString("F0080002-0451-4000-B000-000000000000");
    private final UUID WYP_CONF_CHAR_UUID = UUID.fromString("F0080003-0451-4000-B000-000000000000");



    private final UUID DEC_1 = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");
    private final UUID DEC_2 = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");



    private final String WYP_SERVICE_UUID_16 = "FEE7";
    private final String WYP_SERVICE_UUID2_16 = "FFFF";
    private final String WYP_SERVICE_UUID3_16 = "0001";
    private final String YCY_SERVICE_UUID_16 = "5554";

    private ArrayList<BandModel> bandModelList = new ArrayList<>();
    private static ModelConfig modelConfig;

    private ModelConfig() {
        init();
    }

    private void init() {
        bandModelList.add(new BandModel(GlobalValue.FACTORY_YCY, true, YcySdk.class, null, null, null, null, YCY_SERVICE_UUID_16));
     //   bandModelList.add(new BandModel(GlobalValue.FACTORY_AD, true, AdSdk.class, null, null, null, null, AD_SERVICE_UUID_16));

    }


    public static ModelConfig getInstance() {
        if (modelConfig == null) {
            modelConfig = new ModelConfig();
        }
        return modelConfig;
    }


    public void initBleOperateByUUID(Context context, String factoryName) {

        Log.d(TAG, "initBleOperate: factoryNameFromXml:" + factoryName);
    //    MyApplication.globalFactoryName = factoryName;
        for (BandModel bandModel : bandModelList) {
            if (bandModel.isSdk()) {
                if (bandModel.getFactoryName() == GlobalValue.FACTORY_YCY) {
                   com.yc.pedometer.sdk.BLEServiceOperate.getInstance(context);

                }
            } else {
                BLEServiceOperate bleServiceOperate = BLEServiceOperate.getInstance(context);
                bleServiceOperate.startBindService(factoryName);
            }
        }
    }

    public String getFactoryNameByUUID(String uuid16, String name) {
        if ("FEE7".equals(uuid16) ) {
            if(name != null){
            //    Log.d(TAG, "getFactoryNameByUUID1: uuid16:"+uuid16  +  " name: "+name);
                if (name.startsWith("HTS")) {
                //    Log.d(TAG, "getFactoryNameByUUID: into HTS");
                    return GlobalValue.FACTORY_FITCLOUD;
                } else {
              //      Log.d(TAG, "getFactoryNameByUUID: into WYP");
                    return GlobalValue.FACTORY_WYP;
                }
            }
        } else {
        //    Log.d(TAG, "getFactoryNameByUUID2: uuid16:"+uuid16  +  " name: "+name);
            for (BandModel bandModel : bandModelList) {
                if (bandModel.getServiceUUID16().equals(uuid16)) {
                    return bandModel.getFactoryName();
                }
            }
        }
        return null;
    }


    public ArrayList<BandModel> getBandModelList() {
        return bandModelList;
    }


    /**
     * may be return  null
     *
     * @param factoryName
     * @return
     */
    public UUID getServiceUUID(String factoryName) {
        for (BandModel bandModel : bandModelList) {
            if (bandModel.getFactoryName().equals(factoryName)) {
                return bandModel.getServiceUUID();
            }
        }
        return null;
    }


    public UUID getNotifyUUID(String factoryName) {
        for (BandModel bandModel : bandModelList) {
            if (bandModel.getFactoryName().equals(factoryName)) {
                return bandModel.getNotifyUUID();
            }
        }
        return null;
    }


    public UUID getConfUUID(String factoryName) {
        for (BandModel bandModel : bandModelList) {
            if (bandModel.getFactoryName().equals(factoryName)) {
                return bandModel.getConfUUID();
            }
        }
        return null;
    }


    public UUID getDEC_2() {
        return DEC_2;
    }
}
